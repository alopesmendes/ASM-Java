package concat;




public record MyRecord(int a, int b) {
		
	
	public static void main(String[] args) {
		MyRecord r = new MyRecord(1, 2);
		System.out.println(r+ "aaaa "+ r.equals(new MyRecord(1, 2)));
	}
	
}

