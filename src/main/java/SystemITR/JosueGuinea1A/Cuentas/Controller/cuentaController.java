package SystemITR.JosueGuinea1A.Cuentas.Controller;

import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaRequestDTO;
import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaResponseDTO;
import SystemITR.JosueGuinea1A.Cuentas.Service.cuentaService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin
public class cuentaController {

    private final cuentaService service;

    public cuentaController(cuentaService service) {
        this.service = service;
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<CuentaResponseDTO>>> obtenerPorUsuario(@PathVariable Long idUsuario) {
        List<CuentaResponseDTO> cuentas = service.obtenerPorUsuario(idUsuario);
        ApiResponse<List<CuentaResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuentas del usuario obtenidas correctamente",
                cuentas
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(params = "idUsuario")
    public ResponseEntity<ApiResponse<List<CuentaResponseDTO>>> obtenerPorUsuarioParam(@RequestParam Long idUsuario) {
        List<CuentaResponseDTO> cuentas = service.obtenerPorUsuario(idUsuario);
        ApiResponse<List<CuentaResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuentas del usuario obtenidas correctamente",
                cuentas
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        CuentaResponseDTO cuenta = service.obtenerPorId(id);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Detalle de la cuenta obtenido correctamente",
                cuenta
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/resumen")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> obtenerResumen(@PathVariable Long id) {
        CuentaResponseDTO cuenta = service.obtenerPorId(id);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Resumen de la cuenta obtenido correctamente",
                cuenta
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> crear(@Valid @RequestBody CuentaRequestDTO dto) {
        CuentaResponseDTO creada = service.crear(dto);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Cuenta registrada correctamente",
                creada
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> actualizar(@PathVariable Long id,
                                                                     @Valid @RequestBody CuentaRequestDTO dto) {
        CuentaResponseDTO actualizada = service.actualizar(id, dto);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuenta actualizada correctamente",
                actualizada
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> desactivarPut(@PathVariable Long id) {
        CuentaResponseDTO desactivada = service.desactivar(id);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuenta desactivada correctamente",
                desactivada
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> desactivarPatch(@PathVariable Long id) {
        CuentaResponseDTO desactivada = service.desactivar(id);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuenta desactivada correctamente",
                desactivada
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Cuenta eliminada correctamente"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
