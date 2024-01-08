package ua.shortener.security.auth.dto.login;

import lombok.Getter;


@Getter
public enum ResponseLoginError {

    OK("Successfully"),
    BAD_PASSWORD_OR_LOGIN("Wrong password or login"),
    JWT("");

    private final String message;
    ResponseLoginError(final String msg) {
        this.message = msg;
    }

}
