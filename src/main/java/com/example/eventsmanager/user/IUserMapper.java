package com.example.eventsmanager.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    UserDto entityToDto(UserEntity userEntity);

    UserEntity dtoToEntity(UserDto userDto);

    UserRequestDto dtoToRequestDto(UserDto userDto);

    UserDto requestDtoToDto(UserRequestDto userRequestDto);
}
