package site.alexkononsol.siteToOK.controllers;

import site.alexkononsol.siteToOK.entity.Message;
import site.alexkononsol.siteToOK.repositories.MessageRepository;
import site.alexkononsol.siteToOK.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class MainPageController {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String mainPage(ModelMap model, Principal principal){
        Message message = new Message();
        model.addAttribute("message",message);
        return "index";
    }

    @PostMapping("/")
    public String mainPage(@ModelAttribute("message") Message message, Principal principal){
        message.setSender(principal.getName());
        //message.setRecipient("admin");
        message.setDate(LocalDateTime.now());
        messageRepository.save(message);
        return "redirect:/";
    }
}