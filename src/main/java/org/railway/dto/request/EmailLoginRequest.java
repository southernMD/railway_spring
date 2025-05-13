package org.railway.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailLoginRequest {
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
