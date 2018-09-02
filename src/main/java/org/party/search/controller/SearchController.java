package org.party.search.controller;

import org.party.search.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.party.search.service.ElasticsearchQueryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private ElasticsearchQueryService elasticsearchQueryService;

    @GetMapping("/{q}")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Article> search(@PathVariable("q") String q) throws IOException {
            return this.elasticsearchQueryService.query( q );
    }
}
