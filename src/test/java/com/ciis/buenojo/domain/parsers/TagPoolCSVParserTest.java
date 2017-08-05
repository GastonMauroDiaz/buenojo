package com.ciis.buenojo.domain.parsers;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.assertj.core.api.Fail;
import org.springframework.core.io.ByteArrayResource;


import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

import org.junit.*;
import static org.assertj.core.api.Assertions.assertThat;


public class TagPoolCSVParserTest {
	
	private static final String  csvString  = "Etiqueta,Etiqueta similar 1,Etiqueta similar 2,Etiqueta similar 3\nAgua,Humedal/Mallín,Erial,\nAlerce,Ciprés,Mixto alto,Coihue\nHumedal/Mallín,Agua,Erial,\nCiprés,Alerce,,\nErial,Agua,,\nMixto alto,Ciprés,,\nCoihue,Mixto Alto,,";
	@Test
	public void testTagPoolParser () throws BuenOjoCSVParserException {

		TagPoolCSVParser parser = new TagPoolCSVParser(new ByteArrayResource(csvString.getBytes(StandardCharsets.UTF_8)));
		Course course = new Course();
		course.setId(new Long(1000));
		try {
			List<Tag> tags = parser.parse(course);
			assertThat(tags).isNotNull().isNotEmpty();
			assertThat(tags.size()).isNotZero().isEqualTo(7);
			assertThat(tags.get(0).getNumber()).isEqualTo(1);
			assertThat(tags.get(1).getNumber()).isEqualTo(2);
			List<TagPool> tagPool = parser.parseTagPool();
			assertThat(tagPool).isNotNull().isNotEmpty();
			assertThat(tagPool.size()).isNotZero().isEqualTo(11);
			
		} catch (IOException e) {
		
			Fail.fail(e.getMessage());
		}
		
		
		
	}
}
