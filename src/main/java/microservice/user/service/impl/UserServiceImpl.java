package microservice.user.service.impl;

import lombok.RequiredArgsConstructor;
import microservice.user.dto.SignUpDTO;
import microservice.user.dto.UserDTO;
import microservice.user.entity.PhoneEntity;
import microservice.user.entity.UserEntity;
import microservice.user.exeption.SignUpException;
import microservice.user.exeption.UserNotFoundException;
import microservice.user.repository.UserRepository;
import microservice.user.service.SecurityService;
import microservice.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final SecurityService securityService;
  private final PasswordEncoder passwordEncoder;
  private final ModelMapper modelMapper;

  @Override
  public UserDTO signUp(SignUpDTO signUpDTO) {
    Optional<UserEntity> userEntityOptional = userRepository
        .findByEmail(signUpDTO.getEmail());
    if (userEntityOptional.isPresent()) {
      throw new SignUpException("User already exists");
    }
    UserEntity userEntity = userDtoToUserEntityMapper(signUpDTO);
    userRepository.save(userEntity);
    return userEntityToUserDTOMapper(userEntity);
  }

  @Override
  public UserDTO getUserByEmail(String email) throws SignUpException {
    Optional<UserEntity> user = userRepository.getByEmail(email);
    if (user.isEmpty()) {
      throw new UserNotFoundException("Invalid email");
    }
    return userEntityToUserDTOMapper(user.get());
  }

  private UserEntity userDtoToUserEntityMapper(SignUpDTO signUpDTO) {
    UserEntity userEntity = modelMapper.map(signUpDTO, UserEntity.class);
    userEntity.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
    userEntity.setCreated(LocalDateTime.now());
    userEntity.setLastLogin(LocalDateTime.now());
    userEntity.setActive(true);

    if (signUpDTO.getPhones() != null) {
      List<PhoneEntity> phoneEntities = signUpDTO.getPhones().stream()
          .map(phoneDto -> {
            PhoneEntity phoneEntity = modelMapper.map(phoneDto, PhoneEntity.class);
            phoneEntity.setUser(userEntity);
            return phoneEntity;
          })
          .collect(Collectors.toList());
      userEntity.setPhones(phoneEntities);
    }
    return userEntity;
  }

  private UserDTO userEntityToUserDTOMapper(UserEntity userEntity) {
    UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
    userDTO.setToken(securityService.generateToken(userEntity.getEmail()));
    return userDTO;
  }

}
