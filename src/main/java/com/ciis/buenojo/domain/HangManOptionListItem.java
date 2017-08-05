package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HangManOptionListItem.
 */
@Entity
@Table(name = "hang_man_option_list_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManOptionListItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "option_group", nullable = false)
    private String optionGroup;

    @Column(name = "option_type")
    private String optionType;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "option_text", length = 25, nullable = false)
    private String optionText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(String optionGroup) {
        this.optionGroup = optionGroup;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HangManOptionListItem hangManOptionListItem = (HangManOptionListItem) o;

        if ( ! Objects.equals(id, hangManOptionListItem.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManOptionListItem{" +
            "id=" + id +
            ", optionGroup='" + optionGroup + "'" +
            ", optionType='" + optionType + "'" +
            ", optionText='" + optionText + "'" +
            '}';
    }
}
