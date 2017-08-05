package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PhotoLocationImage.
 */
@Entity
@Table(name = "photo_location_image")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhotoLocationImage implements Serializable, IKeywordAnnotated {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.REMOVE) 
    private ImageResource image;
    
    @ManyToMany    
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "photo_location_image_keyword",
               joinColumns = @JoinColumn(name="photo_location_images_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="keywords_id", referencedColumnName="ID"))
    private Set<PhotoLocationKeyword> keywords = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImageResource getImage() {
        return image;
    }

    public void setImage(ImageResource imageResource) {
        this.image = imageResource;
    }

    public Set<PhotoLocationKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<PhotoLocationKeyword> photoLocationKeywords) {
        this.keywords = photoLocationKeywords;
    }

    
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhotoLocationImage photoLocationImage = (PhotoLocationImage) o;

        if ( ! Objects.equals(id, photoLocationImage.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PhotoLocationImage{" +
            "id=" + id +
            '}';
    }
    
    public static PhotoLocationImage fromImageResource(ImageResource imageResource){
    	PhotoLocationImage img = new PhotoLocationImage();
    	img.setImage(imageResource);
    	return img;
    	
    }
}
