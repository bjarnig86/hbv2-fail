package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DaycareWorkerRepository extends JpaRepository <DaycareWorker, Long> {
    DaycareWorker save(DaycareWorker daycareWorker);
    DaycareWorker findDaycareWorkerById(Long id);
    List<DaycareWorker> findByLocation(String location);
    List<DaycareWorker> findByLocationCode(int locationCode);
    List<DaycareWorker> findAll();
    void delete(DaycareWorker daycareWorker);
}
