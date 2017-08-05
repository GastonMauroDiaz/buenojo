package com.ciis.buenojo.domain.parsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

import static org.assertj.core.api.Assertions.assertThat;
public class PhotoLocationSightPairCSVTest {

	@Test
	public void testParse() {
		
		
		String sightString = "\"\",\"id\",\"satCol\",\"satRow\",\"satTolerancia\",\"terCol\",\"terRow\",\"terTolerancia\"\n\"1\",1,350,825,19,1699,1025,60\n\"2\",2,513,409,25,1558,830,80\n\"3\",3,304,309,25,414,690,80";
		
		InputStreamSource source =new ByteArrayResource(sightString.getBytes(StandardCharsets.UTF_8));
		
		PhotoLocationSightPairCSVParser parser = new PhotoLocationSightPairCSVParser(source);
		
		assertThat(parser).isNotNull();
		
		try {
			List<PhotoLocationSightPair> sightPairs = parser.parse();
			assertThat(sightPairs).isNotNull();
			assertThat(sightPairs.size()).isEqualTo(3);
		} catch (IOException e) {
			
			fail(e.getMessage());
		} catch (BuenOjoCSVParserException e) {
			fail(e.getMessage());
		}
		
	}
	
	

}
