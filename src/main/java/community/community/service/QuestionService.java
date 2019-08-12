package community.community.service;

import community.community.dto.PageDTO;
import community.community.dto.QuestionDTO;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import community.community.mapper.QuestionMapper;
import community.community.mapper.UserMapper;
import community.community.model.Question;
import community.community.model.QuestionExample;
import community.community.model.User;
import community.community.model.UserExample;
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

    public PageDTO list(Integer page, Integer size) {
        //limit偏移量
        Integer offset=(page-1)*size;
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(),new RowBounds(offset,size));
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);

        QuestionExample example = new QuestionExample();
        long l = questionMapper.countByExample(example);
        Integer totalCount = (int)l;//总数据数(总问题数)
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public PageDTO listByUserId(Integer userId, Integer page, Integer size) {
        Integer offset=(page-1)*size;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));

        List<QuestionDTO> questionDTOList=new ArrayList<>();

        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(example);//总数据数(总问题数)
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        QuestionDTO questionDTO=new QuestionDTO();
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomException(CustomExceptionEnum.QUESTION_NOT_FOUND);
        }
        BeanUtils.copyProperties(question,questionDTO);
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        questionDTO.setUser(users.get(0));

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //插入
            question.setCreateTime(System.currentTimeMillis());
            question.setModifiedTime(question.getCreateTime());
            questionMapper.insert(question);
        }else {
            Question updateQuestion=new Question();
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setModifiedTime(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int update=questionMapper.updateByExampleSelective(updateQuestion, example);
            if (update!=1){
                throw new CustomException(CustomExceptionEnum.QUESTION_NOT_FOUND);
            }
        }
    }
}
