package org.railway.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsernameLoginRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
