package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.questionnaire.QuestionnaireResultDto;
import com.soen6841.backend.entity.QuestionnaireResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface QuestionnaireResultMapper {
    QuestionnaireResultMapper INSTANCE = Mappers.getMapper(QuestionnaireResultMapper.class);

    QuestionnaireResultDto map(QuestionnaireResult source);

    List<QuestionnaireResultDto> map(List<QuestionnaireResult> source);
}
