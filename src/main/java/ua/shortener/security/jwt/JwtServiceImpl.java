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
public final class JwtServiceImpl implements JwtService {

    private final static String JWT_SECRET_KEY = "QweGFDNHGFYrgbUhgGFcghgey134e589RDkbjYRSxcBBVHHGfHCHCGhcBVB";
    private final static int JWT_LIFETIME = 3_600_00000;

    @Override
    public String extractUserName(final String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public String generateToken(final UserDetails userDetails) {
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
    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        String username = extractUserName(token);
        Date expirationDate = extractAllClaims(token).getExpiration();
        return (username.equals(userDetails.getUsername())) && (new Date().before(expirationDate));
    }

    private Claims extractAllClaims(final String token) {
        return Jwts.
                parser()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



    public List<String> getRole(final String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public String generateTokenInvalidToken(final String token) {
        Map<String, Object> claims = new HashMap<>();
        List<String> role = getRole(token);
        String username = extractUserName(token);

        claims.put("roles", role);

        Date issueDate = new Date();

        return Jwts
                .builder()
                .subject(username)
                .issuedAt(issueDate)
                .expiration(issueDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }
}