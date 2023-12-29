package ua.shortener.security.auth.dto.registration;

public enum ResponseRegisterError {

    OK("Successfully added"),
    BAD_PASSWORD("Password should have 8 or more chars and contains numbers, " +
            "letters in upper case and letters in lower case"),
    BAD_EMAIL("User with this email already exist");

    private final String message;
    ResponseRegisterError(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
