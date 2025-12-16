package corp.sky.nano.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import corp.sky.nano.security.AuthResponse;
import corp.sky.nano.security.JwtUtil;
import corp.sky.nano.security.LoginRequest;
import corp.sky.nano.security.RegisterRequest;
import corp.sky.nano.user.UserEntity;
import corp.sky.nano.user.UserRepository;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository repo;
    @Autowired
    private JwtUtil util;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/login")
    public ResponseEntity<AuthResponse> loginAPI(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = (UserDetails) auth.getPrincipal();

        String token = util.generateToken(user.getUsername());
        return new ResponseEntity<AuthResponse>(
                new AuthResponse(request.getUsername(), token), HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/register")
    public ResponseEntity<AuthResponse> registerAPI(@Valid @RequestBody RegisterRequest request) {
        UserEntity user = new UserEntity();
        user.password = encoder.encode(request.getPassword());
        user.username = request.getUsername();
        user.role = "USER";
        repo.save(user);
        String token = util.generateToken(user.username);
        return new ResponseEntity<AuthResponse>(new AuthResponse(request.getUsername(), token), HttpStatus.CREATED);
    }
}
