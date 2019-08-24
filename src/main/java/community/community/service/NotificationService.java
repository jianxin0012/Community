package community.community.service;

import community.community.dto.NotificationDTO;
import community.community.dto.PageDTO;
import community.community.enums.NotificationTypeEnum;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import community.community.mapper.*;
import community.community.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private NotificationExtMapper notificationExtMapper;

    public PageDTO list(Long userId, Integer page, Integer size) {
        Integer offset = (page - 1) * size;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("create_time desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
//            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setId(notification.getId());
            notificationDTO.setCreateTime(notification.getCreateTime());
            notificationDTO.setStatus(notification.getStatus());
            if (notification.getType() == NotificationTypeEnum.REPLY_QUESTION.getType()) {
                notificationDTO.setType(NotificationTypeEnum.REPLY_QUESTION.getName());
                Question question = questionMapper.selectByPrimaryKey(notification.getOuterid());
                notificationDTO.setOuterTitle(question.getTitle());
            } else {
                notificationDTO.setType(NotificationTypeEnum.REPLY_COMMENT.getName());
                Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterid());
                notificationDTO.setOuterTitle(comment.getContent());
            }
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(notification.getNotifier());
            List<User> users = userMapper.selectByExample(example);
            notificationDTO.setNotifier(users.get(0));
            notificationDTOS.add(notificationDTO);
        }
        pageDTO.setData(notificationDTOS);
        NotificationExample notificationExample1 = new NotificationExample();
        notificationExample1.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample1);
        pageDTO.setPagination(totalCount, page, size);
        return pageDTO;
    }

    public Long unreadCount(Long id) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(id).andStatusEqualTo(0);
        return notificationMapper.countByExample(notificationExample);
    }

    public Long read(Long id, User user) {
        Long questionId=0L;
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification==null){
            throw new CustomException(CustomExceptionEnum.NOTIFICATION_NOT_FOUND);
        }
        if (notification.getReceiver()!=user.getId()){
            throw new CustomException(CustomExceptionEnum.READ_NOTIFICATION_FAIL);
        }

        if (notification.getType() == 1) {
            questionId = notification.getOuterid();
        }
        if (notification.getType() == 2) {
            Comment comment = commentMapper.selectByPrimaryKey(notification.getOuterid());
            questionId = comment.getParentId();
        }
        notificationExtMapper.read(id);
        return questionId;
    }
}
