package is.hi.hbv501g.hbv1.Security;

import org.springframework.stereotype.Component;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import com.auth0.jwt.JWT;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.AuthorityUtils;
import is.hi.hbv501g.hbv1.Persistence.Entities.DaycareWorker;

import java.io.Serializable;
import java.util.List;

@Component
public class JWTUtils implements Serializable {
    private final String SECRET = "oo323kjok2jerlkaldkjp98q2t4pulakfjlkdjhioa3ry223rfweak";

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 600000000;

    public String generateToken(DaycareWorker dcw) {
        long userID = dcw.getId();
        Algorithm hash = Algorithm.HMAC512(SECRET);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                // TODO: Baeta vid user authority fra roles
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = JWT.create().withIssuer("theBoys").withClaim("sub", userID)
                .withClaim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .sign(hash);
        return token;
    }
}