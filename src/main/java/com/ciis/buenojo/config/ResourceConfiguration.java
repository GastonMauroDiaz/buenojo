package com.ciis.buenojo.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ciis.buenojo.domain.util.BuenOjoFileUtils;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;

import org.slf4j.Logger;


@Configuration
public class ResourceConfiguration extends WebMvcConfigurerAdapter {

	@Inject
	private BuenOjoEnvironment env;

	private static final Logger log = LoggerFactory.getLogger(ResourceConfiguration.class);
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("configuring resource handlers");
		String fullPath = env.getGameResourcesAbsoluteFilepath() + "/";
		log.info("game-resources full path:"+fullPath);
		
		log.debug("configuring resource handlers");
		log.debug(" configured resource:" + fullPath);
		
        registry.addResourceHandler(BuenOjoFileUtils.GAME_RESOURCES_RECURSIVE_PATH).addResourceLocations(fullPath);
        
        log.info("FTP Path: "+env.getFTPAbsolutePath());

    }

}
