package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;

import java.util.List;

public interface DaycareWorkerService {
    List<DaycareWorker> findByLocation(String location);
    List<DaycareWorker> findByLocationCode(String locationCode);
    DaycareWorker findDaycareWorkerById(Long id);
    DaycareWorker findDaycareWorkerByEmail(String email);
    List<DaycareWorker> findAll();
    DaycareWorker addDaycareWorker(DaycareWorker daycareWorker);
    void delete(DaycareWorker daycareWorker);
}


