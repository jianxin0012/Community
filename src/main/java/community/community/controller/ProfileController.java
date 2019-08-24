package community.community.controller;

import community.community.dto.PageDTO;
import community.community.model.User;
import community.community.service.NotificationService;
import community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size,
                          @PathVariable(name = "action") String action,
                          Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return "redirect:/";
        //如果action=null。。。。。
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PageDTO pageDTO = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("QuestionPageList",pageDTO);
        }
        if ("replies".equals(action)) {
            PageDTO pageDTO=notificationService.list(user.getId(), page, size);

            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

            model.addAttribute("ReplyPageList",pageDTO);
        }
        return "profile";
    }
}
