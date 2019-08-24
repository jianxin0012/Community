package community.community.mapper;

import community.community.model.Question;

import java.util.List;

public interface QuestionExtMapper2 {
    List<Question> selectRelated(Question question);
}
