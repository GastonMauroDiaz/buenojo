package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.ciis.buenojo.domain.enumeration.MultipleChoiceInteractionTypeEnum;

/**
 * A MultipleChoiceQuestion.
 */
@Entity
@Table(name = "multiple_choice_question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MultipleChoiceQuestion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "question", length = 100, nullable = false)
    private String question;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private MultipleChoiceInteractionTypeEnum interactionType;

    @Min(value = 1)
    @Max(value = 100)
    @Column(name = "exercise_id")
    private Integer exerciseId;

    @Column(name = "source")
    private String source;

    @ManyToOne
    private MultipleChoiceExerciseContainer multipleChoiceExerciseContainer;

    @OneToOne    private ImageResource imageResource;

    @OneToOne    private MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public MultipleChoiceInteractionTypeEnum getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(MultipleChoiceInteractionTypeEnum interactionType) {
        this.interactionType = interactionType;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MultipleChoiceExerciseContainer getMultipleChoiceExerciseContainer() {
        return multipleChoiceExerciseContainer;
    }

    public void setMultipleChoiceExerciseContainer(MultipleChoiceExerciseContainer multipleChoiceExerciseContainer) {
        this.multipleChoiceExerciseContainer = multipleChoiceExerciseContainer;
    }

    public ImageResource getImageResource() {
        return imageResource;
    }

    public void setImageResource(ImageResource imageResource) {
        this.imageResource = imageResource;
    }

    public MultipleChoiceSubjectSpecific getMultipleChoiceSubjectSpecific() {
        return multipleChoiceSubjectSpecific;
    }

    public void setMultipleChoiceSubjectSpecific(MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific) {
        this.multipleChoiceSubjectSpecific = multipleChoiceSubjectSpecific;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) o;

        if ( ! Objects.equals(id, multipleChoiceQuestion.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
            "id=" + id +
            ", question='" + question + "'" +
            ", interactionType='" + interactionType + "'" +
            ", exerciseId='" + exerciseId + "'" +
            ", source='" + source + "'" +
            '}';
    }
}
