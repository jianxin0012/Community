package community.community.dto;

import community.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private String content;
    private Long commentator;
    private Long createTime;
    private Long modifiedTime;
    private Long likeCount;
    private Long commentCount;
    private User user;
}
