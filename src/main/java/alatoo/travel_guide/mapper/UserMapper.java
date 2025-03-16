package alatoo.travel_guide.mapper;

import alatoo.travel_guide.dto.UserDto;
import alatoo.travel_guide.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity userDtoToUser(UserDto dto);
    UserDto userToUserDto(UserEntity user);
}