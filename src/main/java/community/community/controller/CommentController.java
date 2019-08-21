package community.community.controller;

import community.community.dto.CommentCreateDTO;
import community.community.dto.ResultDTO;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import community.community.model.Comment;
import community.community.model.User;
import community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
            throw new CustomException(CustomExceptionEnum.NO_LOGIN);
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
}
