package alatoo.travel_guide.controllers;

import alatoo.travel_guide.dto.SignupDto;
import alatoo.travel_guide.repositories.UserRepository;
import alatoo.travel_guide.security.JwtTokenUtil;
import alatoo.travel_guide.services.EmailService;
import alatoo.travel_guide.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private EmailService emailService;

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
        Mockito.when(jwtTokenUtil.generateVerificationToken(signupDto.getEmail())).thenReturn("dummyToken");

        mockMvc.perform(post("/trusted/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully. Please verify your email."));
    }
}
