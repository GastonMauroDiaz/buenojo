package com.ciis.buenojo.service;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.config.BuenOjoEnvironment;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.util.BuenOjoFileUtils;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageResourceRepository;

@Service
@Transactional(rollbackOn = Exception.class)
public class ImageResourceService {

	private final Logger log = LoggerFactory.getLogger(ImageResourceService.class);
	@Inject
	public ImageResourceRepository imageRepository;

	@Inject
	private BuenOjoEnvironment env;
	public ImageResource createImageResource(MultipartFile loResImage, MultipartFile hiResImage) throws IOException, BuenOjoFileException, BuenOjoInconsistencyException {
		return createImageResource(loResImage, hiResImage, false);
	}
	
	public ImageResource createImageResource(MultipartFile loResImage, MultipartFile hiResImage, Boolean dryRun) throws IOException, BuenOjoFileException, BuenOjoInconsistencyException {
		ImageResource imageResource = new ImageResource();
		if (loResImage!=null) {
			if (imageResource.getName()==null)
				imageResource.setName(FilenameUtils.removeExtension(loResImage.getOriginalFilename()));
			imageResource.setLoResImage(loResImage.getBytes());
			imageResource.setLoResImageContentType(loResImage.getContentType());

		}
		if (hiResImage != null){
			if (imageResource.getName()==null)
				imageResource.setName(FilenameUtils.removeExtension(hiResImage.getOriginalFilename()));
			imageResource.setHiResImage(hiResImage.getBytes());
			imageResource.setHiResImageContentType(hiResImage.getContentType());

		}
		return createImageResource(imageResource,dryRun);
	}
	private String hiResFilenameForImageResource(ImageResource imageResource){
		String fileExtension = BuenOjoFileUtils.extensionFromContentType(imageResource.getHiResImageContentType());
		String fileName = FilenameUtils.removeExtension(imageResource.getName()) + BuenOjoFileUtils.HI_RES_SUFFIX +FilenameUtils.EXTENSION_SEPARATOR_STR +fileExtension;
		return fileName;
	}
	private String loResFilenameForImageResource(ImageResource imageResource){
		String fileExtension = BuenOjoFileUtils.extensionFromContentType(imageResource.getLoResImageContentType());
		String fileName = FilenameUtils.removeExtension(imageResource.getName()) +FilenameUtils.EXTENSION_SEPARATOR_STR +fileExtension;
		return fileName;
	}

	private boolean isRename(ImageResource imageResource) throws BuenOjoInconsistencyException{


		if (imageResource.getLoResImagePath()!=null){
			return !loResFilenameForImageResource(imageResource).equals(FilenameUtils.getName(imageResource.getLoResImagePath()));
		}else if(imageResource.getHiResImagePath()!=null) {
			return !hiResFilenameForImageResource(imageResource).equals(FilenameUtils.getName(imageResource.getHiResImagePath()));
		}else {
			throw new BuenOjoInconsistencyException("no se puede determinar el nombre de "+imageResource);
		}

	}
	public ImageResource updateImageResource(ImageResource updatedImageResource) throws BuenOjoFileException, BuenOjoInconsistencyException {
		
		boolean updatingHiRes = updatedImageResource.getHiResImage()!= null;
		boolean updatingLowRes = updatedImageResource.getLoResImage()!=null;


		if(isRename(updatedImageResource)){
			//move existing files

			// if image Name already exists, fail

			if (imageNameExists(updatedImageResource.getName()) == null){
				throw new BuenOjoFileException("ya existe una imagen con nombre '"+updatedImageResource.getName()+"'");
			}
			boolean hasOldHiResImage = updatedImageResource.getHiResImagePath()!= null;
			if (!updatingHiRes && hasOldHiResImage){ // rename
				String oldAbsolutePath = getAbsolutePath(updatedImageResource.getHiResImagePath());

				String newfileName = hiResFilenameForImageResource(updatedImageResource);
				String absolutePath = absolutePathForFilename(newfileName);
				moveFile(oldAbsolutePath, absolutePath);
				updatedImageResource.setHiResImagePath(relativePathForFilename(newfileName));


			} else if (updatingHiRes && hasOldHiResImage) { // create new file and remove the old one
				String oldResFile = updatedImageResource.getHiResImagePath();
				saveHiResBytesForImageResource(updatedImageResource);
				deleteRelativeImageQuietly(oldResFile);


			} else if (updatingHiRes && !hasOldHiResImage) { // just create new file

				saveHiResBytesForImageResource(updatedImageResource);
			}


			boolean hasOldLowResImage = updatedImageResource.getLoResImagePath()!=null;
			if (!updatingLowRes && hasOldLowResImage){ // move the existing image
				String oldAbsolutePath = getAbsolutePath(updatedImageResource.getLoResImagePath());

				String fileName = loResFilenameForImageResource(updatedImageResource);
				String absolutePath = absolutePathForFilename(fileName);
				moveFile(oldAbsolutePath, absolutePath);
				updatedImageResource.setHiResImagePath(relativePathForFilename(fileName));

			} else if (updatingLowRes && hasOldLowResImage) { // create new file and remove the old one
				String oldRelativePath = updatedImageResource.getLoResImagePath();

				saveLoResBytesForImageResource(updatedImageResource);

				deleteRelativeImageQuietly(oldRelativePath);

			} else if (updatingLowRes && !hasOldLowResImage) { // just create new file
				saveLoResBytesForImageResource(updatedImageResource);
			}
		} else {
			// not renaming just overwrite to the same location

			if (updatedImageResource.getHiResImage()!=null){
				saveHiResBytesForImageResource(updatedImageResource);

			}
			if (updatedImageResource.getLoResImage()!= null){
				saveLoResBytesForImageResource(updatedImageResource);
			}
		}

		ImageResource returnValue = null;

		try {
			returnValue = imageRepository.save(updatedImageResource);

		}catch (DataAccessException e){
			log.error("update error:"+ updatedImageResource);
			log.error(e.getMessage());
			throw new BuenOjoFileException(e.getMessage());

		}
		return returnValue;
	}

