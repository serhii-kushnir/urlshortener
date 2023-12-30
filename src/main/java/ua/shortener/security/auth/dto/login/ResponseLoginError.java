package ua.shortener.security.auth.dto.login;

public enum ResponseLoginError {
    OK("Successfully"),
    BAD_PASSWORD_OR_LOGIN("Wrong password or login"),
    JWT("");

    private final String message;
    ResponseLoginError(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
