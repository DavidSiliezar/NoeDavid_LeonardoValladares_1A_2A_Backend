package SystemITR.JosueGuinea1A.Usuarios.DTO;

import SystemITR.JosueGuinea1A.Cuentas.DTO.CuentaResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ResponseDTO {

    private Long idUsuario;
    private String nombre;
    private String correo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaRegistro;

    private List<CuentaResponseDTO> cuentas;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<CuentaResponseDTO> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaResponseDTO> cuentas) {
        this.cuentas = cuentas;
    }
}
