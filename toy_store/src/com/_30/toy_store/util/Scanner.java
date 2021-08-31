package com._30.toy_store.util;

public class Scanner {
	
	// 위임
	private static final java.util.Scanner deligatingScanner = new java.util.Scanner(System.in);
	
	public int nextInt() {
		int input = deligatingScanner.nextInt();
		deligatingScanner.nextLine();	// 버퍼 지우기
		return input;
	}
	
	public double nextLong() {
		long input = deligatingScanner.nextLong();
		deligatingScanner.nextLine();	// 버퍼 지우기
		return input;
	}
	
	public double nextDouble() {
		double input = deligatingScanner.nextDouble();
		deligatingScanner.nextLine();	// 버퍼 지우기
		return input;
	}
	
	public String nextLine() {
		return deligatingScanner.nextLine();
	}
	
}
