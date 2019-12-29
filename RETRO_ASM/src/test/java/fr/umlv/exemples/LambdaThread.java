package fr.umlv.exemples;

public class LambdaThread {
	public static void main(String[] args) {
		Thread thread = new Thread(() -> { System.out.println("Thread en cours"); });
		thread.start();
	}

}