package org.party.search.domain;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Tag extends AbstractEntity {

    private String name;
}
