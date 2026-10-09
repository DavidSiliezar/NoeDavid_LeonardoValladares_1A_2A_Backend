package SystemITR.JosueGuinea1A.Cuentas.Entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;

@Entity
@Table(name= "cuentas")
public class cuentaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuenta")
    private long id_cuenta;


}
