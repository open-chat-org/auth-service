package openchat.authservice.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {

    private String username;
    private String mail;
    private String password;
}