	private void saveLoResBytesForImageResource(ImageResource imageResource) throws BuenOjoFileException{
		String fileName = loResFilenameForImageResource(imageResource);
		String absolutePath = absolutePathForFilename(fileName);
		File newFile = new File(absolutePath);
		saveBytesToFile(imageResource.getLoResImage(), newFile);
		imageResource.setHiResImage(null);
		imageResource.setLoResImagePath(relativePathForFilename(fileName));

	}
	private void saveHiResBytesForImageResource(ImageResource imageResource) throws BuenOjoFileException{
		String newfileName = hiResFilenameForImageResource(imageResource);
		String absolutePath = absolutePathForFilename(newfileName);

		File newFile = new File(absolutePath);
		saveBytesToFile(imageResource.getHiResImage(),newFile);
		imageResource.setHiResImage(null);
		imageResource.setHiResImagePath(relativePathForFilename(newFile.getName()));
	}
	private String absolutePathForFilename(String filename){
		return FilenameUtils.concat(env.getImageResourceAbsolutePath(), filename);
	}
	private String relativePathForFilename(String filename){
		return FilenameUtils.concat(env.getImageResourceRelativePath(), filename);
	}
	private ImageResource imageNameExists(String name) {
		return imageRepository.findOneByName(name);
	}
	public ImageResource createImageResource(ImageResource imageResource) throws IOException, BuenOjoFileException, BuenOjoInconsistencyException {
		return createImageResource(imageResource, false);
	}
	
