package site.alexkononsol.siteToOK.controllers;


import site.alexkononsol.siteToOK.entity.Article;
import site.alexkononsol.siteToOK.form.ArticleForm;
import site.alexkononsol.siteToOK.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

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
        int countArticles = repository.sizeTable();
        if(countArticles==0){
            model.addAttribute("isEmpty",true);
        }
        else {
            model.addAttribute("isEmpty",false);
            ArrayList<Article> articles = (ArrayList<Article>) repository.lastArticle(page);
            model.addAttribute("articles",articles);
            int countPagesWithArticles=(countArticles%5 > 0)?(countArticles/5)+1:countArticles/5;
            model.addAttribute("countPages",countPagesWithArticles);
            model.addAttribute("thisPage",page);
        }
        return "article";
    }


}
