package community.community.service;

import community.community.dto.PageDTO;
import community.community.dto.QuestionDTO;
import community.community.mapper.QuestionMapper;
import community.community.mapper.UserMapper;
import community.community.model.Question;
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
        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.findById(question.getCreator()));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);
        Integer totalCount = questionMapper.count();//总数据数(总问题数)
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public PageDTO listByUserId(Integer userId, Integer page, Integer size) {
        Integer offset=(page-1)*size;
        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList=new ArrayList<>();

        PageDTO pageDTO = new PageDTO();
        for (Question question : questions) {
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(userMapper.findById(question.getCreator()));
            questionDTOList.add(questionDTO);
        }
        pageDTO.setQuestions(questionDTOList);
        Integer totalCount = questionMapper.countByUserId(userId);//总数据数(总问题数)
        pageDTO.setPagination(totalCount,page,size);
        return pageDTO;
    }

    public QuestionDTO getById(Integer id) {
        QuestionDTO questionDTO=new QuestionDTO();
        Question question = questionMapper.getById(id);
        BeanUtils.copyProperties(question,questionDTO);
        questionDTO.setUser(userMapper.findById(question.getCreator()));

        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            //插入
            question.setCreateTime(System.currentTimeMillis());
            question.setModifiedTime(question.getCreateTime());
            questionMapper.create(question);
        }else {
            Question DBQuestion=questionMapper.getById(question.getId());
            DBQuestion.setTitle(question.getTitle());
            DBQuestion.setDescription(question.getDescription());
            DBQuestion.setTag(question.getTag());
            DBQuestion.setModifiedTime(System.currentTimeMillis());
            questionMapper.update(DBQuestion);
        }
    }
}
