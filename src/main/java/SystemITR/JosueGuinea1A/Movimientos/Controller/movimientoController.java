package SystemITR.JosueGuinea1A.Movimientos.Controller;

import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaResponseDTO;
import SystemITR.JosueGuinea1A.Cuentas.Service.cuentaService;
import SystemITR.JosueGuinea1A.Movimientos.DTO.MovimientoRequestDTO;
import SystemITR.JosueGuinea1A.Movimientos.DTO.MovimientoResponseDTO;
import SystemITR.JosueGuinea1A.Movimientos.Service.movimientoService;
import SystemITR.JosueGuinea1A.Response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin
public class movimientoController {

    private final movimientoService service;
    private final cuentaService servicioCuentas;

    public movimientoController(movimientoService service, cuentaService servicioCuentas) {
        this.service = service;
        this.servicioCuentas = servicioCuentas;
    }

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> obtenerPorCuenta(
            @PathVariable Long idCuenta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<MovimientoResponseDTO> movimientos = service.obtenerPorCuenta(idCuenta, tipo, categoria, fechaInicio, fechaFin);
        ApiResponse<List<MovimientoResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimientos obtenidos correctamente",
                movimientos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(params = "idCuenta")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> obtenerPorCuentaParam(
            @RequestParam Long idCuenta,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<MovimientoResponseDTO> movimientos = service.obtenerPorCuenta(idCuenta, tipo, categoria, fechaInicio, fechaFin);
        ApiResponse<List<MovimientoResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimientos obtenidos correctamente",
                movimientos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{idCuenta}/tipo/{tipo}")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> filtrarPorTipo(
            @PathVariable Long idCuenta,
            @PathVariable String tipo) {
        List<MovimientoResponseDTO> movimientos = service.obtenerPorCuenta(idCuenta, tipo, null, null, null);
        ApiResponse<List<MovimientoResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimientos filtrados por tipo correctamente",
                movimientos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{idCuenta}/categoria/{categoria}")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> filtrarPorCategoria(
            @PathVariable Long idCuenta,
            @PathVariable String categoria) {
        List<MovimientoResponseDTO> movimientos = service.obtenerPorCuenta(idCuenta, null, categoria, null, null);
        ApiResponse<List<MovimientoResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimientos filtrados por categoría correctamente",
                movimientos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{idCuenta}/fechas")
    public ResponseEntity<ApiResponse<List<MovimientoResponseDTO>>> filtrarPorFechas(
            @PathVariable Long idCuenta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<MovimientoResponseDTO> movimientos = service.obtenerPorCuenta(idCuenta, null, null, fechaInicio, fechaFin);
        ApiResponse<List<MovimientoResponseDTO>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimientos filtrados por rango de fechas correctamente",
                movimientos
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cuenta/{idCuenta}/resumen")
    public ResponseEntity<ApiResponse<CuentaResponseDTO>> obtenerResumenCuenta(@PathVariable Long idCuenta) {
        CuentaResponseDTO resumen = servicioCuentas.obtenerPorId(idCuenta);
        ApiResponse<CuentaResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Resumen de ingresos, gastos y saldo actual obtenido correctamente",
                resumen
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> obtenerPorId(@PathVariable Long id) {
        MovimientoResponseDTO movimiento = service.obtenerPorId(id);
        ApiResponse<MovimientoResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimiento obtenido correctamente",
                movimiento
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> crear(@Valid @RequestBody MovimientoRequestDTO dto) {
        MovimientoResponseDTO creado = service.crear(dto);
        ApiResponse<MovimientoResponseDTO> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Movimiento registrado correctamente",
                creado
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MovimientoResponseDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoRequestDTO dto) {
        MovimientoResponseDTO actualizado = service.actualizar(id, dto);
        ApiResponse<MovimientoResponseDTO> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimiento actualizado correctamente",
                actualizado
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Movimiento eliminado correctamente"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
