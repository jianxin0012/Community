package community.community.controller;

import community.community.dto.CommentDTO;
import community.community.dto.QuestionDTO;
import community.community.service.CommentService;
import community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        //累加阅读数
        questionService.incView(id);
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> comments=commentService.ListByQuestionId(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        return "question";
    }
}
