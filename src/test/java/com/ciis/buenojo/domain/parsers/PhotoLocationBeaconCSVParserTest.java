package com.ciis.buenojo.domain.parsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationBeacon;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

import static org.assertj.core.api.Assertions.assertThat;
public class PhotoLocationBeaconCSVParserTest {

	@Test
	public void testParse() {
		
		
		String beaconCSV = "\"\",\"col\",\"row\",\"\"\n\"1\",290,949,10";
		InputStreamSource source =new ByteArrayResource(beaconCSV.getBytes(StandardCharsets.UTF_8));
		PhotoLocationBeaconCSVParser parser = new PhotoLocationBeaconCSVParser(source);
		
		PhotoLocationBeacon beacon = null ;
		try {
			beacon = parser.parse();
		} catch (IOException e) {

			fail(e.getMessage());
		} catch (BuenOjoCSVParserException e) {

			fail(e.getMessage());
		}
		
		assertThat(beacon).isNotNull();
		
		
		
		
	}
	@Test
	public void testParseMultiBeacon() {
		
		String beaconCSV = "\"\",\"col\",\"row\",\"\"\n\"1\",290,949,10\n\"2\",290,949,10";
		InputStreamSource source =new ByteArrayResource(beaconCSV.getBytes(StandardCharsets.UTF_8));
		PhotoLocationBeaconCSVParser parser = new PhotoLocationBeaconCSVParser(source);
		
		try {
			parser.parse();
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (BuenOjoCSVParserException e) {
			assertThat(e.getMessage()).isEqualTo("El archivo contiene más de un indicador");
		}
	}
}
