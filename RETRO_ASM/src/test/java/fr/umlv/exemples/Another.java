package fr.umlv.exemples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Another {
	private int size;
	private boolean done;
	private final char toLine = '\n';
	private final float approximate;
	private final double morePrecise;
	private static final long ESTIMATE_SIZE = Long.MAX_VALUE;
	private static final short MIN_SIZE = Short.MIN_VALUE;
	private final Exemple mainExemple;
	private final Exemple[] other5Exemple = new Exemple[5];
	
	
	public static class Entry<K, V> {
		private final K key;
		private final V value;
		/**
		 * @param key
		 * @param value
		 */
		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "key: "+key+", value:"+value;
		}
	}
	/**
	 * @param size
	 * @param approximate
	 * @param morePrecise
	 * @param mainExemple
	 */
	public Another(float approximate, double morePrecise, Exemple mainExemple) {
		this.approximate = approximate;
		this.morePrecise = morePrecise;
		this.mainExemple = mainExemple;
	}
	
	public boolean add(Exemple exemple, int index) {
		Objects.requireNonNull(exemple);
		if (index < 0 || index >= 5) {
			throw new IllegalArgumentException("index");
		}
		if (done) {
			return false;
		} else if (approximate + morePrecise < ESTIMATE_SIZE && morePrecise > MIN_SIZE) {
			other5Exemple[index] = exemple;
			size++;
		}
		return (done = size == 5);
	}
	
	@Override
	public String toString() {
		return 	mainExemple.toString()+toLine+
				Arrays.stream(other5Exemple).
				filter(e -> e != null).
				map(Exemple::toString).collect(Collectors.joining(", "));
	}
	
	
	public void parse(Path path) throws IOException {
		try (Stream<Path> w = Files.walk(path)) {
			w.map(p -> p.getFileName()).forEach(System.out::println);
		}
		String c = "name" + "7";
		Integer.parseInt(c);
	}

	public static void main(String[] args) {
		Another a = new Another(5, 10.000, new Exemple());
		a.add(new Exemple(), 0);
		System.out.println(a.toString());
		Entry<Integer, String> e = new Entry<Integer, String>(5, "five");
		System.out.println(e.toString());

	}

}
