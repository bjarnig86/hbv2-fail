package is.hi.hbv501g.hbv1.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;
import is.hi.hbv501g.hbv1.Persistence.Entities.LoginForm;
import is.hi.hbv501g.hbv1.Security.JWTUtils;
import is.hi.hbv501g.hbv1.Services.AuthenticationService;
import is.hi.hbv501g.hbv1.Services.DaycareWorkerService;

@RestController
public class AuthenticationController {
    AuthenticationService authenticationService;
    DaycareWorkerService daycareWorkerService;
    JWTUtils jwtUtils;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JWTUtils jwtUtils,
            DaycareWorkerService daycareWorkerService) {
        this.authenticationService = authenticationService;
        this.daycareWorkerService = daycareWorkerService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/api/login")
    public String login(@RequestBody LoginForm loginform, HttpSession session) {
        try {
            System.out.println("hola que ase");
            String type = "";
            String token = "";
            if (loginform.id != null) {
                System.out.println("mariaaaa");
                type = authenticationService.getTypeOfUser(loginform.id);
                if (type == "DCW") {
                    System.out.println("maricarmen");
                    DaycareWorker dcw = daycareWorkerService.findDaycareWorkerById(loginform.id);
                    System.out.println("mariaaaa"+dcw.getFirstName());
                    token = authenticationService.getTokenForDCW(dcw, loginform.password);
                } else if (type == "parent") {
                    // parent token
                } else {
                    token = null;
                }
            } else if (loginform.email != null){
                authenticationService.getTypeOfUser(loginform.email);
                if (type == "DCW") {
                    DaycareWorker dcw = daycareWorkerService.findDaycareWorkerByEmail(loginform.email);
                    token = authenticationService.getTokenForDCW(dcw, loginform.password);
                } else if (type == "parent") {
                    // parent token
                } else {
                    token = null;
                }
            }
            if (token != null) {
                session.setAttribute(token, loginform.id);
                return token;
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password not correct");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password not correct");
        }
    }

    @PostMapping("/api/dcw/signup")
    public String signup(@RequestBody DaycareWorker dcw, HttpSession session, HttpServletRequest request) {
        String token = authenticationService.signupDCW(dcw);
        return token;
    }

}
