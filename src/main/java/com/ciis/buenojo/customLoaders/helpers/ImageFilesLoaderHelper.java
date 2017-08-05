package com.ciis.buenojo.customLoaders.helpers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import org.slf4j.Logger;

public class ImageFilesLoaderHelper {
	public static ArrayList<String> getImageResources(String pattern, String resourcePath,Integer dept, Logger log ){
		final URI uri;
		final ArrayList<String> resourceList;
		final PathMatcher matcher;
		final Path myPath;
		final Stream<Path> walk; 

		resourceList= new ArrayList<String>();
		matcher = FileSystems.getDefault().getPathMatcher(pattern);
		try {
			log.info("PATH:"+resourcePath);
			uri = ImageFilesLoaderHelper.class.getResource("/"+resourcePath).toURI();
			log.info(uri.toString());
			if (uri.getScheme().equals("jar")) {
				final FileSystem fileSystem;
				fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
				myPath = fileSystem.getPath(resourcePath);
			} else {
				myPath = Paths.get(uri);
			}
	    	walk = Files.walk(myPath, dept);
			for (Iterator<Path> it = walk.iterator(); it.hasNext();){
		        Path currentPath =it.next(); 
		        log.info("Path2:"+currentPath.toString());
				if (matcher.matches(currentPath))
				{
					log.info(currentPath.toString());
					resourceList.add(currentPath.toString());
				}
			}
		} catch (IOException e) {
			log.error("File error", e);
		
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			log.error("URI error", e);
			}
	    return resourceList;
		
		
	}
}
