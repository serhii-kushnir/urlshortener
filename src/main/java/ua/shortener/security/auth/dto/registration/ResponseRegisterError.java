package ua.shortener.security.auth.dto.registration;

import lombok.Getter;

@Getter
public enum ResponseRegisterError {

    OK("Successfully added"),
    BAD_PASSWORD("Password should have 8 or more chars and contains numbers, " +
            "letters in upper case and letters in lower case"),
    EXISTING_EMAIL("User with this email already exist"),
    BAD_EMAIL("Input email isn't correct, please check it"),
    BAD_NAME("Input name should have more than 3 symbols and contains more than 1 letter");

    private final String message;
    ResponseRegisterError(final String message){
        this.message = message;
    }

}
