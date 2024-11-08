package com.example.eventsmanager.event.address;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IAddressMapper {
    IAddressMapper INSTANCE = Mappers.getMapper(IAddressMapper.class);

    AddressDto entityToDto(AddressEntity addressEntity);

    AddressEntity dtoToEntity(AddressDto addressDto);
}
