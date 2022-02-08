package openchat.authservice.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignInRequestDto {

    @NotBlank
    private String mail;

    @NotBlank
    private String password;
}
