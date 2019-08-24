package community.community.controller;

import community.community.model.User;
import community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String read(@PathVariable(name="id") Long id,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        Long questionId=notificationService.read(id,user);
        return "redirect:/question/"+questionId;
    }
}
