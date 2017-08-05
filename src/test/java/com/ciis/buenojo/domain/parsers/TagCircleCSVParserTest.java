package com.ciis.buenojo.domain.parsers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.assertj.core.api.Fail;
import org.assertj.core.data.Offset;
import org.junit.*;
import static org.assertj.core.api.Assertions.assertThat;
import com.ciis.buenojo.domain.TagCircle;

/**
 * Class for testing {@link TagCircleCSVParser}
 * @author franciscogindre
 *
 */
public class TagCircleCSVParserTest {
	
	
	private static String csvString = "\"id\",\"col\",\"row\",\"radioPx\"\n0,431,197,73.72\n1,344,317,29.49";
	@Test
	public void testTagCircleParse() {
		
		InputStream in = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.UTF_8));
		TagCircleCSVParser parser = new TagCircleCSVParser(in);
		List<TagCircle> list = null;
		try {
			list = parser.parse();
		}
		catch (Exception e) {
			assertThat(e).isInstanceOf(IOException.class);
			Fail.fail(e.getMessage());
		}
		assertThat(list).isNotEmpty();
		assertThat(list).isNotNull();
		assertThat(list.size()).isEqualTo(2);
		assertThat(list.get(0).getNumber()).isEqualTo(0);
		assertThat(list.get(0).getX()).isEqualTo(431);
		assertThat(list.get(0).getY()).isEqualTo(197);
		assertThat(list.get(0).getRadioPx()).isCloseTo(73.72f, Offset.offset(0.1f));
		
		assertThat(list.get(1).getNumber()).isEqualTo(1);
		
		assertThat(list.get(1).getX()).isEqualTo(344);
		assertThat(list.get(1).getY()).isEqualTo(317);
		assertThat(list.get(1).getRadioPx()).isCloseTo(29.49f, Offset.offset(0.1f));
		
 
	}
	

}
