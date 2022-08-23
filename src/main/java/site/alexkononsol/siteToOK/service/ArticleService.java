package site.alexkononsol.siteToOK.service;

import site.alexkononsol.siteToOK.entity.Article;

import java.util.List;


public interface ArticleService {
    void save(Article article);
    Article getById(long id);
    void deleteById(long id);
    int articleCount();
    List<Article> get5Articles(int page);
    List<Article> get5Preview(int page);
}
