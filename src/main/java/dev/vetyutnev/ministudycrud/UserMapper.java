package dev.vetyutnev.ministudycrud;

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
