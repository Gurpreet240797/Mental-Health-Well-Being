package com.soen6841.backend.mapper;

import com.soen6841.backend.dto.questionnaire.QuestionAnswerDetailDto;
import com.soen6841.backend.entity.Question;
import com.soen6841.backend.entity.QuestionAnswer;
import com.soen6841.backend.entity.QuestionOption;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper
public interface QuestionAnswerDetailMapper {

    QuestionAnswerDetailMapper INSTANCE = Mappers.getMapper(QuestionAnswerDetailMapper.class);

    default List<QuestionAnswerDetailDto> map(List<QuestionAnswer> source, List<Question> questions) {
        if ( source != null ) {
            List<QuestionAnswerDetailDto> list = new ArrayList<>( source.size() );
            for ( QuestionAnswer questionAnswer : source ) {
                Optional<Question> questionOptional = questions.stream().filter(q -> Objects.equals(q.getId(), questionAnswer.getQuestionId())).findFirst();
                if ( questionOptional.isPresent() ) {
                    QuestionAnswerDetailDto dto = new QuestionAnswerDetailDto();
                    Question question = questionOptional.get();
                    dto.setQuestionDescription(question.getQuestionString());
                    question.getOptions().stream().filter(o -> Objects.equals(o.getScore(), questionAnswer.getScore())).findFirst().ifPresent(o -> dto.setAnswer(o.getOptionString()));
                    list.add(dto);
                }
            }
            return list;
        }
        return null;
    }

}
