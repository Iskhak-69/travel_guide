package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.*;
import alatoo.travel_guide.entities.RefreshToken;
import alatoo.travel_guide.entities.UserEntity;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.security.JwtTokenUtil;
import alatoo.travel_guide.services.RefreshTokenService;
import alatoo.travel_guide.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "User sign-up", description = "Registers a new user with email, password, and username")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "409", description = "Email already in use")
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
    @Operation(summary = "User login", description = "Login with username and password")
    @ApiResponse(responseCode = "200", description = "Returns JWT access and refresh tokens")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        UserEntity user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenUtil.generateToken(authentication.getName());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

            return ResponseEntity.ok(new JwtResponse(token, user.getEmail(), refreshToken.getToken()));
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @Operation(summary = "Get user by ID", description = "Returns the user's data by ID (JWT required)")
    @ApiResponse(responseCode = "200", description = "Returns UserDto object")
    @ApiResponse(responseCode = "403", description = "Forbidden, JWT missing or invalid")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @Operation(summary = "Refresh JWT token", description = "Uses refresh token to generate a new access token")
    @ApiResponse(responseCode = "200", description = "Returns new JWT token")
    @ApiResponse(responseCode = "400", description = "Invalid or expired refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        refreshTokenService.verifyExpiration(refreshToken);

        String newToken = jwtTokenUtil.generateToken(refreshToken.getUser().getUsername());
        return ResponseEntity.ok(
                new JwtResponse(newToken, refreshToken.getUser().getEmail(), refreshToken.getToken())
        );
    }

}
