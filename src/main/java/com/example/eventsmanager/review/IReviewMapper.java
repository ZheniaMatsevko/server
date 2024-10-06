package com.example.eventsmanager.review;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IReviewMapper {
    IReviewMapper INSTANCE = Mappers.getMapper(IReviewMapper.class);
    ReviewResponseDto entityToResponseDto(ReviewEntity reviewEntity);
    ReviewEntity createRequestDtoToToEntity(CreateReviewRequestDto createReviewRequestDto);
}
