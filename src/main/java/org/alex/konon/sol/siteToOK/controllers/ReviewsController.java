package org.alex.konon.sol.siteToOK.controllers;


import org.alex.konon.sol.siteToOK.entity.Review;
import org.alex.konon.sol.siteToOK.repositories.ReviewRepository;
import org.alex.konon.sol.siteToOK.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Controller
public class ReviewsController {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/reviews")
    public String showReviewsPage(ModelMap model,@RequestParam(value = "page",defaultValue = "1") int page){
        int countReviews = reviewRepository.sizeTable();
        if(countReviews==0){
            model.addAttribute("isEmpty",true);
        }else{
            model.addAttribute("isEmpty",false);
            ArrayList<Review> list = reviewRepository.lastReviews(page);
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

        review.setUser(userRepository.findByUsername(principal.getName()));
        review.setDate(LocalDateTime.now());
        reviewRepository.save(review);
        return "redirect:/reviews";
    }
}