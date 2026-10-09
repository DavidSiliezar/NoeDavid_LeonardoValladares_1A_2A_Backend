package SystemITR.JosueGuinea1A.Movimientos.Service;

import SystemITR.JosueGuinea1A.Cuentas.Entity.cuentaEntity;
import SystemITR.JosueGuinea1A.Cuentas.Repository.CuentaRepository;
import SystemITR.JosueGuinea1A.Movimientos.DTO.MovimientoRequestDTO;
import SystemITR.JosueGuinea1A.Movimientos.DTO.MovimientoResponseDTO;
import SystemITR.JosueGuinea1A.Movimientos.Entity.movimientoEntity;
import SystemITR.JosueGuinea1A.Movimientos.Repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class movimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public movimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> obtenerPorCuenta(Long idCuenta,
                                                        String tipo,
                                                        String categoria,
                                                        LocalDate fechaInicio,
                                                        LocalDate fechaFin) {
        if (!cuentaRepository.existsById(idCuenta)) {
            throw new RuntimeException("Cuenta no encontrada con ID: " + idCuenta);
        }

        List<movimientoEntity> movimientos = movimientoRepository
                .findByCuenta_IdCuentaOrderByFechaDescIdMovimientoDesc(idCuenta);

        return movimientos.stream()
                .filter(m -> {
                    if (tipo != null && !tipo.isBlank()) {
                        if (m.getTipo() == null || !m.getTipo().equalsIgnoreCase(tipo.trim())) {
                            return false;
                        }
                    }
                    if (categoria != null && !categoria.isBlank()) {
                        if (m.getCategoria() == null || !m.getCategoria().toLowerCase().contains(categoria.trim().toLowerCase())) {
                            return false;
                        }
                    }
                    if (fechaInicio != null || fechaFin != null) {
                        LocalDate fechaMov = convertirALocalDate(m.getFecha());
                        if (fechaMov == null) {
                            return false;
                        }
                        if (fechaInicio != null && fechaMov.isBefore(fechaInicio)) {
                            return false;
                        }
                        if (fechaFin != null && fechaMov.isAfter(fechaFin)) {
                            return false;
                        }
                    }
                    return true;
                })
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovimientoResponseDTO obtenerPorId(Long idMovimiento) {
        movimientoEntity movimiento = movimientoRepository.findById(idMovimiento)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + idMovimiento));
        return mapearAResponse(movimiento);
    }

    @Transactional
    public MovimientoResponseDTO crear(MovimientoRequestDTO dto) {
        if (dto.getIdCuenta() == null) {
            throw new RuntimeException("El ID de la cuenta es obligatorio para registrar un movimiento");
        }

        cuentaEntity cuenta = cuentaRepository.findById(dto.getIdCuenta())
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + dto.getIdCuenta()));

        if (cuenta.getActiva() == null || !"S".equalsIgnoreCase(cuenta.getActiva().trim())) {
            throw new RuntimeException("No se puede registrar un movimiento en una cuenta inactiva");
        }

        movimientoEntity movimiento = new movimientoEntity();
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(dto.getFecha() != null ? dto.getFecha() : new Date());
        movimiento.setTipo(dto.getTipo().trim().toUpperCase());
        movimiento.setCategoria(dto.getCategoria().trim());
        movimiento.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        movimiento.setMonto(dto.getMonto().setScale(2, RoundingMode.HALF_UP));

        movimientoEntity guardado = movimientoRepository.save(movimiento);
        return mapearAResponse(guardado);
    }

    @Transactional
    public MovimientoResponseDTO actualizar(Long idMovimiento, MovimientoRequestDTO dto) {
        movimientoEntity movimiento = movimientoRepository.findById(idMovimiento)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + idMovimiento));

        cuentaEntity cuenta = movimiento.getCuenta();
        if (dto.getIdCuenta() != null && !dto.getIdCuenta().equals(cuenta.getIdCuenta())) {
            cuenta = cuentaRepository.findById(dto.getIdCuenta())
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + dto.getIdCuenta()));
            movimiento.setCuenta(cuenta);
        }

        if (cuenta.getActiva() == null || !"S".equalsIgnoreCase(cuenta.getActiva().trim())) {
            throw new RuntimeException("No se puede modificar un movimiento de una cuenta inactiva");
        }

        if (dto.getFecha() != null) {
            movimiento.setFecha(dto.getFecha());
        }
        movimiento.setTipo(dto.getTipo().trim().toUpperCase());
        movimiento.setCategoria(dto.getCategoria().trim());
        movimiento.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : null);
        movimiento.setMonto(dto.getMonto().setScale(2, RoundingMode.HALF_UP));

        movimientoEntity actualizado = movimientoRepository.save(movimiento);
        return mapearAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long idMovimiento) {
        if (!movimientoRepository.existsById(idMovimiento)) {
            throw new RuntimeException("Movimiento no encontrado con ID: " + idMovimiento);
        }
        movimientoRepository.deleteById(idMovimiento);
    }

    private LocalDate convertirALocalDate(Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof java.sql.Date sqlDate) {
            return sqlDate.toLocalDate();
        }
        return Instant.ofEpochMilli(fecha.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private MovimientoResponseDTO mapearAResponse(movimientoEntity movimiento) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setIdMovimiento(movimiento.getIdMovimiento());
        if (movimiento.getCuenta() != null) {
            dto.setIdCuenta(movimiento.getCuenta().getIdCuenta());
            dto.setNombreCuenta(movimiento.getCuenta().getNombre());
        }
        dto.setFecha(movimiento.getFecha());
        dto.setTipo(movimiento.getTipo());
        dto.setCategoria(movimiento.getCategoria());
        dto.setDescripcion(movimiento.getDescripcion());
        dto.setMonto(movimiento.getMonto() != null
                ? movimiento.getMonto().setScale(2, RoundingMode.HALF_UP)
                : null);
        return dto;
    }
}
