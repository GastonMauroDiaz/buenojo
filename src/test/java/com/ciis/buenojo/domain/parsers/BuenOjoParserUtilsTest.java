package com.ciis.buenojo.domain.parsers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;



public class BuenOjoParserUtilsTest {

	@Test
	public void test() {
		String rawString = "Colinas,Herb/subarbustivo";
		String rawSpacedString = "  Colinas , Herb/subarbustivo  ";
		String expected1 = "Colinas";
		String expected2 = "Herb/subarbustivo";
		String [] array = BuenOjoParserUtils.keywordStringListToArray(rawString);
		String [] arraySpaced = BuenOjoParserUtils.keywordStringListToArray(rawSpacedString);
		assertThat(array).isNotNull();
		assertThat(array.length).isEqualTo(2);
		assertThat(array[0]).isEqualTo(expected1);
		assertThat(array[1]).isEqualTo(expected2);
		
		assertThat(arraySpaced[0]).isEqualTo(expected1);
		assertThat(arraySpaced[1]).isEqualTo(expected2);
		

	}

}
