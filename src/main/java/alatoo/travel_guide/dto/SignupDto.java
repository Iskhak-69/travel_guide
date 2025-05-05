package alatoo.travel_guide.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupDto {

    @NotEmpty
    @Email
    @NotBlank
    private String email;

    @NotEmpty
    @Size(min = 8)
    private String password;

    @NotBlank
    private String username;
}

