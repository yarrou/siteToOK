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
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository repository;
    //
    @GetMapping("add_article")
    public String testPage(ModelMap model) {
        ArticleForm articleForm = new ArticleForm();
        model.put("articleForm",articleForm);
        return "add_article";
    }

    @PostMapping("add_article")
    public String addNewArticle(ModelMap model, @ModelAttribute("articleForm") ArticleForm articleForm) {
        repository.save(new Article(articleForm.getText(),articleForm.getTitle()));
        return "redirect:/article";
    }


    @GetMapping("redactor_article")
    public String redactor(ModelMap model,@RequestParam(value = "idArticle",required = true) long id){
        Article article = repository.findById(id).get();
        model.addAttribute("article",article);
        model.addAttribute("idArt",id);
        return "redactor_article";
    }
    @PostMapping("redactor_article")
    public String redactorPost(ModelMap model, @ModelAttribute("article") Article article){
        repository.save(article);
        return "redirect:/article";
    }

    @GetMapping("delete_article")
    public String deleteArticle(ModelMap model , @RequestParam(value = "idArticle",required = true) long id){
        repository.deleteById(id);
        return "redirect:/article";
    }


    @GetMapping("article")
    public String articlePage(ModelMap model,@RequestParam(value = "page",defaultValue = "1") int page){

        ArrayList<Article> articles=null;
        int countArticles = repository.sizeTable();//number of records in the "Articles" table
        int countPagesWithArticles =1;//number of pages with articles
        if(countArticles > 0){
            articles = (ArrayList<Article>) repository.lastArticle(page);
            if(countArticles%5 > 0){
                countPagesWithArticles = (countArticles / 5) +1;
            }
            else {
                countPagesWithArticles = countArticles / 5;
            }
        }
        else{
             Article article = new Article("<h2>Пока статей нет</h2><p>Загляните попозже.</p>");
             articles.add(article);
        }
        model.addAttribute("articles",articles);
        model.addAttribute("pagearticles",page);
        model.addAttribute("countPages",countPagesWithArticles);
        return "article";
    }
}
