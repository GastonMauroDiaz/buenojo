package com.ciis.buenojo.customLoaders.helpers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.ciis.buenojo.domain.util.BuenOjoFileUtils;

public class ResourceHelper {

	private static Logger log = LoggerFactory.getLogger(ResourceHelper.class);
	public static final String getResourceAddress(boolean isFromGameResourceInput, String... fileResourcePathComponents)
	{
		if (isFromGameResourceInput)
		{
			final String[] inputGameResourceArray = {BuenOjoFileUtils.GAME_RESOURCES_INPUT_DIR};
			fileResourcePathComponents = (String[]) ArrayUtils.addAll(inputGameResourceArray, fileResourcePathComponents);
		}	
		return getResourceAddress(fileResourcePathComponents);
	}
	public static final String getResourceAddress(String... fileResourcePathComponents)
	{
		final StringJoiner sjPath;
		sjPath= new StringJoiner("/");
		for (String fileResourcePathComponent : fileResourcePathComponents)
			sjPath.add(fileResourcePathComponent);
		return sjPath.toString();
	}

	public static final String getGameResourcePath()
	{
		return (String.format("/%s", BuenOjoFileUtils.GAME_RESOURCES_DIR));

	}

	public static final String getGameResourceInputPath()
	{
		return (String.format("%s", BuenOjoFileUtils.GAME_RESOURCES_INPUT_DIR));

	}

	public static final URL getResource(final boolean isFromGameResourceInput,String... fileResourcePathComponents)
	{
		if (isFromGameResourceInput)
		{
			final String[] inputGameResourceArray = {BuenOjoFileUtils.GAME_RESOURCES_INPUT_DIR};
			fileResourcePathComponents = (String[]) ArrayUtils.addAll(inputGameResourceArray, fileResourcePathComponents);

		}	
		return getResource(fileResourcePathComponents);
	}

	public static final URL getResource(String... fileResourcePathComponents)
	{
		URL output = null;
		final String resourceName;
		try{
			resourceName =  getResourceAddress(fileResourcePathComponents);
			log.info(resourceName);

			output = ResourceHelper.class.getResource("/"+resourceName);
			log.info("llego 6");

			log.info(output.toString());

		}
		catch (Exception e)
		{
			log.error("Fail",e);

		}
		return output;
	}

	public static List<String> listWithoutPrefix(List<String> listWithPrefix,  Logger log)
	{
		final List<String> listWithoutPrefix;
		listWithoutPrefix= new ArrayList<String>();
		for (String resourcePath : listWithPrefix)
		{
			if (resourcePath.length()>1)
				if (resourcePath.startsWith("/"))
					listWithoutPrefix.add(resourcePath.substring(1));
				else
					log.warn(String.format("Invalid path %s", resourcePath));
			else
				log.warn(String.format("Empty path %s", resourcePath));
		}
		return listWithoutPrefix;
	}

	public static List<String> listWithoutPath(List<String> listWithPrefix, String fixedPath,  Logger log)
	{
		final List<String> listWithoutFixedPath;
		listWithoutFixedPath= new ArrayList<String>();
		for (String resourcePath : listWithPrefix)
		{
			if (StringUtils.isNotEmpty(resourcePath))
				if (resourcePath.startsWith(fixedPath))
					listWithoutFixedPath.add(resourcePath.substring(fixedPath.length()));
				else
					log.warn(String.format("Invalid starting path %s, expected to begin with %s", resourcePath, fixedPath));
			else
				log.warn(String.format("Empty path %s", resourcePath));
		}
		return listWithoutFixedPath;
	}

	public static List<String> 	listWithoutResourcePathAndFixedPath(List<String> listWithPrefix, String fixedPath,  Logger log)
	{
		final List<String> listWithoutFixedPath;
		listWithoutFixedPath= new ArrayList<String>();
		for (String resourcePath : listWithPrefix)
		{
			if (StringUtils.isNotEmpty(resourcePath))
				if (resourcePath.contains(fixedPath))
				{
					final int beginIndex;
					beginIndex = resourcePath.indexOf(fixedPath) + fixedPath.length();
					listWithoutFixedPath.add(resourcePath.substring(beginIndex));
				}
				else
					log.warn(String.format("Invalid  path %s, expected to container [%s]", resourcePath, fixedPath));
			else
				log.warn(String.format("Empty path %s", resourcePath));
		}
		return listWithoutFixedPath;
	}

	public static void validateResource(String resourcePath,Logger log) 
	{ 

		ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		try {
			if (resourceResolver.getResources("classpath*:/GameData" +resourcePath+"*").length==0)
				log.error("INEXISTENT RESOURCE "+"/GameData" +resourcePath+"*");
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
	}


}
