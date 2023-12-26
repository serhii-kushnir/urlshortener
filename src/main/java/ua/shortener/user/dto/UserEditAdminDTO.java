package ua.shortener.user.dto;

import ua.shortener.user.Role;
import ua.shortener.user.User;
import lombok.Data;

@Data
public class UserEditAdminDTO {
    private Role role;
    private boolean enabled;

    public void editUserAdmin(User user) {
        user.setRole(this.role);
        user.setEnabled(this.enabled);
    }
}
