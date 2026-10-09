package SystemITR.JosueGuinea1A.Usuarios.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Date;

public class RequestDTO {


    @NotBlank(message = "nombre es obligatorio")
    private String nombre;


    @NotBlank(message = "correo es obligatorio")
    @Email
    @UniqueElements
    private String correo;

    @NotBlank(message = "No puede quedar vacio")
    private Date fechaRegistro;

    public @NotBlank(message = "nombre es obligatorio") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "nombre es obligatorio") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "correo es obligatorio") @Email String getCorreo() {
        return correo;
    }

    public void setCorreo(@NotBlank(message = "correo es obligatorio") @Email String correo) {
        this.correo = correo;
    }

    public @NotBlank(message = "No puede quedar vacio") Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(@NotBlank(message = "No puede quedar vacio") Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
