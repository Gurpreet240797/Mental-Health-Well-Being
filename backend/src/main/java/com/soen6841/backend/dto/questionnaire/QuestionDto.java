package com.soen6841.backend.dto.questionnaire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDto {
    @JsonProperty("qid")
    private String id;
    @JsonProperty("des")
    private String questionString;
    @JsonProperty("options")
    private List<QuestionOptionDto> options;
}
