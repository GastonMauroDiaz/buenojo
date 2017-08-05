package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A HangManGameContainer.
 */
@Entity
@Table(name = "hang_man_game_container")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HangManGameContainer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HangManGameContainer hangManGameContainer = (HangManGameContainer) o;

        if ( ! Objects.equals(id, hangManGameContainer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "HangManGameContainer{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
