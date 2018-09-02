package org.party.search.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Article extends AbstractEntity {

    @NotNull
    private String headline;

    private Date published;

    private String summary;

    @Transient
    private Double score;

    @Column(columnDefinition = "longtext")
    private String content;

    private String byline;

    private String youtubeId;

    //How article is displayed on article drill down page
    private String articleStyle;

    //An intro paragraph for feature articles
    @Column(columnDefinition = "longtext")
    private String standfirst;

    String imagePath;

    //Alt that goes with imagePath
    private String imageAlt;

    //Shorter alternate headline for use on
    private String shorthead;

    @ManyToOne
    private Issue issue;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JoinTable(name = "article_tags", joinColumns={@JoinColumn(name="article_id")}, inverseJoinColumns={@JoinColumn(name="tag_id")})
    private Set<Tag> tags;
}
