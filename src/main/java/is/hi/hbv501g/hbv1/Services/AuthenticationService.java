package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;

public interface AuthenticationService {
    String getTypeOfUser(Long id);    
    String getTypeOfUser(String email);
    String getTokenForDCW(DaycareWorker dcw, String password);
    String signupDCW(DaycareWorker dcw);

    
}
