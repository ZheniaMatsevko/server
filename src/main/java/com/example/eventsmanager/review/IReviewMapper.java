package com.example.eventsmanager.review;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IReviewMapper {
    IReviewMapper INSTANCE = Mappers.getMapper(IReviewMapper.class);
    ReviewDto entityToDto(ReviewEntity reviewEntity);
    ReviewEntity dtoToEntity(ReviewDto reviewDto);
    ReviewRequestDto dtoToRequestDto(ReviewDto reviewDto);
    ReviewDto requestDtoToDto(ReviewRequestDto reviewRequestDto);
}
