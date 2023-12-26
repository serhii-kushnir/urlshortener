package ua.shortener.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    private final static String JWT_SECRET_KEY = "QweGFDNHGFYrgbUhgGFcghgey134e589RDkbjYRSxcBBVHHGfHCHCGhcBVB";
    private final static int JWT_LIFETIME = 3_600_000;
    @Override
    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put("roles", role);

        Date issueDate = new Date();
        Date expiredDate = new Date(issueDate.getTime() + JWT_LIFETIME);

        return Jwts
                .builder()
                .subject(userDetails.getUsername())
                .issuedAt(issueDate)
                .expiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUserName(token);
        Date expirationDate = extractAllClaims(token).getExpiration();
        return (username.equals(userDetails.getUsername())) && (new Date().before(expirationDate));
    }

    private Claims extractAllClaims(String token){
        return Jwts.
                parser()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<String> getRole(String token){
        return extractAllClaims(token).get("roles", List.class);
    }
}
