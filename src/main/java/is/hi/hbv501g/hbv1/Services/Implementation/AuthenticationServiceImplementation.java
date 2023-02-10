package is.hi.hbv501g.hbv1.Services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;
import is.hi.hbv501g.hbv1.Security.JWTUtils;
import is.hi.hbv501g.hbv1.Services.AuthenticationService;

@Service
public class AuthenticationServiceImplementation implements AuthenticationService {
    private final JWTUtils jwtUtils;
    @Autowired
    public AuthenticationServiceImplementation(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    public String getTypeOfUser(Long id) {
        return "DCW";
    }
    public String getTypeOfUser(String email) {
        return "DCW";
    }
    @Override
    public String getTokenForDCW(DaycareWorker dcw, String pwd) {
        if (dcw != null && dcw.getPassword().equals(pwd)) {
            String token = jwtUtils.generateToken(dcw);
            return token;
        }
        return null;
    }
    public String signupDCW(DaycareWorker dcw) {
        return "goodshit";
    }

    
}
