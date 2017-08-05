package com.ciis.buenojo.customLoaders.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.Normalizer;

import javax.inject.Inject;


import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;

public class FileManagementHelper {
	@Inject
	private static Environment env;
	private static String baseDirectory;
	
	private static String assetInjectorDirectory;


	public static String normalizeFileName(String fileName)
	{
		return Normalizer.normalize(fileName, Normalizer.Form.NFD);
	}


	public static boolean copyTo(final File srcFile,final File destFile,final Logger log)
	{
		boolean output=false;
		try {
		log.info("source:" + srcFile);
		log.info("dest:" + destFile);
			FileUtils.copyFile(srcFile, destFile);
			output=true;
		}
		catch (IOException e)
		{
			log.error("File copy fail", e);
		}
		return output;

	}

	public static String getBaseDirectory()
	{
	     if (baseDirectory==null)
	     {
	    	/* try {
				baseDirectory = new File (".").getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	    	 baseDirectory=System.getProperty("user.dir");
	     }
	      return baseDirectory;

	}

	public static boolean copyTo(final String srcFile,final String destFile,final Logger log)
	{
		final File src;
		final File dest;
		boolean output=false;
		try {
			src = new File(FileManagementHelper.class.getResource(srcFile).toURI());
            dest=  new File(getAssetInjectorDirectory()+"/"+destFile);
            output= copyTo(src, dest, log);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			log.error("Copy Fail", e);
		}
			return output;

	}


	public static void setBaseDirectory(String baseDirectory) {
		FileManagementHelper.baseDirectory = baseDirectory;
	}


	public static String getAssetInjectorDirectory() {
		if (assetInjectorDirectory == null){
			if (env!=null)
			assetInjectorDirectory = env.getProperty("buenojo.gameResourcesBasePath");
			else
				assetInjectorDirectory = "/home/ezequiel/TexxomProjectsWorkspaces/buenOjo/src/main/webapp";
		}
		return assetInjectorDirectory;
	}


	public static void setAssetInjectorDirectory(String assetInjectorDirectory) {
		FileManagementHelper.assetInjectorDirectory = assetInjectorDirectory;
	}


}
