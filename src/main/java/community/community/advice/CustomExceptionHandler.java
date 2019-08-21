package community.community.advice;

import com.alibaba.fastjson.JSON;
import community.community.dto.ResultDTO;
import community.community.exception.CustomException;
import community.community.exception.CustomExceptionEnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(Throwable e, Model model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)){
            ResultDTO resultDTO;
            if (e instanceof CustomException){
                resultDTO=ResultDTO.errorOf((CustomException) e);
            }else {
                resultDTO=ResultDTO.errorOf(CustomExceptionEnum.SERVER_ERROR);
            }
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException e1) {
            }
            return null;
        }else {
            if (e instanceof CustomException) {
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", "服务冒烟啦，请稍后再试");
            }
            return new ModelAndView("error");
        }
    }
}
