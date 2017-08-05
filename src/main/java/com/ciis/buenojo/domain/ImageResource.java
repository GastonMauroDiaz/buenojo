package com.ciis.buenojo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ciis.buenojo.domain.listeners.ImageResourceListener;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Represents an image resource in the system.
 */
@Entity
@Table(name = "image_resource")
@EntityListeners( ImageResourceListener.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ImageResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private byte[] loResImage;


    @Column(name = "lo_res_image_content_type", nullable = false)
    private String loResImageContentType;
    
    @Transient
    @JsonSerialize
    @JsonDeserialize
    private byte[] hiResImage;


    @Column(name = "hi_res_image_content_type", nullable = false)
    private String hiResImageContentType;
    public Long getId() {
        return id;
    }

    @Column(name = "hi_res_image_path")
    private String hiResImagePath;
    @Column(name = "lo_res_image_path")
    private String loResImagePath;
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Transient
    public byte[] getLoResImage() {
        return loResImage;
    }

    public void setLoResImage(byte[] loResImage) {
        this.loResImage = loResImage;
    }

    public String getLoResImageContentType() {
        return loResImageContentType;
    }

    public void setLoResImageContentType(String loResImageContentType) {
        this.loResImageContentType = loResImageContentType;
    }

    public byte[] getHiResImage() {
        return hiResImage;
    }

    public void setHiResImage(byte[] hiResImage) {
        this.hiResImage = hiResImage;
    }

    public String getHiResImageContentType() {
        return hiResImageContentType;
    }

    public void setHiResImageContentType(String hiResImageContentType) {
        this.hiResImageContentType = hiResImageContentType;
    }

    public String getHiResImagePath() {
		return hiResImagePath;
	}

	public void setHiResImagePath(String hiResImagePath) {
		this.hiResImagePath = hiResImagePath;
	}

	public String getLoResImagePath() {
		return loResImagePath;
	}

	public void setLoResImagePath(String loResImagePath) {
		this.loResImagePath = loResImagePath;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ImageResource imageResource = (ImageResource) o;

        if ( ! Objects.equals(id, imageResource.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ImageResource{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", loResImage='" + loResImage + "'" +
            ", loResImagePath='" + loResImagePath + "'" +
            ", loResImageContentType='" + loResImageContentType + "'" +
            ", hiResImage='" + hiResImage + "'" +
            ", hiResImagePath='" + hiResImagePath + "'" +
            ", hiResImageContentType='" + hiResImageContentType + "'" +
            '}';
    }
    
   

}
