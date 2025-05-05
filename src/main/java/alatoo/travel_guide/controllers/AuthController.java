package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.JwtResponse;
import alatoo.travel_guide.dto.SignupDto;
import alatoo.travel_guide.dto.UserDto;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.exceptions.ApiException;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.security.JwtTokenUtil;
import alatoo.travel_guide.services.EmailService;
import alatoo.travel_guide.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
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

import java.util.Optional;

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
    private final EmailService emailService;

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
        userRepository.save(user);

        String token = jwtTokenUtil.generateVerificationToken(dto.getEmail());
        emailService.sendVerificationEmail(dto.getEmail(), token);

        return ResponseEntity.ok("User registered successfully. Please verify your email.");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SignupDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Please verify your email before logging in.");
        }

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

    @GetMapping("/user/id-by-email")
    public ResponseEntity<?> getUserIdByEmail(@RequestParam String email) {
        log.info("Fetching user ID for email: {}", email);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        return userOptional
                .map(user -> ResponseEntity.ok(user.getId()))
                .orElseGet(() -> {
                    log.warn("User not found for email: {}", email);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Long.valueOf("User not found"));
                });
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            log.info("Verifying token: {}", token);
            String email = jwtTokenUtil.getEmailFromToken(token);

            if (email == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: email not found.");
            }

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApiException("Verification token expired or invalid email", HttpStatus.NOT_FOUND));

            if (user.isVerified()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already verified.");
            }

            user.setVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully.");
        } catch (ExpiredJwtException e) {
            log.warn("Token expired", e);
            String email = jwtTokenUtil.getEmailFromExpiredToken(token);
            userRepository.findByEmail(email).ifPresent(userRepository::delete);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired. Please sign up again.");
        } catch (Exception e) {
            log.error("Error during email verification", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }
    }
}
