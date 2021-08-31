package com._30.toy_store.main;

import com._30.toy_store.ui.ToyStoreUI;

public final class Main {

	public static void main(String[] args) {
		// 운영체제 사용자
		com.sun.security.auth.module.NTSystem NTSystem = new
		        com.sun.security.auth.module.NTSystem();
		String pcUserName = NTSystem.getName();
		
		// .dat 대신 .txt 사용
		String filePath = String.format(
				"C:\\users\\%s\\toy-store-data\\objects.txt", pcUserName);
		ToyStoreUI ui = new ToyStoreUI(filePath);
		ui.start();
	}
	
}
