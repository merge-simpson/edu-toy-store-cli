package com._30.toy_store.ui;

import java.util.List;

import com._30.toy_store.domain.BlockToy;
import com._30.toy_store.domain.Doll;
import com._30.toy_store.domain.Toy;
import com._30.toy_store.service.ToyService;

// import java.util.*; // 이런 식의 import 와일드카드(*)는 삼가는 것.

import com._30.toy_store.util.HistoryManager;
import com._30.toy_store.util.Paginator;
import com._30.toy_store.util.Scanner;

// final 클래스: 상속이 되지 않는 클래스
public final class ToyStoreUI {
	
	// history로 관리하면
	//  1. 메뉴 이동이 상하위 메뉴가 아니더라도 텔레포트 하듯이 다른 곳으로 이동 가능
	//  2. 이전 메뉴로 복귀가 간편(뒤로가기)
	private final HistoryManager history;
	private final ToyService toyService;
	private static final Scanner scanner = new Scanner();
	
	private static final int PAGE_SIZE = 5;
	
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
				toyService.save();
				System.exit(0); // 무조건 프로세스 종료(메인 함수 종료)
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
		// (진도상 사용자 입력 유효성 예외 처리는 세세하게 하지 않음.)
		int sel = scanner.nextInt();
		// Class<? extends Toy> toyType = null;
		
		String toyTypeKor = null;
		
		if (sel == 1) {
			toyTypeKor = "인형";
		} else if (sel == 2) {
			toyTypeKor = "블록";
		} else {
			System.out.println("잘못된 입력입니다.");
			return; // this.history 조작하지 않고 return 하면 이 함수로 돌아옴.
		}
		
		Toy toy = null;
		String name;
		int price;
		int stock;
		double discount;
		boolean isSuccessed;
		try {
			System.out.printf("[%s] 장난감 이름: ", toyTypeKor);
			name = scanner.nextLine();
			System.out.printf("[%s] 장난감 가격: ", toyTypeKor);
			price = scanner.nextInt();
			System.out.printf("[%s] 장난감 재고: ", toyTypeKor);
			stock = scanner.nextInt();
			System.out.printf("[%s] 장난감 할인율: ", toyTypeKor);
			discount = scanner.nextDouble();
			
			if (sel == 1) {
				toy = new Doll.Builder(name, price)
						.stock(stock)
						.discount(discount)
						.build();
			} else if (sel == 2) {
				toy = new BlockToy.Builder(name, price)
						.stock(stock)
						.discount(discount)
						.build();
			}
			isSuccessed = toyService.addToy(toy);
			/*
			 * ▲ 발견할 수 있는 문제점: 
			 * 	Doll의 Builder와 BlockToy의 Builder에
			 * 	공통 조상을 두지 않아 다형성 구성 어려움.
			 * (오버라이딩을 하지 않으니 똑같은 코드임에도 if문으로 구분하는 중)
			 */
		} catch (NumberFormatException e) {
			System.out.println("숫자로 입력해 주십시오.");
			return;
		} catch (RuntimeException e) {
			System.out.println("올바른 값을 입력하여 주십시오.");
			return;
		}
		
