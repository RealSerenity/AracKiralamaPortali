package rserenity.business.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rserenity.Exception.ResourceNotFoundException;
import rserenity.business.dto.UserDto;
import rserenity.business.services.UserServices;
import rserenity.data.entity.UserEntity;
import rserenity.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<UserDto> getAll() {
        List<UserDto> userDtos = new ArrayList<>();
        Iterable<UserEntity> entityList= userRepository.findAll();
        for(UserEntity entity : entityList){
            UserDto userDto = entityToDto(entity);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntity = dtoToEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return userDto;
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) throws ResourceNotFoundException{
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User does not exist by given id " + id));
        UserDto userDto = entityToDto(userEntity);
        return ResponseEntity.ok(userDto);
    }

    @Override
    public ResponseEntity<UserDto> updateUserById(Long id, UserDto user) {
        UserDto oldUser = null;
        try {
            oldUser = getUserById(id).getBody();
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if(oldUser == null){
            return null;
        }
        if(!oldUser.getUsername().equals(user.getUsername())){
            if(getUserDtoByName(user.getUsername()) == null){
                oldUser.setUsername(user.getUsername());
                return ResponseEntity.ok(entityToDto(userRepository.save(dtoToEntity(oldUser))));
            }else{
                System.out.println("Username must be unique");
                return null;
            }
        }else if (!passwordEncoder.matches(user.getPassword(),oldUser.getPassword())){
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            return ResponseEntity.ok(entityToDto(userRepository.save(dtoToEntity(oldUser))));
        }
        return null;
    }

    @Override
    public ResponseEntity<Map<String, Boolean>> deleteUser(Long id){
        return null;
    }

    @Override
    public UserEntity getUserEntityByName(String userName) {
        return userRepository.findUserEntityByUsername(userName);
    }

    @Override
    public UserDto getUserDtoByName(String userName) {
        if(getUserEntityByName(userName) == null){
            return null;
        }
        return entityToDto(getUserEntityByName(userName));
    }


    @Override
    public boolean userLogin(UserDto userDto) {
        UserEntity entity=  userRepository.findUserEntityByUsername(userDto.getUsername());

        if(entity != null &&  passwordEncoder.matches(userDto.getPassword(),entity.getPassword())){
            //şifreler eşleştirildi
            return true;
        }else {
            System.out.println("Wrong password !");
            //şifrede sıkıntı var
            return false;
        }
    }

    @Override
    public UserDto entityToDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserEntity dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }
}
