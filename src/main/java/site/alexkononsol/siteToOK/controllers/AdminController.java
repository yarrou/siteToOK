package site.alexkononsol.siteToOK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.alexkononsol.siteToOK.entity.*;
import site.alexkononsol.siteToOK.service.MessageService;
import site.alexkononsol.siteToOK.service.ReviewService;
import site.alexkononsol.siteToOK.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final MessageService messageService;
    private final ReviewService reviewService;

    public AdminController(UserService userService, MessageService messageService, ReviewService reviewService) {
        this.userService = userService;
        this.messageService = messageService;
        this.reviewService = reviewService;
    }

    @GetMapping("/admin")
    public String userList(Model model,@RequestParam(value = "page",defaultValue = "1") int page) {

        long countUsersInDB = userService.userCount();
        List<User> users = userService.get5Users(page);
        Role admRole = new Role(2L, "ROLE_ADMIN");
        long countPagesWithUsers =(countUsersInDB%5 > 0)?(countUsersInDB / 5)+1:countUsersInDB / 5;
        model.addAttribute("users", users);
        model.addAttribute("pageusers",page);
        model.addAttribute("countPages",countPagesWithUsers);
        model.addAttribute("admRole",admRole);
        return "admin";
    }

    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        if (action.equals("addAdminRole")){
            userService.addAdminRole(userId);
        }
        return "redirect:/admin";
    }

    @GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.getUserList(userId));
        return "admin";
    }

    @GetMapping("/admin/view_profile")
    public String viewProfile(ModelMap model, @RequestParam(required = true) Long profileId){
        User user = userService.findUserById(profileId);
        Profile profile = user.getProfile();
        model.addAttribute("user",user);
        model.addAttribute("profile",profile);
        return "view_profile";
    }

    @GetMapping("/admin/appeals")
    public String viewAppealsPage(ModelMap model, Principal principal){
        List<Message> usersAppeals = messageService.getAppeals();
        model.addAttribute("appeals",usersAppeals);
        return "appeals";
    }

    @GetMapping("admin/appeals/{id}")
    public String appealPage(ModelMap model,@PathVariable("id") long id){
        Message appeal = messageService.getById(id);
        model.addAttribute("appeal",appeal);
        return "appeal";
    }
    @PostMapping("/admin/appeals/{id}")
    public String appealsActionsPage(ModelMap model,@PathVariable("id") long id,@RequestParam(defaultValue = "",required = true) String action,
                                     Principal principal){
        Message message = messageService.getById(id);
        String actionPath ="/";
        if(action.equals("delete")){
            messageService.delete(message);
             actionPath = "redirect:/admin/appeals";
        }
        if(action.equals("reply")){
            message.setRecipient(principal.getName());
            messageService.save(message);
            actionPath = "redirect:/messages/"+message.getSender();
        }
        return actionPath;
    }

    @GetMapping("/admin/reviews")
    public String showReviewsPage(ModelMap model,@RequestParam(value = "page",defaultValue = "1") int page){
        int countReviews = reviewService.sizeTableForCheck();
        if(countReviews==0){
            model.addAttribute("isEmpty",true);
        }else{
            model.addAttribute("isEmpty",false);
            List<Review> list = reviewService.lastReviewsForCheck(page);
            model.addAttribute("list",list);
            int countPagesWithReviews=(countReviews%5 > 0)?(countReviews/5)+1:countReviews/5;
            model.addAttribute("countPages",countPagesWithReviews);
            model.addAttribute("thisPage",page);
        }
        return "reviews_for_check";
    }

    @PostMapping("/admin/reviews_for_check")
    public String actionWithReviews(@RequestParam(defaultValue = "",required = true) String action,@RequestParam(defaultValue = "",required = true) long reviewId){
        Review review = reviewService.getById(reviewId);
        if(action.equals("delete")){
            reviewService.delete(review);
        }
        if(action.equals("approve")){
            review.setApproved(true);
            reviewService.save(review);
        }
        return "redirect:/admin/reviews";
    }
}
