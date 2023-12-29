package ua.shortener.security.auth.dto.registration;

import lombok.Builder;
import lombok.Data;
import ua.shortener.security.auth.dto.login.ResponseLoginError;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class JwtRegistrationResponse {
    private int status;
    private Map<ResponseRegisterError, String> errorMap;
//    private List<ResponseRegisterError> errors;
//    private List<String> messages;
}
