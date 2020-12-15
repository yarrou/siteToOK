package org.alex.konon.sol.siteToOK.repositories;

import org.alex.konon.sol.siteToOK.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.ArrayList;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long>{
    Article save(Article article);

    @Query(value = "SELECT * FROM article  LIMIT 2 ",nativeQuery = true)
    List<Article> lastArticle();
    //@Param("numberPage") int numberPage
}
