package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.questionnaire.QuestionDto;
import com.soen6841.backend.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    QuestionDto map(Question source);

    List<QuestionDto> map(List<Question> source);
}