		if (isSuccessed) {
			System.out.println("상품 등록이 완료되었습니다.");
		} else {
			System.out.println("중복된 상품이 존재합니다.");
		}
		
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
		List<Toy> allToys = toyService.getToyAll();
		for (Toy toy : allToys) {
			System.out.println(toy);
		}
		this.history.add(HOME);
	}
	
	public void printSearchToyMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       SEARCH  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("이름 전체 또는 일부를 입력하십시오.");
		System.out.println("(공백으로 키워드를 구분합니다.)");
		String keyword = scanner.nextLine();
		
		List<Toy> filteredToys = toyService.getSearchedToy(keyword);
		for (Toy toy : filteredToys) {
			System.out.println(toy);
		}
		
		this.history.add(HOME);
	}
	
	public void printUpateToyMenu() {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       UPDATE  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		
		Toy selectedToy = this.selectToyWithMessage("어떤 상품을 수정하시겠습니까?");
		if (selectedToy == null) {
			this.history.removeLastMenu(); // 뒤로 가기
			return;
		}
		
		int sel = 999;
		updatingLoop: while(sel != 0) {
			System.out.println("어떤 속성을 수정하시겠습니까?");
			System.out.println(" 1. 상품 이름");
			System.out.println(" 2. 가격");
			System.out.println(" 3. 재고");
			System.out.println(" 4. 할인율(0.0 ~ 1.0");
			System.out.println(" 5. 상품 분류 변경");	// FIXME 교육 떄는 제외
			System.out.println(" 0. 완료");
			System.out.print  (">> ");
			
			sel = scanner.nextInt();
			switch (sel) {
			case 1:
				System.out.print("새 이름 입력: ");
				String name = scanner.nextLine();
				if (selectedToy instanceof Doll) {
					((Doll)selectedToy).setName(name);
				} else if (selectedToy instanceof BlockToy) {
					((BlockToy)selectedToy).setName(name);
				}
				break;
			case 2:
				System.out.print("새 가격 입력: ");
				int price = scanner.nextInt();
				if (selectedToy instanceof Doll) {
					((Doll)selectedToy).setPrice(price);
				} else if (selectedToy instanceof BlockToy) {
					((BlockToy)selectedToy).setPrice(price);
				}
				break;
			case 3:
				System.out.print("재고 입력: ");
				int stock = scanner.nextInt();
				if (selectedToy instanceof Doll) {
					((Doll)selectedToy).setStock(stock);
				} else if (selectedToy instanceof BlockToy) {
					((BlockToy)selectedToy).setStock(stock);
				}
				break;
			case 4:
				System.out.print("할인율 입력: ");
				double discount = scanner.nextDouble();
				if (selectedToy instanceof Doll) {
					((Doll)selectedToy).setDiscount(discount);
				} else if (selectedToy instanceof BlockToy) {
					((BlockToy)selectedToy).setDiscount(discount);
				}
				break;
			case 5: // FIXME 교육 때는 아예 제외.
				System.out.println("1. 인형");
				System.out.println("2. 블록");
				System.out.print(">> ");
				sel = scanner.nextInt();
				if (sel == 1) {
					if (Doll.class.equals(selectedToy.getClass())) {
						System.out.println("상품 타입이 이미 Doll입니다.");
						continue updatingLoop;
					}

					boolean isModified = this.toyService.modifyToyType(selectedToy, Doll.class);
					System.out.println(isModified ? 
							"상품이 인형으로 성공적으로 변경되었습니다." 
							: "상품이 올바르게 변경되지 않았습니다.(예외 발생)");
				} else if (sel == 2) {
					if (BlockToy.class.equals(selectedToy.getClass())) {
						System.out.println("상품 타입이 이미 BlockToy입니다.");
						continue updatingLoop;
					}
					boolean isModified = this.toyService.modifyToyType(selectedToy, BlockToy.class);
					System.out.println(isModified ? 
							"상품이 인형으로 성공적으로 변경되었습니다." 
							: "상품이 올바르게 변경되지 않았습니다.(예외 발생)");
				}
				break;
			/*
			case 0:
				break;
			*/
			default:
				break updatingLoop;
			}
		}
		this.history.add(HOME);
	}// printUpateToyMenu()
	
	public void printDeleteToyMenu( ) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===       DELETE  TOY       ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		Toy selectedToy = this.selectToyWithMessage("어떤 상품을 삭제하시겠습니까?");
		boolean isDeleted = this.toyService.removeToy(selectedToy);
		System.out.println(isDeleted ? "상품이 삭제되었습니다.": "상품이 삭제되지 않았습니다.");
		this.history.add(HOME);
	}
	
	public void printBuyToyMenu( ) {
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		System.out.println("===        BUY   TOY        ===");
		System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		Toy toy = selectToyWithMessage("구매할 상품을 선택하십시오.");
		System.out.println("몇 개나 구매하시겠습니까?");
		int amount = scanner.nextInt();
		this.toyService.buyAndGetTotalPrice(toy, amount);
		this.history.add(HOME);
	}
	
	private Toy selectToyWithMessage(String message) {
		Toy selectedToy = null;
		List<Toy> filteredList = toyService.getToyAll();
		Paginator<Toy> paginator = new Paginator<Toy>(filteredList, PAGE_SIZE);
		List<Toy> pagedList = paginator.getPagedPart();
		boolean isSelected = false;
		while (!isSelected) {
			System.out.println(message);
			System.out.println(" 0. 검색");
			for (int i = 0; i < pagedList.size(); i++) {
				Toy toy = pagedList.get(i);
				
				if (toy instanceof Doll) {
					Doll toyAsDoll = (Doll)toy;
					System.out.println(
							String.format(" %d. %s", i+1, toyAsDoll.getName()));
				} else if (toy instanceof BlockToy) {
					BlockToy toyAsBlockToy = (BlockToy)toy;
					System.out.println(
							String.format(" %d. %s", i+1, toyAsBlockToy.getName()));
				}
			}
			if (!paginator.isFirstPage()) {
				System.out.println(" 7. 이전 페이지");
			}
			if (paginator.hasNextPage()) {
				System.out.println(" 8. 다음 페이지");
			}
			System.out.println(" 9. 뒤로 가기");
			System.out.println("--------------");
			System.out.print  (">> ");
			int sel = scanner.nextInt();
			
			switch (sel) {
			case 0:
				System.out.println("=== 검색:");
				System.out.println(" 검색 키워드를 입력하세요.");
				System.out.println(" (띄어쓰기로 각 키워드 구분)");
				System.out.print  (">> ");
				String keyword = scanner.nextLine();
				filteredList = toyService.getSearchedToy(keyword);
				break;
			case 1: 
			case 2:
			case 3:
			case 4:
			case 5:
				isSelected = sel <= pagedList.size();	// true => while break
				if (isSelected) {
					selectedToy = pagedList.get(sel-1);
				}
				break;
			case 7:
				paginator.prevPage();
				break;
			case 8:
				paginator.nextPage();
				break;
			case 9:
				return null;
			default:
				break;
			}
		}// while (!selected)
		return selectedToy;
	}// fun: selectToyWithMessage(String message)
}
