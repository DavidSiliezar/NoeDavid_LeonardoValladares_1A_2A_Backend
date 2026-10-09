package SystemITR.JosueGuinea1A.Cuentas.Entity;

import SystemITR.JosueGuinea1A.Usuarios.Entity.usuarioEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
public class cuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private Long idCuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private usuarioEntity usuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "saldo_inicial", nullable = false, precision = 12, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "activa", nullable = false, length = 1, columnDefinition = "CHAR(1)")
    private String activa;

    @PrePersist
    public void prePersist() {
        if (this.saldoInicial == null) {
            this.saldoInicial = BigDecimal.ZERO;
        }
        if (this.activa == null || this.activa.isBlank()) {
            this.activa = "S";
        } else {
            this.activa = this.activa.trim().toUpperCase();
        }
        if (this.tipo != null) {
            this.tipo = this.tipo.trim().toUpperCase();
        }
    }

    public Long getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Long idCuenta) {
        this.idCuenta = idCuenta;
    }

    public usuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(usuarioEntity usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }
}
