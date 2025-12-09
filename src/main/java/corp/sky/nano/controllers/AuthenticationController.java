package corp.sky.nano.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import corp.sky.nano.security.LoginRequest;
import corp.sky.nano.security.LoginResponse;

@Controller
public class AuthenticationController {
    private PasswordEncoder encoder;

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> loginAPI(@RequestBody LoginRequest request) {
        return new ResponseEntity<LoginResponse>(
                new LoginResponse(request.getUsername(), encoder.encode(request.getPassword())), null, 200);
    }
}
