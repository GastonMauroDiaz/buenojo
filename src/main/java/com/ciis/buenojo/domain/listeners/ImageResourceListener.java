package com.ciis.buenojo.domain.listeners;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.persistence.PostRemove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ciis.buenojo.config.BuenOjoEnvironment;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.service.util.BeanUtil;

public class ImageResourceListener {
	
	private final Logger log = LoggerFactory.getLogger(ImageResourceListener.class);
	
	@PostRemove
	public void removeImagesFromDisk(ImageResource image){
		
		if (image.getHiResImagePath() != null){
			delete(image.getHiResImagePath());
		}
		
		if (image.getLoResImagePath() != null){
			delete(image.getLoResImagePath());
		}
	}
	
	
	private void delete(String path){
		BuenOjoEnvironment env = BeanUtil.getBean(BuenOjoEnvironment.class);
		String basePath = env.getGameResourcesBasePath();
		Path filePath = Paths.get(basePath, path);
		Boolean result = filePath.toFile().delete();
		if (!result){
			log.info("No se pudo eliminar el archivo: "+filePath);
		}
		
	}
}