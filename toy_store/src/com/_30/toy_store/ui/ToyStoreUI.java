package com._30.toy_store.ui;

import com._30.toy_store.service.ToyService;

// import java.util.*; // 이런 식의 import 와일드카드(*)는 삼가는 것.

import com._30.toy_store.util.HistoryManager;
import com._30.toy_store.util.Scanner;

// final 클래스: 상속이 되지 않는 클래스
public final class ToyStoreUI {
	
	// history로 관리하면
	//  1. 메뉴 이동이 상하위 메뉴가 아니더라도 텔레포트 하듯이 다른 곳으로 이동 가능
	//  2. 이전 메뉴로 복귀가 간편(뒤로가기)
	private final HistoryManager history;
	private final ToyService toyService;
	private static final Scanner scanner = new Scanner();
	
	// enum 열거형을 대체하는 문자열들
	private static final String HOME = "HOME";
	private static final String INSERT_TOY = "INSERT_TOY";
	private static final String SELECT_TOY_HOME = "SELECT_TOY_HOME";
	private static final String SELECT_TOY_ALL = "SELECT_TOY_ALL";
	private static final String SEARCH_TOY = "SEARCH_TOY";
	private static final String UPDATE_TOY = "UPDATE_TOY";
	private static final String DELETE_TOY = "DELETE_TOY";
	private static final String BUY_TOY = "BUY_TOY";
	private static final String EXIT = "EXIT";
	
	public ToyStoreUI(String filePath) {
		history = new HistoryManager();
		toyService = new ToyService(filePath);
	}
	
	// main 같은 역할
	public void start() {
		while(true) {
			
			if (history.isEmpty()) {
				this.history.add(HOME);
			}
			
			final String currentMenu = this.history.getCurrentHistory();
			
			switch(currentMenu) {
			case HOME:
				printHomeMenu();
				break;
			case INSERT_TOY:
				printInsertToyMenu();
				break;
			case SELECT_TOY_HOME:
				printSelectToyHomeMenu();
				break;
			case SELECT_TOY_ALL:
				printSelectToyAllMenu();
				break;
			case SEARCH_TOY:
				printSearchToyMenu();
				break;
			case UPDATE_TOY:
				printUpateToyMenu();
				break;
			case DELETE_TOY:
				printDeleteToyMenu();
				break;
			case BUY_TOY:
				printBuyToyMenu();
				break;
			case EXIT:
				System.exit(0);// 무조건 프로세스 종료(메인 함수 종료)
				break;
			default:
				this.history.clear();
				break;
			}
		}// while(true)
	}
	
	public void printHomeMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===         H O M E         ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("  1. 상품 등록");
		System.out.println("  2. 상품 조회");
		System.out.println("  3. 상품 수정");
		System.out.println("  4. 상품 삭제");
		System.out.println("  5. 구매");
		System.out.println("  0. 종료");
		System.out.print  (">> ");
		
		// switch~case를 대체할 수 있는: 간단한 배열과 인덱싱
		// (선택지가 연속되게 넘버링이 되어 있을 때 편함.)
		final String[] menuList = {EXIT, INSERT_TOY, SELECT_TOY_HOME, UPDATE_TOY, DELETE_TOY, BUY_TOY};
		int sel = scanner.nextInt();
		this.history.add(menuList[sel]);
	}
	
	public void printInsertToyMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       INSERT  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("  1. 인형 종류 추가");
		System.out.println("  2. 블럭 장난감 종류 추가");
		System.out.print  (">> ");
		// TODO
		this.history.add(HOME);
	}
	
	public void printSelectToyHomeMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       SEARCH TOYS       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("  1. 전체 조회");
		System.out.println("  2. 상품 검색");
		System.out.println("  0. 홈으로");
		System.out.print  (">> ");
		
		final String[] menuList = {EXIT, SELECT_TOY_ALL, SEARCH_TOY};
		int sel = scanner.nextInt();
		this.history.add(menuList[sel]);
	}
	
	public void printSelectToyAllMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===        ALL  TOYS        ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		// TODO
		this.history.add(HOME);
	}
	
	public void printSearchToyMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       SEARCH  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		// TODO
		this.history.add(HOME);
	}
	
	public void printUpateToyMenu( ) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       UPDATE  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		// TODO
		this.history.add(HOME);
	}

	public void printDeleteToyMenu( ) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       DELETE  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		// TODO
		this.history.add(HOME);
	}
	
	public void printBuyToyMenu( ) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===        BUY   TOY        ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		// TODO
		this.history.add(HOME);
	}
}
