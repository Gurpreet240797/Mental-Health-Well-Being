package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.questionnaire.QuestionOptionDto;
import com.soen6841.backend.entity.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionOptionMapper {
    QuestionOptionMapper INSTANCE = Mappers.getMapper(QuestionOptionMapper.class);

    QuestionOptionDto map(QuestionOption source);

    List<QuestionOptionDto> map(List<QuestionOption> source);
}
