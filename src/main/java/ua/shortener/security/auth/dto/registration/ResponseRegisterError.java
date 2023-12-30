package ua.shortener.security.auth.dto.registration;

public enum ResponseRegisterError {

    OK("Successfully added"),
    BAD_PASSWORD("Password should have 8 or more chars and contains numbers, " +
            "letters in upper case and letters in lower case"),
    BAD_EMAIL("User with this email already exist"),
    INCORRECT_EMAIL("Input email isn't correct, please check it"),
    BAD_NAME("Input name should have more than 3 symbols and contains more than 1 letter");

    private final String message;
    ResponseRegisterError(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
