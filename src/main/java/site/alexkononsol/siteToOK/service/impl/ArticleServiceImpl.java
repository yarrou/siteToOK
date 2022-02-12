package site.alexkononsol.siteToOK.service.impl;

import org.springframework.stereotype.Service;
import site.alexkononsol.siteToOK.entity.Article;
import site.alexkononsol.siteToOK.repositories.ArticleRepository;
import site.alexkononsol.siteToOK.service.ArticleService;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;

    public ArticleServiceImpl(ArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Article article) {
        repository.save(article);
    }

    @Override
    public Article getById(long id) {
        return repository.getOne(id);
    }

    @Override
    public void deleteById(long id) {
        repository.deleteById(id);
    }

    @Override
    public int articleCount() {
        return repository.sizeTable();
    }

    @Override
    public List<Article> get5Articles(int page) {
        return repository.lastArticle(page);
    }
}
