package concat;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipInputStream;

public class TryConcat {
	public void parseCount(Path path) throws IOException {
		Closeable c = new Closeable() {
			@Override
			public void close() {
				System.out.println("close called !");
			}
		};
		try(c) {
			System.out.println("testTryWithResources");
		}
		
		try(InputStream in = Files.newInputStream(path);
			ZipInputStream zip = new ZipInputStream(in)) {
			System.out.println(zip.available());
		}
		
	
	}

	public static void testTryWithResources() throws IOException {
		Closeable c = new Closeable() {
			@Override
			public void close() {
				System.out.println("close called !");
			}
		};
		try(c) {
			System.out.println("testTryWithResources");
		}
	}

	public static void testTryWithResources2() {
		var c = new Closeable() {
			@Override
			public void close() {
				System.out.println("close called !");
			}
		};
		try(c) {
			System.out.println("testTryWithResources2");
		}
	}
	
	//public static void concat() {
	//	String s1 = "zae" + 4 + "zae";
	//	String s2 = s1 + ":was s1";
	//	System.out.println(s1 + "//"+s2);
	//}
}
