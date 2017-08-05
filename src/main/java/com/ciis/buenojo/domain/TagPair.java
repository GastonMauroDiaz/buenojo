package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Pairs up a tag with the corresponding slot where "id" is the number of
 * tagSlotId and the tag is the tag ID"id" "etiqueta""0" "13""1" "3""2" "21""3"
 * "24""4" "26"
 */
@Entity
@Table(name = "tag_pair")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TagPair implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "tag_slot_id", nullable = false)
    private Integer tagSlotId;

    @ManyToOne    
  
    private Tag tag;

    @ManyToOne
    @JsonIgnore
    private ImageCompletionSolution imageCompletionSolution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Integer getTagSlotId() {
        return tagSlotId;
    }

    public void setTagSlotId(Integer tagSlotId) {
        this.tagSlotId = tagSlotId;
    }

    public ImageCompletionSolution getImageCompletionSolution() {
        return imageCompletionSolution;
    }

    public void setImageCompletionSolution(ImageCompletionSolution imageCompletionSolution) {
        this.imageCompletionSolution = imageCompletionSolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagPair tagPair = (TagPair) o;

        if ( ! Objects.equals(id, tagPair.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TagPair{" +
            "id=" + id +
            ", tagSlotId='" + tagSlotId + "'" +
            ", tagId='" + tag +"'"+
            ", solution='" + imageCompletionSolution + "'" +
            '}';
    }
}
