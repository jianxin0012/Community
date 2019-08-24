package community.community.service;

import community.community.dto.PageDTO;
import community.community.dto.QuestionDTO;
import community.community.dto.QuestionQueryDTO;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import community.community.mapper.QuestionExtMapper;
import community.community.mapper.QuestionExtMapper2;
import community.community.mapper.QuestionMapper;
import community.community.mapper.UserMapper;
import community.community.model.Question;
import community.community.model.QuestionExample;
import community.community.model.User;
import community.community.model.UserExample;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private QuestionExtMapper2 questionExtMapper2;

    public PageDTO list(String search, Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)){
            String tag = StringUtils.replace(search, " ", "|");
        }

        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper2.countBySearch(questionQueryDTO);//总数据数(总问题数)
        pageDTO.setPagination(totalCount, page, size);
        //limit偏移量
        Integer offset = (page - 1) * size;
        questionQueryDTO.setOffset(offset);
        questionQueryDTO.setSize(size);
        List<Question> questions = questionExtMapper2.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);



        return pageDTO;
    }

    public PageDTO listByUserId(Long userId, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();

        PageDTO<QuestionDTO> pageDTO = new PageDTO<>();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setData(questionDTOList);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);//总数据数(总问题数)
        pageDTO.setPagination(totalCount, page, size);
        return pageDTO;
    }

    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomException(CustomExceptionEnum.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question, questionDTO);
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        questionDTO.setUser(users.get(0));

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //插入
            question.setCreateTime(System.currentTimeMillis());
            question.setModifiedTime(question.getCreateTime());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            Question updateQuestion = new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setModifiedTime(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update != 1) {
                throw new CustomException(CustomExceptionEnum.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        //多个人访问出现问题
//        Question question = questionMapper.selectByPrimaryKey(id);
//        Question updateQuestion = new Question();
//        updateQuestion.setViewCount(question.getViewCount() + 1);
//        QuestionExample example = new QuestionExample();
//        example.createCriteria().andIdEqualTo(id);
//        questionMapper.updateByExampleSelective(updateQuestion, example);
          questionExtMapper.incView(id);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO questionDTO) {
        if (StringUtils.isBlank(questionDTO.getTag())){
            return new ArrayList<>();
        }
        String tag = StringUtils.replace(questionDTO.getTag(), ",", "|");
        Question question=new Question();
        question.setId(questionDTO.getId());
        question.setTag(tag);
        List<Question> relatedQuestions = questionExtMapper2.selectRelated(question);
        List<QuestionDTO> relatedQuestionDTO=new ArrayList<>();
        for (Question relatedQuestion : relatedQuestions) {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(relatedQuestion,questionDTO1);
            questionDTO1.setUser(questionDTO.getUser());
            relatedQuestionDTO.add(questionDTO1);
        }
        return relatedQuestionDTO;
    }
}
