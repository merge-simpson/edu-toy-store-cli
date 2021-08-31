package com._30.toy_store.main;

import com._30.toy_store.ui.ToyStoreUI;

public final class Main {

	public static void main(String[] args) {
		// FIXME 파일 경로
		String filePath = "파일경로.txt";
		ToyStoreUI ui = new ToyStoreUI(filePath);
		ui.start();
	}
	
}
