package org.alex.konon.sol.siteToOK.controllers;


import org.alex.konon.sol.siteToOK.entity.Article;
import org.alex.konon.sol.siteToOK.form.ArticleForm;
import org.alex.konon.sol.siteToOK.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository repository;


    @GetMapping("add_article")
    public String testPage(ModelMap model) {
        ArticleForm articleForm = new ArticleForm();
        model.put("articleForm",articleForm);
        return "add_article";
    }


    @PostMapping("add_article")
    public String addNewArticle(ModelMap model, @ModelAttribute("articleForm") ArticleForm articleForm) {
        //article.setId(null);
        repository.save(new Article(articleForm.getText(),articleForm.getTitle()));
        return "redirect:/article";
    }

    @GetMapping("article")
    public String articlePage(ModelMap model){
        ArrayList<Article> articles = (ArrayList<Article>) repository.findAll();
        String ar;
        if(articles.isEmpty()){
             Article article = new Article("<h2>место для статьи</h2><p>Статья еще не написана, еще в процессе</p>");
             articles.add(article);
        }
        model.addAttribute("articles",articles);
        return "article";
    }

}
