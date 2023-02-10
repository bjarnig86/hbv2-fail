package is.hi.hbv501g.hbv1.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.hbv501g.hbv1.Persistence.DTOs.AlertDTO;
import is.hi.hbv501g.hbv1.Persistence.DTOs.ApplicationDTO;
import is.hi.hbv501g.hbv1.Persistence.DTOs.DayReportDTO;
import is.hi.hbv501g.hbv1.Persistence.DTOs.DaycareWorkerDTO;
import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Persistence.Simpletons.SimpleDCW;
import is.hi.hbv501g.hbv1.Services.ChildService;
import is.hi.hbv501g.hbv1.Services.DayReportService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import is.hi.hbv501g.hbv1.Services.DaycareWorkerService;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Controller for Daycareworker logic.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class DaycareWorkerController {

    @Autowired
    private DaycareWorkerService daycareWorkerService;

    @Autowired
    private ChildService childService;

    @Autowired
    private DayReportService dayReportService;

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.auth0.client-secret}")
    private String clientSecret;

    @Value("${TOKEN}")
    private String token;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GET on/daycareworkers
     *
     * @param locationCode the chosen location
     * @return List of Daycare Workers for the chosen location if location is
     *         provided, otherwise all Daycare Workers
     */
    @GetMapping("/daycareworkers")
    public ResponseEntity<List<SimpleDCW>> getAllDaycareWorkers(@RequestParam(required = false) String locationCode) {
        List<SimpleDCW> dcws = new ArrayList<>();
        try {

            if (locationCode == null)
                daycareWorkerService.findAll().forEach(dcw -> {
                    SimpleDCW simple = SimpleDCW.createSimpleDCW(dcw);
                    dcws.add(simple);
                });
            else
                daycareWorkerService.findByLocationCode(locationCode).forEach(dcw -> {
                    SimpleDCW simple = SimpleDCW.createSimpleDCW(dcw);
                    dcws.add(simple);
                });

            if (dcws.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(dcws, HttpStatus.OK);
    }

    /**
     * GET on /daycareworkers/{id}
     *
     * @param id daycareworker id
     * @return daycareworker
     */
    @GetMapping("/daycareworkers/{id}")
    public ResponseEntity<DaycareWorker> getDaycareWorkerByID(@PathVariable("id") String id) {
        DaycareWorker dcw;
        try {
            Long idAsLong = Long.parseLong(id);
            dcw = daycareWorkerService.findDaycareWorkerById(idAsLong);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(dcw, HttpStatus.OK);
    }

    /**
     * GET on /daycareworkerexists/{id}
     *
     * @param ssn daycareworker ssn
     * @return boolean
     */
    @GetMapping("/daycareworkerexists/{ssn}")
    public boolean daycareworkerexists(@PathVariable("ssn") String ssn) {
        boolean dcw;
        try {
            dcw = daycareWorkerService.findDaycareWorkerBySsn(ssn);
            if (dcw) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * POST on /adddaycareworker
     *
     * @param daycareWorkerDTO data transfer object daycareWorkerDTO from the
     *                         request body
     * @return daycareworker added to database
     * @throws IOException
     */
    @PostMapping("/adddaycareworker")
    public ResponseEntity<DaycareWorker> addDaycareWorker(@RequestBody DaycareWorkerDTO daycareWorkerDTO)
            throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        String bodyForTokenCall = "grant_type=client_credentials" +
                "&client_id=lnItWvExMuRghIaYbN8dRUj7wfiSIjvU" +
                "&client_secret=eoJjUmSMCOo4d5CVh4eLs33nanTNqlXwwwx3XWDUPK29odhT0DJH-b6gaHjE3mHR" +
                "&audience=https://dev-xzuj3qsd.eu.auth0.com/api/v2/";

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> tokenEntity = new HttpEntity<>(bodyForTokenCall, tokenHeaders);
        JSONObject newToken;
        try {
            newToken = restTemplate.postForObject("https://dev-xzuj3qsd.eu.auth0.com/oauth/token", tokenEntity, JSONObject.class);
//            System.out.println("NEW TOKEN!!" + newToken);
        } catch (Exception e) {
//            System.out.println("HALLÓ TOKEN FAIL!! \n" + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(newToken.getAsString("access_token"));

        // Signup logic for auth0
        JSONObject json = new JSONObject();
        json.put("email", daycareWorkerDTO.getEmail());
        json.put("password", daycareWorkerDTO.getPassword());
//        json.put("client_id", clientId);
        json.put("connection", "Username-Password-Authentication");

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        String result = "";
        try {
            result = restTemplate.postForObject("https://dev-xzuj3qsd.eu.auth0.com/api/v2/users", entity,
                    String.class);
        } catch (HttpClientErrorException err) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        JsonNode root = objectMapper.readTree(result);

        String email = root.path("email").asText();
        String id = root.path("user_id").asText();

        // Here we assign the role DCW to the new account on auth0.
        // This has to be done after the user is created.
        JSONObject roleJson = new JSONObject();
        List<String> roleArray = new ArrayList<>();
        roleArray.add(daycareWorkerDTO.getROLE());
        roleJson.put("roles", roleArray);

        HttpEntity<String> roleEntity = new HttpEntity<>(roleJson.toString(), headers);

        try {
            restTemplate.postForObject("https://dev-xzuj3qsd.eu.auth0.com/api/v2/users/" + id + "/roles",
                    roleEntity, String.class);
        } catch (HttpClientErrorException err) {
            System.out.println(err);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        // After all the auth0 logic we finally construct the daycareworker and add it
        // to out Database
        DaycareWorker daycareWorker = new DaycareWorker(
                daycareWorkerDTO.getSsn(),
                daycareWorkerDTO.getFirstName(),
                daycareWorkerDTO.getLastName(),
                daycareWorkerDTO.getMobile(),
                daycareWorkerDTO.getEmail(),
                id,
                daycareWorkerDTO.getExperienceInYears(),
                daycareWorkerDTO.getAddress(),
                daycareWorkerDTO.getLocation(),
                daycareWorkerDTO.getLocationCode());

        try {
            daycareWorkerService.addDaycareWorker(daycareWorker);

            if (daycareWorker == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(daycareWorker, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST on /createdayreport
     *
     * @param dayReportDTO the dayreport being created
     * @return the dayreport
     */
    @PostMapping("/createdayreport")
    public ResponseEntity<DayReport> createDayReport(@RequestBody DayReportDTO dayReportDTO) {
        try {
            DaycareWorker dcw = daycareWorkerService.findDaycareWorkerById(dayReportDTO.getDcwId());
            Child c = childService.findChildById(dayReportDTO.getChildId());

            DayReport dayReport = new DayReport(dayReportDTO.getSleepFrom(), dayReportDTO.getSleepTo(),
                    dayReportDTO.getAppetite(), dayReportDTO.getComment(), dcw, c);

            daycareWorkerService.createDayReport(dayReport);

            if (dayReport == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(dayReport, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * POST on /createAlert
     *
     * @param alertDTO the alert being created
     * @return the alert
     */
    @PostMapping("/createAlert")
    public ResponseEntity<Alert> createAlert(@RequestBody AlertDTO alertDTO) {
        try {
            DaycareWorker dcw = daycareWorkerService.findDaycareWorkerById(alertDTO.getDcwId());
            Child c = childService.findChildById(alertDTO.getChildId());

            Alert alert = new Alert(alertDTO.getTimestamp(), alertDTO.getSeverity(), alertDTO.getDescription(), dcw, c);

            daycareWorkerService.createAlert(alert);

            if (alert == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(alert, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String create_token() {
        String input = clientId + ":" + clientSecret;
        String encoded = Base64.getEncoder().encodeToString(input.getBytes());
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }

    /**
     * POST on /daycareworker/apply
     * @param applicationDTO data transfer object applicationDTO from the request body
     * @param principal currently logged-in user
     * @return an array of application information
     * @throws IOException
     */
    @PostMapping("/daycareworker/apply")
    public ResponseEntity<Application> applyForDaycareWorker(@RequestBody ApplicationDTO applicationDTO,
                                        @AuthenticationPrincipal OidcUser principal) throws IOException {
        DaycareWorker dcw = daycareWorkerService.findDaycareWorkerById(applicationDTO.getDaycareWorkerId());

        ArrayList<Application> arr = new ArrayList<>();
        try {

            Child c = childService.findChildById(applicationDTO.getChildId());

            if (c.getDaycareWorker() != null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (dcw.getChildrenCount() >= dcw.getMAXCHILDREN()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            Application appl = new Application(applicationDTO.getDaycareWorkerId(), applicationDTO.getParentId(), applicationDTO.getChildId());
            daycareWorkerService.applyForDaycareWorker(appl);

            dcw.addChildToList(c);
            daycareWorkerService.addDaycareWorker(dcw);

            c.setDaycareWorker(dcw);
            childService.save(c);

            if (appl == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(appl, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/daycareworker/removechild")
    public ResponseEntity removeChildFromDaycareWorker(@RequestBody String childId,
                                                             @AuthenticationPrincipal OidcUser principal) throws IOException {
        try {
            Child child = childService.findChildById(Long.parseLong(childId));
            DaycareWorker dcw = child.getDaycareWorker();
            List<DayReport> dayReportList = childService.getAllDayReportsByChild(child);

            for (DayReport d: dayReportList) {
                dayReportService.delete(d);
            }

            dcw.removeChildFromList(child);

            child.setDaycareWorker(null);

            daycareWorkerService.addDaycareWorker(dcw);
            childService.save(child);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
