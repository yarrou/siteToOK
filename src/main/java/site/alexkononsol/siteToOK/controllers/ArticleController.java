package site.alexkononsol.siteToOK.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.alexkononsol.siteToOK.entity.Article;
import site.alexkononsol.siteToOK.form.ArticleForm;
import site.alexkononsol.siteToOK.service.ArticleService;

import java.util.ArrayList;

@Controller
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    @GetMapping("add_article")
    public String testPage(ModelMap model) {
        ArticleForm articleForm = new ArticleForm();
        model.put("articleForm",articleForm);
        return "add_article";
    }

    @PostMapping("add_article")
    public String addNewArticle( @ModelAttribute("articleForm") ArticleForm articleForm) {
        service.save(new Article(articleForm.getText(),articleForm.getTitle()));
        return "redirect:/article";
    }


    @GetMapping("redactor_article")
    public String redactor(ModelMap model,@RequestParam(value = "idArticle") long id){
        Article article = service.getById(id);
        model.addAttribute("article",article);
        model.addAttribute("idArt",id);
        return "redactor_article";
    }
    @PostMapping("redactor_article")
    public String redactorPost( @ModelAttribute("article") Article article){
        service.save(article);
        return "redirect:/article";
    }

    @GetMapping("delete_article")
    public String deleteArticle( @RequestParam(value = "idArticle") long id){
        service.deleteById(id);
        return "redirect:/article";
    }


    @GetMapping("article")
    public String articlePage(ModelMap model,@RequestParam(value = "page",defaultValue = "1") int page){
        int countArticles = service.articleCount();
        if(countArticles==0){
            model.addAttribute("isEmpty",true);
        }
        else {
            model.addAttribute("isEmpty",false);
            ArrayList<Article> articles = (ArrayList<Article>) service.get5Articles(page);
            model.addAttribute("articles",articles);
            int countPagesWithArticles=(countArticles%5 > 0)?(countArticles/5)+1:countArticles/5;
            model.addAttribute("countPages",countPagesWithArticles);
            model.addAttribute("thisPage",page);
        }
        return "article";
    }


}
