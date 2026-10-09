package SystemITR.JosueGuinea1A.Cuentas.Repository;

import SystemITR.JosueGuinea1A.Cuentas.Entity.cuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<cuentaEntity, Long> {

    List<cuentaEntity> findByUsuario_IdUsuarioOrderByIdCuentaAsc(Long idUsuario);

    boolean existsByUsuario_IdUsuario(Long idUsuario);
}
