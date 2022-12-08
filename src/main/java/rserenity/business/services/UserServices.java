package rserenity.business.services;

import org.springframework.http.ResponseEntity;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.UserDto;
import rserenity.data.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserServices {

    public List<UserDto> getAll();

    public UserDto createUser(UserDto userDto);

    public ResponseEntity<UserDto> getUserById(Long id) throws ResourceNotFoundException;

    public ResponseEntity<UserDto> updateUserById(Long id,UserDto user);

    public ResponseEntity<Map<String,Boolean>> deleteUser(Long id);

    public UserEntity getUserEntityByName(String userName);

    public UserDto getUserDtoByName(String userName);

    public boolean userLogin(UserDto userDto);

    public UserDto entityToDto(UserEntity userEntity);
    public UserEntity dtoToEntity(UserDto userDto);
}
