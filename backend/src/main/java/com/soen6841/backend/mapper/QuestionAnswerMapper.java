package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.questionnaire.QuestionAnswerDto;
import com.soen6841.backend.entity.QuestionAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionAnswerMapper {
    QuestionAnswerMapper INSTANCE = Mappers.getMapper(QuestionAnswerMapper.class);

    QuestionAnswerDto map(QuestionAnswer source);

    List<QuestionAnswerDto> map(List<QuestionAnswer> source);
}
