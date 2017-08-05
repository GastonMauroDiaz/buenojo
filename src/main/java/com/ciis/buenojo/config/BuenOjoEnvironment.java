package com.ciis.buenojo.config;

import java.io.File;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.util.BuenOjoFileUtils;

@Service
public class BuenOjoEnvironment {

	@Inject	
	private Environment env;
	/**
	 * {@code return new File(IMAGE_RESOURCE_PATH) }
	 * @return
	 */
	public String getImageResourceAbsoluteFilepath() {
		return getGameResourcesAbsoluteFilepath()+"/"+BuenOjoFileUtils.IMAGE_RESOURCE_DIR;
	}
	public String getImageResourceAbsolutePath(){
		
		return FilenameUtils.concat(getGameResourcesAbsolutePath(), BuenOjoFileUtils.IMAGE_RESOURCE_DIR);
	}
	public String getGameResourcesRelativePath() {
		return BuenOjoFileUtils.GAME_RESOURCES_DIR;
	}
	
	public String getImageResourceRelativePath() {
		return BuenOjoFileUtils.IMAGE_RESOURCE_PATH;
	}
	
	
	public String getGameResourcesAbsoluteFilepath() {
		return "file:"+ getGameResourcesAbsolutePath();
	}
	public String getGameResourcesAbsolutePath(){
		return env.getProperty("buenojo.gameResourcesBasePath")+"/"+env.getProperty("buenojo.gameResourcesFolder");
	}
	public String getGameResourcesBasePath(){
		return env.getProperty("buenojo.gameResourcesBasePath");
	}
	
	public String getFTPAbsolutePath(){
		return env.getProperty("buenojo.ftp.location");
	}
}
	