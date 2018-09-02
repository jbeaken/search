package org.party.search.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Issue extends AbstractEntity {

    @NotNull
    private String issueNo;
}
