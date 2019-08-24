package community.community.service;

import community.community.dto.CommentDTO;
import community.community.enums.CommentTypeEnum;
import community.community.enums.NotificationStatusEnum;
import community.community.enums.NotificationTypeEnum;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import community.community.mapper.*;
import community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomException(CustomExceptionEnum.TARGET_NOT_FOUND);
        }
        if (comment.getType()==null || !CommentTypeEnum.contain(comment.getType())){
            throw new CustomException(CustomExceptionEnum.TYPE_WRONG);
        }
        if (comment.getType()== CommentTypeEnum.QUESTION.getType()){
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question==null){
                throw new CustomException(CustomExceptionEnum.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0L);
            comment.setLikeCount(0L);
            commentMapper.insert(comment);
            questionExtMapper.incComment(question.getId());
            //创建通知
            createNotify(comment,question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
        }
        if (comment.getType()== CommentTypeEnum.COMMENT.getType()){
//            回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment==null){
                throw new CustomException(CustomExceptionEnum.COMMENT_NOT_FOUND);
            }
            comment.setCommentCount(0L);
            comment.setLikeCount(0L);
            commentMapper.insert(comment);
            questionExtMapper.incSecComment(dbComment.getId());
            //创建通知
            createNotify(comment,dbComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);
        }
    }

    private void createNotify(Comment comment, Long receiver, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        notification.setCreateTime(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(comment.getParentId());
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> ListByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("create_time desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取评论人的去重列表
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    public void incLike(Long id) {
        commentExtMapper.incLikeCount(id);
    }
}
