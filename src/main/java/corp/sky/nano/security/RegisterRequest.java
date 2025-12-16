package corp.sky.nano.security;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
    @NotNull(message = "Please input username")
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Please input password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
