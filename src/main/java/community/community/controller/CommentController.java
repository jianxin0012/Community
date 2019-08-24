package community.community.controller;

import community.community.dto.CommentCreateDTO;
import community.community.dto.CommentDTO;
import community.community.dto.ResultDTO;
import community.community.enums.CommentTypeEnum;
import community.community.exception.CustomExceptionEnum;
import community.community.model.Comment;
import community.community.model.User;
import community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;



    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        Comment comment = new Comment();
        User user = (User)request.getSession().getAttribute("user");
        if (user==null){
            return ResultDTO.errorOf(CustomExceptionEnum.NO_LOGIN);
        }
        if (commentCreateDTO==null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomExceptionEnum.COMMENT_IS_EMPTY);
        }
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setCreateTime(System.currentTimeMillis());
        comment.setModifiedTime(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        commentService.insert(comment);
        return ResultDTO.successOf();
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO question(@PathVariable(name = "id") Long id){
        List<CommentDTO> sec_comments = commentService.ListByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.successOf(sec_comments);
    }

    @ResponseBody
    @RequestMapping(value = "/comment_like/{id}",method = RequestMethod.GET)
    public Object incLike(@PathVariable(name = "id") Long id){
        commentService.incLike(id);
        return ResultDTO.successOf();
    }
}
