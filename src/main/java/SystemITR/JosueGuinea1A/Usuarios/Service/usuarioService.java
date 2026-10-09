package SystemITR.JosueGuinea1A.Usuarios.Service;

import SystemITR.JosueGuinea1A.Cuentas.Repository.CuentaRepository;
import SystemITR.JosueGuinea1A.Cuentas.Service.cuentaService;
import SystemITR.JosueGuinea1A.Usuarios.DTO.RequestDTO;
import SystemITR.JosueGuinea1A.Usuarios.DTO.ResponseDTO;
import SystemITR.JosueGuinea1A.Usuarios.Entity.usuarioEntity;
import SystemITR.JosueGuinea1A.Usuarios.Repository.UsuarioRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class usuarioService {

    private final UsuarioRepository repository;
    private final CuentaRepository cuentaRepository;
    private final cuentaService servicioCuentas;

    public usuarioService(UsuarioRepository repository,
                          CuentaRepository cuentaRepository,
                          cuentaService servicioCuentas) {
        this.repository = repository;
        this.cuentaRepository = cuentaRepository;
        this.servicioCuentas = servicioCuentas;
    }

    @Transactional(readOnly = true)
    public List<ResponseDTO> obtenerTodos() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "idUsuario"))
                .stream()
                .map(this::mapearAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseDTO obtenerPorId(Long id) {
        usuarioEntity usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        return mapearAResponse(usuario);
    }

    @Transactional
    public ResponseDTO crear(RequestDTO dto) {
        if (repository.existsByCorreoIgnoreCase(dto.getCorreo().trim())) {
            throw new RuntimeException("Ya existe un usuario registrado con el correo: " + dto.getCorreo());
        }
        usuarioEntity usuario = new usuarioEntity();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setCorreo(dto.getCorreo().trim());
        usuario.setFechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : new Date());

        usuarioEntity guardado = repository.save(usuario);
        return mapearAResponse(guardado);
    }

    @Transactional
    public ResponseDTO actualizar(Long id, RequestDTO dto) {
        usuarioEntity usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (repository.existsByCorreoIgnoreCaseAndIdUsuarioNot(dto.getCorreo().trim(), id)) {
            throw new RuntimeException("Ya existe otro usuario registrado con el correo: " + dto.getCorreo());
        }

        usuario.setNombre(dto.getNombre().trim());
        usuario.setCorreo(dto.getCorreo().trim());
        if (dto.getFechaRegistro() != null) {
            usuario.setFechaRegistro(dto.getFechaRegistro());
        }

        usuarioEntity actualizado = repository.save(usuario);
        return mapearAResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        if (cuentaRepository.existsByUsuario_IdUsuario(id)) {
            throw new RuntimeException("No se puede eliminar el usuario porque posee cuentas registradas");
        }
        repository.deleteById(id);
    }

    private ResponseDTO mapearAResponse(usuarioEntity usuario) {
        ResponseDTO dto = new ResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setCorreo(usuario.getCorreo());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setCuentas(servicioCuentas.obtenerPorUsuario(usuario.getIdUsuario()));
        return dto;
    }
}
