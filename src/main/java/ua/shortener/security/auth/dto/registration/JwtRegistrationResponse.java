package ua.shortener.security.auth.dto.registration;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class JwtRegistrationResponse {
    private int status;
    private Map<ResponseRegisterError, String> errorMap;
}
