package com.ciis.buenojo.domain;


import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.ciis.buenojo.domain.enumeration.Region;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;

/**
 * A ExerciseTip.
 */
@Entity
@Table(name = "exercise_tip")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExerciseTip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     *  the tag this tip should be associated with
     */
    @OneToOne
    private Tag tag;
    

    /**
     * further description of the tip itself *
     */    
    @Column(name = "tip_detail", columnDefinition="clob")
    private String detail;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "exercise_tip_image_resource",
               joinColumns = @JoinColumn(name="exercise_tip_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="image_resource_id", referencedColumnName="ID"))
  
    private Set<ImageResource> images = new HashSet<>();
    
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @CollectionTable(name = "tip_region",  
            joinColumns = @JoinColumn( name = "exercise_tip_id"))
    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Set<Region> regions;
    
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @CollectionTable(name = "tip_satellite_image_type", 
            joinColumns = @JoinColumn( name = "exercise_tip_id"))
    @Column(name = "satellite_image_type")
    @Enumerated(EnumType.STRING)
    private Set<SatelliteImageType> imageTypes;
    
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String tipDetail) {
        this.detail = tipDetail;
    }

    public EnumSet<Region> getRegions() {
    	return EnumSet.copyOf(regions);
    }
    
    public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Set<ImageResource> getImages() {
		return images;
	}

	public void setImages(Set<ImageResource> images) {
		this.images = images;
	}

	public void setRegions(Set<Region> regions) {
    	this.regions = regions;
    }
    
    public Set<SatelliteImageType> getImageTypes() {
		return imageTypes;
	}

	public void setImageTypes(Set<SatelliteImageType> imageTypes) {
		this.imageTypes = imageTypes;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExerciseTip exerciseTip = (ExerciseTip) o;

        if ( ! Objects.equals(id, exerciseTip.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ExerciseTip{" +
                "id=" + id +
                ", tag=" + tag.getName() +
                ", regions="+ regions.toString()+
                ", images="+ images.toString() +
                ", imageTypes="+imageTypes.toString() +
                ", tipDetail='" + detail + "'" +
                '}';
    }
}
