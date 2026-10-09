package SystemITR.JosueGuinea1A.Movimientos.Repository;

import SystemITR.JosueGuinea1A.Movimientos.Entity.movimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<movimientoEntity, Long> {

    List<movimientoEntity> findByCuenta_IdCuentaOrderByFechaDescIdMovimientoDesc(Long idCuenta);

    List<movimientoEntity> findByCuenta_IdCuenta(Long idCuenta);

    boolean existsByCuenta_IdCuenta(Long idCuenta);

    List<movimientoEntity> findByCuenta_IdCuentaAndTipoIgnoreCaseOrderByFechaDescIdMovimientoDesc(Long idCuenta, String tipo);

    List<movimientoEntity> findByCuenta_IdCuentaAndCategoriaContainingIgnoreCaseOrderByFechaDescIdMovimientoDesc(Long idCuenta, String categoria);

    List<movimientoEntity> findByCuenta_IdCuentaAndFechaBetweenOrderByFechaDescIdMovimientoDesc(Long idCuenta, Date fechaInicio, Date fechaFin);
}
