package fr.umlv.retro.options;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	
	
	@Test @Tag("target")
	public void testParserOptionsWithTarget() {
		String test[] = {"-Target","12","--force"};
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		int res = ParseOptions.parse(test, optfac, map).size();
		assertEquals(1,  res);
	}
	
	@Test @Tag("infos")
	public void testParserOptionsWithInfos() {
		String test[] = {"-Infos"};
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		int res = ParseOptions.parse(test, optfac, map).size();
		assertEquals(1,  res);
	}
	
	@Test @Tag("help")
	public void testParserOptionsWithHelp() {
		String test[] = {"-Help"};
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		int res = ParseOptions.parse(test, optfac, map).size();
		assertEquals(1,  res);
	}

	@Test @Tag("features")
	public void testParserOptionsWithFeatures() {
		String test[] = {"-Features", "TryWithRessources," ,"Concatenation"};
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		int res = ParseOptions.parse(test, optfac, map).size();
		assertEquals(1,  res);
	}
	
	
	@Test@Tag("all")
	public void testParserOptionsAll() {
		String test[] = {"-Target","8", "-Features TryWithRessources, Concatenation", "-Help", "-Infos"};
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		int res = ParseOptions.parse(test, optfac, map).size();
		assertEquals(4,  res);
	}


}

