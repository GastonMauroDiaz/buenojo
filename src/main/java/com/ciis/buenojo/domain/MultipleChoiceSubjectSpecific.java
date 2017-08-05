package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MultipleChoiceSubjectSpecific.
 */
@Entity
@Table(name = "multiple_choice_subject_specific")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MultipleChoiceSubjectSpecific implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    private MultipleChoiceSubject multipleChoiceSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultipleChoiceSubject getMultipleChoiceSubject() {
        return multipleChoiceSubject;
    }

    public void setMultipleChoiceSubject(MultipleChoiceSubject multipleChoiceSubject) {
        this.multipleChoiceSubject = multipleChoiceSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific = (MultipleChoiceSubjectSpecific) o;

        if ( ! Objects.equals(id, multipleChoiceSubjectSpecific.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MultipleChoiceSubjectSpecific{" +
            "id=" + id +
            ", text='" + text + "'" +
            '}';
    }
}
