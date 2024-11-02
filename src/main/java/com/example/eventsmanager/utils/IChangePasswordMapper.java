package com.example.eventsmanager.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IChangePasswordMapper {
    IChangePasswordMapper INSTANCE = Mappers.getMapper(IChangePasswordMapper.class);
    ChangePasswordRequestDto dtoToRequestDto(ChangePasswordDto changePasswordDto);
    ChangePasswordDto requestDtoToDto(ChangePasswordRequestDto changePasswordRequestDto);
}
