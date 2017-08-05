package com.ciis.buenojo.domain.parsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import static org.assertj.core.api.Assertions.assertThat;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class PhotoLocationLandscapeLevelsCSVParserTest {

	@Test
	public void testKeywordParsing() {
		
		String  level = "\"\",\"Niveles\",\"Paisaje\"\n\"1\",\"5:20\",\"Colinas,Llano,Veg media,Veg escasa\"";
		InputStreamSource source =new ByteArrayResource(level.getBytes(StandardCharsets.UTF_8));
		PhotoLocationLandscapeLevelsCSVParser parser = new PhotoLocationLandscapeLevelsCSVParser(source);
		
		try {
			List<String> keywords = parser.parseKeywords();
			assertThat(keywords).isNotNull();
			assertThat(keywords.size()).isEqualTo(4);
			
		} catch (IOException | BuenOjoCSVParserException e) {
			fail(e.getMessage());
			
		}
		
	}
	@Test
	public void testLandscapeLevelParsing() {
		String  level = "\"\",\"Niveles\",\"Paisaje\"\n\"1\",\"5:20\",\"Colinas,Llano,Veg media,Veg escasa\"";
		InputStreamSource source =new ByteArrayResource(level.getBytes(StandardCharsets.UTF_8));
		PhotoLocationLandscapeLevelsCSVParser parser = new PhotoLocationLandscapeLevelsCSVParser(source);
		 try {
			Integer[] levels = parser.parseLevels();
			assertThat(levels).isNotNull();
			assertThat(levels.length).isEqualTo(2);
			assertThat(levels[0]).isNotNull().isEqualTo(5);
			assertThat(levels[1]).isNotNull().isEqualTo(20);
			
		} catch (BuenOjoCSVParserException | IOException e) {

			fail(e.getMessage());
			
		}
		 
		 
	}

}
