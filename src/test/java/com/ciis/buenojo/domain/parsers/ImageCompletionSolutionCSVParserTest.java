package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPair;

import static org.assertj.core.api.Assertions.assertThat;
public class ImageCompletionSolutionCSVParserTest {
	
	private static List<Tag> tags;
	@Before
	public void loadTags () {
		ArrayList<Tag> list = new ArrayList<>();
		Tag tag = new Tag();
		tag.setId(1001L);
		tag.setNumber(1);
		tag.setName("etiqueta 1");
		list.add(tag);
		
		tag = new Tag();
		tag.setId(1002L);
		tag.setNumber(19);
		tag.setName("etiqueta 19");
		list.add(tag);
		
		tag = new Tag();
		tag.setId(1003L);
		tag.setNumber(20);
		tag.setName("etiqueta 20");
		list.add(tag);
		
		tag = new Tag();
		tag.setId(1004L);
		tag.setNumber(26);
		tag.setName("etiqueta 26");
		list.add(tag);
		
		tag = new Tag();
		tag.setId(1005L);
		tag.setNumber(30);
		tag.setName("etiqueta 30");
		list.add(tag);
		
		tags = list;
	}
	@Test
	public void testSolutionParser () {
		String tagPairList = "\"id\",\"etiqueta\"\n\"0\",\"1\"\n\"1\",\"20\"\n\"2\",\"26\"\n\"3\",\"19\"\n\"4\",\"30\"";
		InputStreamSource source =new ByteArrayResource(tagPairList.getBytes(StandardCharsets.UTF_8));
		
		ImageCompletionSolutionCSVParser parser = new ImageCompletionSolutionCSVParser(source, tags);
		
		try {
			List<TagPair> tagPair = parser.parse();
			assertThat(tagPair).isNotNull();
			assertThat(tagPair.size()).isEqualTo(5);
			
		} catch (IOException e) {
			Fail.fail(e.getMessage());
			
		}
		
		
		
		
	}
}
