package com.ciis.buenojo.domain.factories;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;
import com.ciis.buenojo.domain.parsers.SatelliteImageMetadataParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.InvalidSatelliteImageType;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.service.ImageResourceService;


@Service
@Transactional
public class SatelliteImageFactory {

	@Inject
	private ImageResourceService imageResourceService;

	public SatelliteImage imageFromFile(MultipartFile image, MultipartFile csvMetadataFile) throws BuenOjoCSVParserException, InvalidSatelliteImageType, IOException, BuenOjoFileException, BuenOjoInconsistencyException { 
		return imageFromFile(image, csvMetadataFile, false);
	}
	
	public SatelliteImage imageFromFile(MultipartFile image, MultipartFile csvMetadataFile, Boolean dryRun) throws BuenOjoCSVParserException, InvalidSatelliteImageType, IOException, BuenOjoFileException, BuenOjoInconsistencyException {
		SatelliteImageMetadataParser parser = new SatelliteImageMetadataParser(csvMetadataFile);

		List<Map<String,String>> list = parser.parse();
		String fileName = csvMetadataFile.getOriginalFilename();

		if (list.size() > 1) {
			throw new IllegalArgumentException("More than one image metadata found. use imagesFromFile() instead");
		}else if(list.size() ==  0) {
			throw new BuenOjoCSVParserException("Empty CSV file");
		}

		Map<String,String> metadata = list.get(0);
		SatelliteImage satelliteImage = new SatelliteImage();

		satelliteImage.setName(FilenameUtils.getBaseName(fileName));
		ImageResource imageResource = imageResourceService.createImageResource(null, image, dryRun);

		satelliteImage.setImage(imageResource);
		addMetadataToSatelliteImage(metadata, satelliteImage);

		addImageTypeFromName(fileName, satelliteImage);


		return satelliteImage;
	}


	/**
	 * El nombre de las imágenes tiene la siguiente plantilla: prefijo_#_RE#.tif.
	 * Donde el primer # sirve para relacionar los Metadatos-de-las-imágenes-satelitales
	 * con las imágenes en sí, mientras que el segundo #, antecedido por “RE”, se refiere al resolución espacial.
	 * Los datos que se descargan de internet, como bing, solamente tienen prefijo y RE.
	 * @param fileName
	 * @param satelliteImage
	 * @throws InvalidSatelliteImageType
	 */
	private void addImageTypeFromName(String fileName, SatelliteImage satelliteImage) throws InvalidSatelliteImageType {


		String name = FilenameUtils.getBaseName(fileName);
		String[]components = name.split("_");

		String imageTypeString = components[0];
		SatelliteImageType imageType = SatelliteImageType.typeFromString(imageTypeString);

		satelliteImage.setImageType(imageType);


	}

	private void addMetadataToSatelliteImage(Map<String,String> metadata, SatelliteImage image) {
		Double meters = new Double(metadata.get("metros").trim());
		Double lon 	  = new Double(metadata.get("lon").trim());
		Double lat	  = new Double(metadata.get("lat").trim());
		Double res	  = new Double(metadata.get("resolucion").trim());
		String copy	  = metadata.get("copy");


		image.setCopyright(copy);
		image.setLat(lat);
		image.setLon(lon);
		image.setResolution(res);
		image.setMeters(meters);

	}

}
