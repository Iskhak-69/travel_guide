package alatoo.travel_guide.services;

public interface EmailService {
    void sendVerificationEmail(String email, String token);
}
