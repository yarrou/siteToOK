package org.alex.konon.sol.siteToOK.controllers;

import org.alex.konon.sol.siteToOK.entity.Message;
import org.alex.konon.sol.siteToOK.entity.Role;
import org.alex.konon.sol.siteToOK.entity.User;
import org.alex.konon.sol.siteToOK.repositories.MessageRepository;
import org.alex.konon.sol.siteToOK.repositories.UserRepository;
import org.alex.konon.sol.siteToOK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

@Controller
public class MessagesController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    UserService service;

    @GetMapping("/messages")
    public String showMessagesPage(Principal principal, ModelMap model){
        User user = userRepository.findByUsername(principal.getName());
        LinkedHashSet<String> whoWroteNames = messageRepository.dialogues(principal.getName());
        model.addAttribute("whoWrote",whoWroteNames);
        return  "messages";
    }

    @GetMapping("/messages/{name}")
    public String readMessagesPage(@PathVariable("name") String name, ModelMap model,Principal principal){
        model.addAttribute("messages",getMessagesList(principal.getName(),name));
        model.addAttribute("nameUser",name);
        return "messagesFromUser";
    }

    @GetMapping("/messages/{name}/sendMessage")
    public String SendMessagePage(@PathVariable("name")String name,ModelMap model,Principal principal){
        ArrayList<Message> lastFiveMessages = new ArrayList<>();
        Iterator<Message> iterator = getMessagesList(principal.getName(),name).iterator();
        for(int i = 0;( i < 5 )&& iterator.hasNext();i++){
            lastFiveMessages.add( iterator.next());
        }
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
        messageRepository.save(message);
        return "redirect:/messages/"+recipientName;
    }
    ArrayList<Message> getMessagesList(String user1,String user2){
        ArrayList<Message> messages = null;
        if(user2.equals("administration")){
            messages = messageRepository.requestsToAdministration(user1);
        }
        else {
            messages = messageRepository.corespondence(user1,user2);
        }
        return messages;
    }
}
