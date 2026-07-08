package dev.vetyutnev.ministudycrud.user.mapper;

import dev.vetyutnev.ministudycrud.user.api.dto.UserCreateDto;
import dev.vetyutnev.ministudycrud.user.api.dto.UserDto;
import dev.vetyutnev.ministudycrud.user.domain.User;
import dev.vetyutnev.ministudycrud.user.repository.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toDomain(UserCreateDto request);

    User toDomain(UserEntity entity);

    UserDto toDto(User domain);

    UserEntity toEntity(User userDomain);
}
