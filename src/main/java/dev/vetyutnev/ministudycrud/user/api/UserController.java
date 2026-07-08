package dev.vetyutnev.ministudycrud.user.api;

import dev.vetyutnev.ministudycrud.user.mapper.UserMapper;
import dev.vetyutnev.ministudycrud.user.service.UserService;
import dev.vetyutnev.ministudycrud.user.api.dto.UserCreateDto;
import dev.vetyutnev.ministudycrud.user.api.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDto request){
      log.info("HTTP POST /api/users запрос на создание пользователя {}", request.name());

        var userDomain = userMapper.toDomain(request);

        var createdUserDomain = userService.createUser(userDomain);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDto(createdUserDomain));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id){
        log.info("HTTP GET /api/users запрос на получение пользователя {}", id);

        var userDomain = userService.getUser(id);

        return ResponseEntity.ok(
                userMapper.toDto(userDomain)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        log.info("HTTP DELETE /api/users запрос на удаление пользователя {}", id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
