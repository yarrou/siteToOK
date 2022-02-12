package site.alexkononsol.siteToOK.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.alexkononsol.siteToOK.entity.Review;
import site.alexkononsol.siteToOK.service.ReviewService;
import site.alexkononsol.siteToOK.service.UserService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Controller
public class ReviewsController {

    private final UserService userService;
    private final ReviewService reviewService;

    public ReviewsController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public String showReviewsPage(ModelMap model,@RequestParam(value = "page",defaultValue = "1") int page){
        int countReviews = reviewService.countReviews();
        if(countReviews==0){
            model.addAttribute("isEmpty",true);
        }else{
            model.addAttribute("isEmpty",false);
            List<Review> list = reviewService.lastReviews(page);
            model.addAttribute("list",list);
            int countPagesWithReviews=(countReviews%5 > 0)?(countReviews/5)+1:countReviews/5;
            model.addAttribute("countPages",countPagesWithReviews);
            model.addAttribute("thisPage",page);
        }
        return "reviews";
    }

    @GetMapping("/review_add")
    public String addReviewPage(ModelMap model){
        model.addAttribute("review",new Review());
        return "review_add";
    }

    @PostMapping("/review_add")
    public String addReview(Principal principal, @ModelAttribute("review") Review review){

        review.setUser(userService.getUserByName(principal.getName()));
        review.setDate(LocalDateTime.now());
        reviewService.save(review);
        return "redirect:/reviews";
    }
}