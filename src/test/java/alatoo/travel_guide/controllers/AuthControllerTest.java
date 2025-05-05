package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.SignupDto;
import alatoo.travel_guide.dto.UserDto;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.security.JwtTokenUtil;
import alatoo.travel_guide.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignupSuccess() throws Exception {
        SignupDto signupDto = SignupDto.builder()
                .email("test@example.com")
                .password("securepassword")
                .username("testuser")
                .build();

        Mockito.when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("hashedPassword");

        mockMvc.perform(post("/trusted/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetUserByIdSuccess() throws Exception {
        Long userId = 1L;
        UserDto mockUser = UserDto.builder()
                .id(userId)
                .email("mock@example.com")
                .password("mockPassword")
                .createdAt(LocalDateTime.now())
                .build();

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUser);

        mockMvc.perform(get("/trusted/auth/user/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("mock@example.com"));
    }
}
