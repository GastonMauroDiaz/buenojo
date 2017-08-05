package com.ciis.buenojo.domain.parsers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import static org.assertj.core.api.Assertions.assertThat;
import com.ciis.buenojo.domain.PhotoLocationKeyword;

public class PhotoLocationExtraPhotosKeywordCSVParserTest {
	
	private String expectedText;
	private PhotoLocationKeyword bosque;
	private PhotoLocationKeyword montanias;
	private PhotoLocationKeyword colinas; 
	private PhotoLocationKeyword arbustivo;
	@Before 
	public void sdetUp() {
		this.expectedText = "\"\",\"imagen\",\"keywords\"\n\"1\",\"DSC00305.txt\",\"Bosque,Montañas\"\n\"2\",\"DSC00498.txt\",\"Bosque,Colinas\"\n\"3\",\"DSC00520.txt\",\"Colinas,Herb/subarbustivo\"\n";
		 bosque = new PhotoLocationKeyword();
		bosque.setName("Bosque");
		 montanias = new PhotoLocationKeyword();
		montanias.setName("Montañas");
		 colinas = new PhotoLocationKeyword();
		colinas.setName("Colinas");
		 arbustivo = new PhotoLocationKeyword();
		arbustivo.setName("Herb/subarbustivo");
		
  	}
	@Test
	public void test() {
		
		InputStreamSource source = new ByteArrayResource(this.expectedText.getBytes(StandardCharsets.UTF_8));
		assertThat(source).isNotNull();
		PhotoLocationExtraPhotosKeywordCSVParser parser = new PhotoLocationExtraPhotosKeywordCSVParser(source);
		
		assertThat(parser).isNotNull();
		Map<String, List<String>> parsedMap = null;
		try {
			parsedMap = parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("exception on parse: "+ e.getMessage());
		}
		assertThat(parsedMap).isNotNull();
		
		assertThat(parsedMap.size()).isEqualTo(3);
		assertThat(parsedMap.keySet()).contains("DSC00305.txt","DSC00498.txt","DSC00520.txt");
		List<String> photo1 = parsedMap.get("DSC00305.txt");
		assertThat(photo1.size()).isEqualTo(2);
		assertThat(photo1.get(0)).isNotNull();
		assertThat(photo1.get(0)).isEqualTo(bosque.getName());
		assertThat(photo1.get(1)).isNotNull();
		assertThat(photo1.get(1)).isEqualTo(montanias.getName());
	}
	
	@After 
	public void tearDown () {
		this.expectedText = null;
	}
}
