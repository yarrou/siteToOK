package site.alexkononsol.siteToOK.genericExceptionHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(value = MultipartException.class)
    public ModelAndView handleFileUploadException(MultipartException mpex, HttpServletRequest request) {

        ModelAndView modelAndVew = new ModelAndView("error");
        modelAndVew.addObject("clarification","файл слишком большой");
        modelAndVew.addObject("errorMsg", mpex.getMessage());
        return modelAndVew;
    }

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {

        ModelAndView modelAndVew = new ModelAndView("error");
        modelAndVew.addObject("errorMsg", ex.getMessage());
        return modelAndVew;
    }
}