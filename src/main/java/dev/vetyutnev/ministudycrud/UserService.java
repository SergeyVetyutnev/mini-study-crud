package dev.vetyutnev.ministudycrud;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User createUser(User userDomain) {
        log.info("создание пользователя {}", userDomain.name());

        var userEntity = userMapper.toEntity(userDomain);

        var createdUserEntity = userRepository.save(userEntity);

        return userMapper.toDomain(userEntity);

    }

    public User getUser(Long id) {
        log.info("получение пользователя {}", id);

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "пользователь c id " + id + " не найден"));

        return userMapper.toDomain(userEntity);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
