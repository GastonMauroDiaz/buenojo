package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The tag pool where you get your similar choices from
 */
@Entity
@Table(name = "tag_pool")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagPool implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * how similar this tag is to some other. 1 very similar, 3 not so similar
     */    @NotNull
    @Column(name = "similarity", nullable = false)
    private Integer similarity;
    
    @NotNull
    @ManyToOne(cascade=CascadeType.REMOVE)
    
    @JoinColumn(name="tag_id", referencedColumnName="id")
    private Tag tag;

    @NotNull
    @ManyToOne(cascade=CascadeType.REMOVE)
    private Tag similarTag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Integer similarity) {
        this.similarity = similarity;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getSimilarTag() {
        return similarTag;
    }

    public void setSimilarTag(Tag tag) {
        this.similarTag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagPool tagPool = (TagPool) o;

        if ( ! Objects.equals(id, tagPool.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TagPool{" +
            "id=" + id +
            ", similarity='" + similarity + "'" +
            '}';
    }
}
