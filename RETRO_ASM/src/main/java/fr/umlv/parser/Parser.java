package fr.umlv.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Parser {
	public static byte[] parser(Path path) throws IOException {
		return Files.readAllBytes(path);
	}
}