	public ImageResource createImageResource(ImageResource imageResource, Boolean dryRun) throws IOException, BuenOjoFileException, BuenOjoInconsistencyException
	{
		File newHiResFile = null;
		File newLoResFile = null;
		ImageResource existingImage = imageNameExists(imageResource.getName()); 
		if (existingImage != null){
			log.info("ya existe una imagen con el nombre "+imageResource.getName());
			return existingImage;
		}
		if (imageResource.getHiResImage() == null && imageResource.getLoResImage() == null){
			throw new BuenOjoFileException("no puede guardar un image resource nuevo que no tenga un byte array con la imagen");
		}
		if (imageResource.getHiResImage() != null){

			String fileExtension = BuenOjoFileUtils.extensionFromContentType(imageResource.getHiResImageContentType());
			String fileName = imageResource.getName();
			String completeFileName = FilenameUtils.removeExtension(fileName) + BuenOjoFileUtils.HI_RES_SUFFIX +FilenameUtils.EXTENSION_SEPARATOR_STR +fileExtension;

			String absolutePath = absolutePathForFilename(completeFileName);
			if (!dryRun){
				newHiResFile = new File (absolutePath);
				File tempFile = createTempFile(imageResource.getHiResImage(), fileExtension, fileName, completeFileName);
				//Store in absolutePath, url in relative path for tomcat

				moveFile(tempFile.getAbsolutePath(),absolutePath);
			}
			String relativePath = relativePathForFilename(completeFileName);
			imageResource.setHiResImagePath(relativePath);
			imageResource.setHiResImage(null);

		}
		if (imageResource.getLoResImage() != null) {
			String fileExtension = BuenOjoFileUtils.extensionFromContentType(imageResource.getLoResImageContentType());
			String fileName = FilenameUtils.removeExtension(imageResource.getName());
			String completeFileName = fileName  +FilenameUtils.EXTENSION_SEPARATOR_STR +fileExtension;

			String absolutePath = absolutePathForFilename(fileName);

			if (!dryRun) {
				File tempFile = createTempFile(imageResource.getLoResImage(), fileExtension, fileName, completeFileName);
				//attempt to move it
				moveFile(absolutePath, tempFile.getAbsolutePath());
			}
			
			String relativePath = FilenameUtils.concat(env.getImageResourceRelativePath(), fileName);
			imageResource.setLoResImagePath(relativePath);
			imageResource.setLoResImage(null);
		}
		ImageResource returnValue = null;

		try {
			returnValue = imageRepository.save(imageResource);

		}catch (DataAccessException e) {
			// save failed roll back file system
			if (shouldDeleteNewFile(newHiResFile)) FileUtils.forceDelete(newHiResFile);
			if (shouldDeleteNewFile(newLoResFile)) FileUtils.forceDelete(newLoResFile);
			throw new BuenOjoInconsistencyException(e.getMessage());
		}
		return returnValue;
	}

	private File createTempFile(byte[] image, String fileExtension, String fileName,
			String completeFileName) throws IOException, BuenOjoFileException {
		File newTempFile =File.createTempFile(fileName, fileExtension);

		//write temp file
		try {
			FileUtils.writeByteArrayToFile(newTempFile, image);
		} catch (IOException e) {
			//clean up previous file
			log.error(e.getMessage());
			String message = "no se puedo guardar archivo temporal para el archivo:"+completeFileName;
			log.error(message);
			throw new BuenOjoFileException(message);

		}
		return newTempFile;
	}

	private void saveBytesToFile(byte[] bytes, File newFile) throws BuenOjoFileException {

		try {
			FileUtils.writeByteArrayToFile(newFile, bytes);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new BuenOjoFileException("no se pudo crear el archivo nuevo :"+ newFile.getAbsolutePath());
		}
	}

	private boolean shouldDeleteNewFile(File newFile) {
		return newFile!=null;
	}
	private String getAbsolutePath(String relativePath){

		return FilenameUtils.concat(env.getGameResourcesBasePath(), relativePath);
	}
	private void moveFile(String currentAbsolutePath, String newAbsolutePath) throws BuenOjoFileException{
		try {
			FileUtils.moveFile(new File(currentAbsolutePath), new File(newAbsolutePath));
		}  catch (FileExistsException fe){
			log.error(fe.getMessage());
			throw new BuenOjoFileException("no se pudo mover el archivo: " +currentAbsolutePath+" al destino"+ newAbsolutePath + " porque ese archivo ya existe");
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new BuenOjoFileException("no se pudo mover el archivo: " +currentAbsolutePath+" al destino: "+ newAbsolutePath + e.getMessage());
		}
	}
	private void deleteRelativeImageQuietly(String relativePath){
		File imageFile = new File(getAbsolutePath(relativePath));
		FileUtils.deleteQuietly(imageFile);

	}
	private void deleteImageFile(String path){
		try{
			FileUtils.forceDelete(new File(path));

		} catch(IOException e){
			log.error(e.getMessage());
		}

	}
	public void deleteImageResource(Long imageId) throws IOException {
		ImageResource image = imageRepository.findOne(imageId);
		if (image.getHiResImagePath() != null) {

			String filename = FilenameUtils.getName(image.getHiResImagePath());
			String absolutePath = FilenameUtils.concat(env.getImageResourceAbsolutePath(), filename);
			deleteImageFile(absolutePath);

			image.setHiResImagePath(null);
			image.setHiResImageContentType(null);

		}
		if (image.getLoResImagePath() != null) {

			String filename = FilenameUtils.getName(image.getLoResImagePath());
			String absolutePath = FilenameUtils.concat(env.getImageResourceAbsolutePath(), filename);
			deleteImageFile(absolutePath);
			image.setLoResImagePath(null);
			image.setLoResImageContentType(null);

		}

		imageRepository.delete(image);
	}


}
