package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.JwtResponse;
import alatoo.travel_guide.dto.SignupDto;
import alatoo.travel_guide.dto.UserDto;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.security.JwtTokenUtil;
import alatoo.travel_guide.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trusted/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email already exists: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already in use");
        }

        UserEntity user = new UserEntity();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.generateToken(authentication.getName());

            return ResponseEntity.ok(new JwtResponse(token, user.getEmail()));
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }
}
