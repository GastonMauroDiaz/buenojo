package com.ciis.buenojo.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * A HangManExercise.
 */
@Entity
@Table(name = "hang_man_exercise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManExercise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 15, max = 100)
    @Column(name = "task", length = 100, nullable = false)
    private String task;

    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "exercise_order")
    private Integer exerciseOrder;

    @OneToOne   
    private ImageResource image;
    
    @OneToOne    private ImageResource highlightedArea;

    @ManyToOne    private HangManGameContainer hangmanGameContainer;

    private String optionType;
    
    public HangManGameContainer getHangmanGameContainer() {
		return hangmanGameContainer;
	}

	public void setHangmanGameContainer(HangManGameContainer hangmanGameContainer) {
		this.hangmanGameContainer = hangmanGameContainer;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getExerciseOrder() {
        return exerciseOrder;
    }

    public void setExerciseOrder(Integer exerciseOrder) {
        this.exerciseOrder = exerciseOrder;
    }

    public ImageResource getImage() {
        return image;
    }

    public void setImage(ImageResource imageResource) {
        this.image = imageResource;
    }

    public ImageResource getHighlightedArea() {
        return highlightedArea;
    }

    public void setHighlightedArea(ImageResource imageResource) {
        this.highlightedArea = imageResource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HangManExercise hangManExercise = (HangManExercise) o;

        if ( ! Objects.equals(id, hangManExercise.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManExercise{" +
            "id=" + id +
            ", task='" + task + "'" +
            ", exerciseOrder='" + exerciseOrder + "'" +
            '}';
    }

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
}
