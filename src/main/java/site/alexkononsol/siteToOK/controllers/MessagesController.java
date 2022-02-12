package site.alexkononsol.siteToOK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import site.alexkononsol.siteToOK.entity.Message;
import site.alexkononsol.siteToOK.service.MessageService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
public class MessagesController {
    private final MessageService messageService;

    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/messages")
    public String showMessagesPage(Principal principal, ModelMap model){
        Set<String> whoWroteNames = messageService.getDialogues(principal.getName());
        model.addAttribute("whoWrote",whoWroteNames);
        return  "messages";
    }

    @GetMapping("/messages/{name}")
    public String readMessagesPage(@PathVariable("name") String name, ModelMap model,Principal principal){
        model.addAttribute("messages",messageService.getMessagesList(principal.getName(),name));
        model.addAttribute("nameUser",name);
        return "messagesFromUser";
    }

    @GetMapping("/messages/{name}/sendMessage")
    public String SendMessagePage(@PathVariable("name")String name,ModelMap model,Principal principal){
        List<Message> lastFiveMessages = messageService.getLast5Messages(principal.getName(),name);
        Message message = new Message();
        model.addAttribute("recipient",name);
        model.addAttribute("message",message);
        model.addAttribute("lastFiveMessages",lastFiveMessages);
        return "sendMessage";
    }

    @PostMapping("/messages/{name}/sendMessage")
    public String sendMessage(@PathVariable("name") String recipientName, Principal principal, @ModelAttribute("message") Message message){
        message.setRecipient(recipientName);
        message.setSender(principal.getName());
        message.setDate(LocalDateTime.now());
        messageService.save(message);
        return "redirect:/messages/"+recipientName;
    }
}
