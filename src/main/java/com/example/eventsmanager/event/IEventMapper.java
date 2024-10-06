package com.example.eventsmanager.event;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IEventMapper {
    IEventMapper INSTANCE = Mappers.getMapper(IEventMapper.class);
    EventDto entityToDto(EventEntity eventEntity);
    EventEntity dtoToEntity(EventDto eventDto);
    EventRequestDto dtoToRequestDto(EventDto eventDto);
    EventDto requestDtoToDto(EventRequestDto eventRequestDto);
}
