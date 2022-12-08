package rserenity.business.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
