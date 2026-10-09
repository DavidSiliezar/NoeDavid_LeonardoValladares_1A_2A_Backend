package SystemITR.JosueGuinea1A.Usuarios.Entity;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "usuarios")
public class usuarioEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_usuario")
    private long idUsuario;

    @Column(name = "nombre" , nullable = false , length = 100)
    private String nombre;

    @Column(name = "correo" , nullable = false , length = 150)
    private String correo;

    @Column(name = "fecha_registro" , nullable = false , columnDefinition = "SYSDATE")
    private Date fechaRegistro;

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
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
}
