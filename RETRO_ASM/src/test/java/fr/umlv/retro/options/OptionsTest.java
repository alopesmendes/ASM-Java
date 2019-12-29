package fr.umlv.retro.options;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;



class OptionsTest {

	@Test @Tag("parameter")
	public void testParserOptionsWithNullArgs() {
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		assertThrows(NullPointerException.class, () -> ParseOptions.parse(null, optfac, map));
	}
	
	@Test @Tag("parameter")
	public void testParserOptionsWithNullFactory() {
		String test[] = {"test"};
		OptionMap map = OptionMap.initMap();
		assertThrows(NullPointerException.class, () -> ParseOptions.parse(test, null, map));
	}
	
	@Test @Tag("parameter")
	public void testParserOptionsWithNullMap() {
		String test[] = {"test"};
		OptionFactory optfac = new OptionFactory();
		assertThrows(NullPointerException.class, () -> ParseOptions.parse(test, optfac, null));
	}

}

