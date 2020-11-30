package org.alex.konon.sol.siteToOK.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("site")
    public String homePage(Model model){
        return "index";
    }

    @GetMapping("olga_kononovich_more")
    public String olga_more(Model model){
        return "generic";
    }

    @GetMapping("articles")
    public String articlesPage(Model model){return "article";}

}
