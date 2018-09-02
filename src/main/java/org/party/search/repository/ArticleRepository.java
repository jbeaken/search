package org.party.search.repository;

import org.party.search.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    @Query("select a from Article a")
    List<Article> findAll(Pageable pageable);
}
