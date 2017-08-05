package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.ExerciseTip;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.enumeration.Region;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.ImageResourceRepository;
import com.ciis.buenojo.repository.TagRepository;

public class ImageCompletionTipCSVParser {

	private static final String TIP_HEADER 		= "Tip"; 
	private static final String REGION_HEADER 	= "Región";
	private static final String TAG_HEADER 		= "Etiqueta";
	private static final String PHOTOS_HEADER 	= "¿qué foto?";
	private static final String IMAGE_TYPE 		= "Imagen";
	 
	
	private ImageResourceRepository imageRepository;
	
	private TagRepository			tagRepository;
	
	private InputStreamSource 		inputStreamSource;
	private Course 					course;
	
	private final Logger log = LoggerFactory.getLogger(ImageCompletionTipCSVParser.class);
	
	
	
	public ImageCompletionTipCSVParser (InputStreamSource source, Course course, TagRepository tagRepository, ImageResourceRepository imageRepository){
		this.inputStreamSource = source;
		this.course = course;
		this.tagRepository = tagRepository;
		this.imageRepository = imageRepository;
	
	}
	
	
	public List<ExerciseTip> parse() throws BuenOjoCSVParserException{
		ArrayList<ExerciseTip> tipList = new ArrayList<>();
		CSVParser parser = null;
		List<CSVRecord> recordList =null;
		List<Tag> tagList = tagRepository.findByCourseOrderByNumber(this.course);
		
		HashMap<String,Tag> tagMap = new HashMap<>(tagList.size());
		
		
		for (Tag tag : tagList) {
			tagMap.put(tag.getName(), tag);
		}
		
		
		try {
			parser = CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
			recordList = parser.getRecords();
		} catch (IOException e) {
			log.debug("Error parsing tips:"+e.getMessage());
			throw new BuenOjoCSVParserException(e.getMessage());
			
		}
		
		for (CSVRecord csvRecord : recordList) {
			ExerciseTip tip			= new ExerciseTip();
			String tipDetail		= csvRecord.get(TIP_HEADER);
			String region 			= csvRecord.get(REGION_HEADER);
			String tagName 			= csvRecord.get(TAG_HEADER);
			String imageType 		= csvRecord.get(IMAGE_TYPE);
			String photoArrayString = csvRecord.get(PHOTOS_HEADER);
			
			
			tip.setDetail(tipDetail);
			
			EnumSet<Region> regions = stringToRegions(region);
			tip.setRegions(regions);
			
			Tag theTag = tagMap.get(tagName);
			if (theTag == null ) throw new BuenOjoCSVParserException("Could not find tag with name: ["+tagName+"]");
			tip.setTag(theTag);
			
			EnumSet<SatelliteImageType> imageTypeSet = typeFromDescription(imageType.trim());
			tip.setImageTypes(imageTypeSet);
			
			for (String name : stringToImageNames(photoArrayString)) {
				if (!name.isEmpty()) {
					String sanitizedName = name.trim();
					ImageResource img = imageRepository.findOneByName(sanitizedName);
					if (img==null) throw new BuenOjoCSVParserException("Could not find image with name: ["+sanitizedName+"]");
					tip.getImages().add(img);				
				}
			}
			
			tipList.add(tip);
			
		}
		
		return tipList;
	}
	
	private EnumSet<Region> stringToRegions(String regionString){
		String[] regionStrings = regionString.split(",");
	
		ArrayList<Region>regions = new ArrayList<>(regionStrings.length);
		for (String regionName : regionStrings) {
			Region region = Region.valueOf(regionName.trim());
			regions.add(region);
		}
		
		EnumSet<Region> regionSet = EnumSet.copyOf(regions);
		return regionSet;
	}
	private String[] stringToImageNames(String semiColonSeparatedString) {
		String [] strings = semiColonSeparatedString.split(";");
		return strings;
	}
	
	public static EnumSet<SatelliteImageType> typeFromDescription(String description) throws BuenOjoCSVParserException  {
		
		String types[] = description.split(",");
		EnumSet<SatelliteImageType> typeSet = EnumSet.noneOf(SatelliteImageType.class);
		for (String type : types) {
			EnumSet<SatelliteImageType> subSet = enumSetFromTypeString(type.trim());
			 typeSet.addAll(subSet);
		}
		return typeSet;
	}


	private static EnumSet<SatelliteImageType> enumSetFromTypeString(String type)
			throws BuenOjoCSVParserException {
		switch(type) {
			case "Generales":
				return EnumSet.allOf(SatelliteImageType.class);
			case "Spot":
				return EnumSet.of(SatelliteImageType.SpotMS,SatelliteImageType.SpotPS);
			case "Invierno falso color":
				return EnumSet.of(SatelliteImageType.LandsatFC);
			case "Invierno color real":
				return EnumSet.of(SatelliteImageType.LandsatCR, SatelliteImageType.BingInv);
			case "Alta resolución de verano":
				return EnumSet.of(SatelliteImageType.Bing);
			case "Topografía":
				return EnumSet.of(SatelliteImageType.EsriTopo);
			default:
				throw new BuenOjoCSVParserException("Could not parse Satellite image type from description: ["+type+"]");
				
		}
	}
}
