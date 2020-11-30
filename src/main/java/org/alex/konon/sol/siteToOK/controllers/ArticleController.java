package org.alex.konon.sol.siteToOK.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    @GetMapping("new_article")
    public String newArticle(Model model){
        return "new_article";
    }

}
