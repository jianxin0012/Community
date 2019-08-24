package community.community.mapper;

import community.community.dto.QuestionQueryDTO;
import community.community.model.Question;

import java.util.List;

public interface QuestionExtMapper2 {
    List<Question> selectRelated(Question question);

    Integer countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}
