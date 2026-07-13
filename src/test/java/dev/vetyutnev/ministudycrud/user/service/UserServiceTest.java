package dev.vetyutnev.ministudycrud.user.service;

import dev.vetyutnev.ministudycrud.common.exception.UserNotFoundException;
import dev.vetyutnev.ministudycrud.user.domain.User;
import dev.vetyutnev.ministudycrud.user.mapper.UserMapper;
import dev.vetyutnev.ministudycrud.user.repository.UserEntity;
import dev.vetyutnev.ministudycrud.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USER_NAME = "user";
    private static final int USER_AGE = 21;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser_Success() {

        UserEntity userEntity = UserEntity.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .age(USER_AGE)
                .build();

        User expectedUser = new User(USER_ID, USER_NAME, USER_AGE);

        Mockito.when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.toDomain(userEntity)).thenReturn(expectedUser);

        User actualUser = userService.getUser(USER_ID);

        Assertions.assertThat(actualUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);

        Mockito.verify(userRepository).findById(USER_ID);
    }

    @Test
    void getUser_Fail() {

        Long id = 1L;

        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.getUser(id))
                .isInstanceOf(UserNotFoundException.class);

        Mockito.verify(userRepository).findById(id);
        Mockito.verifyNoInteractions(userMapper);
    }

    @Test
    void createUser_Success() {

        UserEntity userEntity = UserEntity.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .age(USER_AGE)
                .build();

        User userDomain = new User(USER_ID, USER_NAME, USER_AGE);

        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);
        Mockito.when(userMapper.toDomain(userEntity)).thenReturn(userDomain);
        Mockito.when(userMapper.toEntity(userDomain)).thenReturn(userEntity);

        User actualUser = userService.createUser(userDomain);

        Assertions.assertThat(actualUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDomain);

        Mockito.verify(userRepository).save(userEntity);
    }

    @Test
    void createUser_Fail(){

        UserEntity userEntity = UserEntity.builder()
                .id(USER_ID)
                .name(USER_NAME)
                .age(USER_AGE)
                .build();

        User userDomain = new User(USER_ID, USER_NAME, USER_AGE);

        Mockito.when(userMapper.toEntity(userDomain)).thenReturn(userEntity);
        Mockito.when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException("database error"));

        Assertions.assertThatThrownBy(() -> userService.createUser(userDomain))
                .isInstanceOf(DataIntegrityViolationException.class);

        Mockito.verify(userRepository).save(userEntity);
        Mockito.verifyNoMoreInteractions(userMapper);
    }

    @Test
    void deleteUser_Success() {
        Mockito.doNothing().when(userRepository).deleteById(USER_ID);

        userService.deleteUser(USER_ID);

        Mockito.verify(userRepository, Mockito.times(1)).deleteById(USER_ID);
    }

    @Test
    void deleteUser_Fail_WhenDatabaseThrowsException() {
        Mockito.doThrow(new EmptyResultDataAccessException(1))
                .when(userRepository).deleteById(USER_ID);

        Assertions.assertThatThrownBy(() -> userService.deleteUser(USER_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);

        Mockito.verify(userRepository).deleteById(USER_ID);
    }
}