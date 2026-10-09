package SystemITR.JosueGuinea1A.Cuentas.Service;

import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaRequestDTO;
import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaResponseDTO;
import SystemITR.JosueGuinea1A.Cuentas.Entity.cuentaEntity;
import SystemITR.JosueGuinea1A.Cuentas.Repository.CuentaRepository;
import SystemITR.JosueGuinea1A.Movimientos.Entity.movimientoEntity;
import SystemITR.JosueGuinea1A.Movimientos.Repository.MovimientoRepository;
import SystemITR.JosueGuinea1A.Usuarios.Entity.usuarioEntity;
import SystemITR.JosueGuinea1A.Usuarios.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class cuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoRepository movimientoRepository;

    public cuentaService(CuentaRepository cuentaRepository,
                         UsuarioRepository usuarioRepository,
                         MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> obtenerPorUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return cuentaRepository.findByUsuario_IdUsuarioOrderByIdCuentaAsc(idUsuario)
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaResponseDTO obtenerPorId(Long idCuenta) {
        cuentaEntity cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + idCuenta));
        return mapearAResponse(cuenta);
    }

    @Transactional
    public CuentaResponseDTO crear(CuentaRequestDTO dto) {
        if (dto.getIdUsuario() == null) {
            throw new RuntimeException("El ID del usuario es obligatorio para registrar una cuenta");
        }
        usuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));

        String activa = (dto.getActiva() != null && !dto.getActiva().isBlank())
                ? dto.getActiva().trim().toUpperCase()
                : "S";

        BigDecimal saldoInicial = dto.getSaldoInicial().setScale(2, RoundingMode.HALF_UP);
        if ("N".equalsIgnoreCase(activa) && saldoInicial.compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Solo se puede desactivar una cuenta cuando su saldo se encuentre en $0.00");
        }

        cuentaEntity cuenta = new cuentaEntity();
        cuenta.setUsuario(usuario);
        cuenta.setNombre(dto.getNombre().trim());
        cuenta.setTipo(dto.getTipo().trim().toUpperCase());
        cuenta.setSaldoInicial(saldoInicial);
        cuenta.setActiva(activa);

        cuentaEntity guardada = cuentaRepository.save(cuenta);
        return mapearAResponse(guardada);
    }

    @Transactional
    public CuentaResponseDTO actualizar(Long idCuenta, CuentaRequestDTO dto) {
        cuentaEntity cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + idCuenta));

        if (dto.getIdUsuario() != null) {
            usuarioEntity usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario()));
            cuenta.setUsuario(usuario);
        }

        BigDecimal nuevoSaldoInicial = dto.getSaldoInicial().setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalIngresos = calcularTotalPorTipo(idCuenta, "INGRESO");
        BigDecimal totalGastos = calcularTotalPorTipo(idCuenta, "GASTO");
        BigDecimal nuevoSaldoActual = nuevoSaldoInicial.add(totalIngresos).subtract(totalGastos);

        String nuevaActiva = (dto.getActiva() != null && !dto.getActiva().isBlank())
                ? dto.getActiva().trim().toUpperCase()
                : cuenta.getActiva();

        if ("N".equalsIgnoreCase(nuevaActiva) && nuevoSaldoActual.compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Solo se puede desactivar una cuenta cuando su saldo actual se encuentre en $0.00. Saldo actual: $" + nuevoSaldoActual);
        }

        cuenta.setNombre(dto.getNombre().trim());
        cuenta.setTipo(dto.getTipo().trim().toUpperCase());
        cuenta.setSaldoInicial(nuevoSaldoInicial);
        cuenta.setActiva(nuevaActiva);

        cuentaEntity actualizada = cuentaRepository.save(cuenta);
        return mapearAResponse(actualizada);
    }

    @Transactional
    public CuentaResponseDTO desactivar(Long idCuenta) {
        cuentaEntity cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + idCuenta));

        CuentaResponseDTO detalle = mapearAResponse(cuenta);
        if (detalle.getSaldoActual().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Solo se puede desactivar una cuenta cuando su saldo actual se encuentre en $0.00. Saldo actual: $" + detalle.getSaldoActual());
        }

        cuenta.setActiva("N");
        cuentaEntity actualizada = cuentaRepository.save(cuenta);
        return mapearAResponse(actualizada);
    }

    @Transactional
    public void eliminar(Long idCuenta) {
        if (!cuentaRepository.existsById(idCuenta)) {
            throw new RuntimeException("Cuenta no encontrada con ID: " + idCuenta);
        }
        if (movimientoRepository.existsByCuenta_IdCuenta(idCuenta)) {
            throw new RuntimeException("No se puede eliminar la cuenta porque tiene movimientos asociados");
        }
        cuentaRepository.deleteById(idCuenta);
    }

    private BigDecimal calcularTotalPorTipo(Long idCuenta, String tipo) {
        List<movimientoEntity> movimientos = movimientoRepository.findByCuenta_IdCuenta(idCuenta);
        return movimientos.stream()
                .filter(m -> m.getTipo() != null && m.getTipo().equalsIgnoreCase(tipo))
                .map(movimientoEntity::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public CuentaResponseDTO mapearAResponse(cuentaEntity cuenta) {
        BigDecimal saldoInicial = cuenta.getSaldoInicial() != null
                ? cuenta.getSaldoInicial().setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalIngresos = calcularTotalPorTipo(cuenta.getIdCuenta(), "INGRESO");
        BigDecimal totalGastos = calcularTotalPorTipo(cuenta.getIdCuenta(), "GASTO");
        BigDecimal saldoActual = saldoInicial.add(totalIngresos).subtract(totalGastos).setScale(2, RoundingMode.HALF_UP);

        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.setIdCuenta(cuenta.getIdCuenta());
        if (cuenta.getUsuario() != null) {
            dto.setIdUsuario(cuenta.getUsuario().getIdUsuario());
            dto.setNombreUsuario(cuenta.getUsuario().getNombre());
        }
        dto.setNombre(cuenta.getNombre());
        dto.setTipo(cuenta.getTipo());
        dto.setSaldoInicial(saldoInicial);
        dto.setTotalIngresos(totalIngresos);
        dto.setTotalGastos(totalGastos);
        dto.setSaldoActual(saldoActual);
        dto.setActiva(cuenta.getActiva() != null ? cuenta.getActiva().trim() : "S");
        return dto;
    }
}
