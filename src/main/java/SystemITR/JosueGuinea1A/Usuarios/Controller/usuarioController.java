package SystemITR.JosueGuinea1A.Usuarios.Controller;

import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaResponseDTO;
import SystemITR.JosueGuinea1A.Cuentas.Service.cuentaService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import SystemITR.JosueGuinea1A.Usuarios.DTO.RequestDTO;
import SystemITR.JosueGuinea1A.Usuarios.DTO.ResponseDTO;
import SystemITR.JosueGuinea1A.Usuarios.Service.usuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin
public class usuarioController {

    private final usuarioService service;
    private final cuentaService servicioCuentas;

    public usuarioController(usuarioService service, cuentaService servicioCuentas) {
        this.service = service;
        this.servicioCuentas = servicioCuentas;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseDTO>>> obtenerTodos() {
        List<ResponseDTO> usuarios = service.obtenerTodos();
        ApiResponse<List<ResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Lista de usuarios obtenida , no hay mas datos para mostrar",
                usuarios
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> obtenerPorId(@PathVariable Long id) {
        ResponseDTO usuario = service.obtenerPorId(id);
        ApiResponse<ResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario obtenido correctamente",
                usuario
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/cuentas")
    public ResponseEntity<ApiResponse<List<CuentaResponseDTO>>> obtenerCuentasPorUsuario(@PathVariable Long id) {
        List<CuentaResponseDTO> cuentas = servicioCuentas.obtenerPorUsuario(id);
        ApiResponse<List<CuentaResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuentas del usuario obtenidas correctamente",
                cuentas
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResponseDTO>> crear(@Valid @RequestBody RequestDTO dto) {
        ResponseDTO creado = service.crear(dto);
        ApiResponse<ResponseDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Usuario registrado correctamente",
                creado
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody RequestDTO dto) {
        ResponseDTO actualizado = service.actualizar(id, dto);
        ApiResponse<ResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario actualizado correctamente",
                actualizado
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Usuario eliminado correctamente"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
