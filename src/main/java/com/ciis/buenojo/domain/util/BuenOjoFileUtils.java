package com.ciis.buenojo.domain.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.enumeration.ExerciseType;
import com.ciis.buenojo.exceptions.BuenOjoDataSetException;

public class BuenOjoFileUtils {

	
	public static final String GAME_RESOURCES_DIR			 = "game-resources";
	public static final String GAME_RESOURCES_INPUT_DIR			 = "game-resources-input";
	public static final String GAME_DATA_DIR						= "GameData";
	public static final String IMAGE_RESOURCE_DIR			 = "imageResources";
	public static final String SATELLITE_IMAGE_DIR			 = "satelliteImages";
	public static final String GAME_RESOURCES_PATH			 = GAME_RESOURCES_DIR;
	public static final String IMAGE_RESOURCE_PATH 			 = GAME_RESOURCES_PATH + "/"+ IMAGE_RESOURCE_DIR;
	public static final String SATELLITE_IMAGE_PATH 		 = GAME_RESOURCES_PATH + "/" + SATELLITE_IMAGE_DIR;
	public static final String GAME_RESOURCES_RECURSIVE_PATH = "/" + GAME_RESOURCES_PATH +  "/**";
	public static final String HI_RES_SUFFIX				 = "_hi_res";
	public static final String CSV_EXTENSION		= "csv";
	public static final String HANG_MAN_DIR 		= "HangMan";
	public static final String PHOTO_LOCATION_DIR 	= "PhotoLocation";
	public static final String IMAGE_COMPLETION_DIR = "ImageCompletion";
	public static final String MULTIPLE_CHOICE_DIR 	= "MultipleChoice";
	

	/**
	 * returns the corresponding extension for the jpeg, png, gif and tiff  MIMETypes
	 * returns same string if contentType is not well formed or not image type
	 * @param contentType MIMEType for images
	 * @return
	 */
	public static String extensionFromContentType(String contentType) {
		switch(contentType.toLowerCase()){
			case "image/jpeg":
			case "image/jpg":
				return "jpg";
			case "image/png":
				return "png";
			case "image/gif":
				return "gif";
			case "image/tiff":
				return "tiff";
			default:
				return contentType;
		}
	}
	
	public static Optional<String> contentType(Path path) {
		
		String extension = FilenameUtils.getExtension(path.getFileName().toString());
		
		switch (extension.toLowerCase()) {
		case "jpeg":
			return Optional.of("image/jpeg");
		case "jpg":
			return Optional.of("image/jpg");
		case "png":
			return Optional.of("image/png");
		case "gif":
			return Optional.of("image/gif");
		case "tif":
		case "tiff":
			return Optional.of("image/tiff");
		case "csv":
			return Optional.of("text/csv");
		default:			
			return Optional.empty();
		}
		
	}
	
	public static ExerciseType exerciseTypeFromPath(Path path){
		for (Iterator<Path> iterator = path.iterator(); iterator.hasNext();) {
			 Path p = iterator.next();
			 
			 ExerciseType type = null;
			 if (dataSetFolderList.contains(p.toString())){
				 type = ExerciseType.valueOf(p.toString());
			 }
			 
			 if (type != null){
				 return type;
			 }
		}
		return null;
	}
	private static final String[] dataSetFolderArray = {HANG_MAN_DIR,IMAGE_COMPLETION_DIR,MULTIPLE_CHOICE_DIR,PHOTO_LOCATION_DIR};
	private static final List<String> dataSetFolderList =  Arrays.asList(dataSetFolderArray);
	
	public static String datasetFolderList(){
		StringBuffer b = new StringBuffer();
		for (String string : dataSetFolderArray) {
			b.append(string);
			b.append(" ");
		}
		return b.toString();
	}
	
	public static boolean isExerciseFolder(Path p) {
		return p.toFile().isDirectory() && dataSetFolderList.contains(p.getFileName().toString()); 
	}
	
	public static MultipartFile multipartFile(Path filePath) throws BuenOjoDataSetException, IOException {
		String name = filePath.getFileName().toString();
		String originalFileName = name;
		String contentType = BuenOjoFileUtils.contentType(filePath).orElseThrow(() -> new BuenOjoDataSetException("la ruta "+filePath+"no contiene un tipo válido de imagen para buen ojo"));
		byte[] content = Files.readAllBytes(filePath);
		return new MockMultipartFile(name,
                originalFileName, contentType, content);
	}
	
	
	
}
