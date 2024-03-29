package ua.shortener.user.dto;

import ua.shortener.user.User;

import lombok.Data;

@Data
public final class EditUserDTO {

    private String name;
    private String password;
    private String email;

    public void editUser(final User user) {
        user.setName(this.name);
        user.setPassword(this.password);
        user.setEmail(this.email);
    }
}
