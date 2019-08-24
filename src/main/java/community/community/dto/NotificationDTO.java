package community.community.dto;

import community.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private String type;
    private Long createTime;
    private Integer status;
    private User notifier;
    private String outerTitle;
}
