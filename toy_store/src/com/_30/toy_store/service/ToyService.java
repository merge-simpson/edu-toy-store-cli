package com._30.toy_store.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com._30.toy_store.domain.BlockToy;
import com._30.toy_store.domain.Doll;
import com._30.toy_store.domain.Toy;

public final class ToyService {
	
	/* (TODO 여러 인스턴스가 똑같은 File을 건들면 안 됨.)
	 * ∵ 한 파일에 여러 인스턴스가 접근하면 무결성 보장 안 됨.
	 * (방법 1) 싱글톤 패턴
	 * 	한계: 서로 다른 파일을 다루는 ToyService 인스턴스도 생성 불가.
	 * 	보완: static Map 사용
	 * 이 시스템에서만 사용할 때는 사실 ToyService가 싱글톤 패턴이어도 되고
	 * 굳이 여러 파일을 사용할 필요가 없긴 함.
	 */
	private static final Map<String, ToyService> instanceMap = new HashMap<>();
	
	private final String filePath;
	private List<Toy> toyList;
	
	private ToyService(String filePath) {
		this.filePath = filePath;
		this.load();
	}
	
	public static final ToyService getInstance(String filePath) {
		ToyService instance = instanceMap.get(filePath);
		if (instance == null) {
			instance = instanceMap.put(filePath, new ToyService(filePath));
		}
		return instance;
	}
	
	// todo, fixme 대문자로 쓰면 tasks에 생김.
	public boolean addToy(Toy toy) {
		// (유효성) toy는 null이 아니어야 하며
		if (toy == null) {
			return false;
		}
		// (유효성) toy의 name이 다른 것들과 중복되지 않아야 한다.
		for (Toy eachToy : toyList) {
			// ▼ 오버라이딩 안 되어 있어 코드 중복이 발생하고 있고,
			//		가독성이 떨어진다.
			if (eachToy instanceof Doll) {
				Doll eachDoll = ((Doll) eachToy);
				if (toy instanceof Doll) {
					Doll toyAsDoll = (Doll)toy;
					if(toyAsDoll.getName().equals(eachDoll.getName())) {
						return false;
					}
				} else if (toy instanceof BlockToy) {
					BlockToy toyAsBlockToy = (BlockToy)toy;
					if(toyAsBlockToy.getName().equals(eachDoll.getName())) {
						return false;
					}
					
				}
			} else if (eachToy instanceof BlockToy) {
				BlockToy eachDoll = ((BlockToy) eachToy);
				if (toy instanceof Doll) {
					Doll toyAsDoll = (Doll)toy;
					if(toyAsDoll.getName().equals(eachDoll.getName())) {
						return false;
					}
				} else if (toy instanceof BlockToy) {
					BlockToy toyAsBlockToy = (BlockToy)toy;
					if(toyAsBlockToy.getName().equals(eachDoll.getName())) {
						return false;
					}
				}
			}
		}
		
		toyList.add(toy);
		
		boolean isSaved = save();
		return isSaved;
	}
	
	public List<Toy> getToyAll() {
		return toyList;
	}
	
	public List<Toy> getSearchedToy(String keyword) {
		List<Toy> filteredToyList = new ArrayList<>();
		String[] keywords = keyword.toLowerCase().split("[ \t\n]");
		
		for (Toy eachToy : toyList) {
			String name = "";
			if (eachToy instanceof Doll) {
				name = ((Doll)eachToy).getName()
						.replace(" ", "")
						.replace("\t", "")
						.replace("\n", "");
			} else if (eachToy instanceof BlockToy) {
				name = ((BlockToy)eachToy).getName()
						.replace(" ", "")
						.replace("\t", "")
						.replace("\n", "");
			}
			
			// boolean hasKeyword = 
			//		Arrays.stream(keywords).anyMatch(name::contains);
			for (String eachKeyword : keywords) {
				if (name.contains(eachKeyword)) {
					filteredToyList.add(eachToy);
					break;
				}
			}
		}
		return filteredToyList;
	}
	
	// FIXME 교육 때는 삭제
	public boolean modifyToyType(Toy toy, Class<? extends Toy> type) {
		try {
			String name = null;
			int price = 0;
			int stock = 0;
			double discount = 0.0;
			
			if (toy instanceof Doll) {
				Doll dollToy = (Doll)toy;
				name = dollToy.getName();
				price = dollToy.getPrice();
				stock = dollToy.getStock();
				discount = dollToy.getDiscount();
			} else if (toy instanceof BlockToy) {
				BlockToy blockToy = (BlockToy) toy;
				name = blockToy.getName();
				price = blockToy.getPrice();
				stock = blockToy.getStock();
				discount = blockToy.getDiscount();
			}
			
			// 리플렉션
			Class<?> builderClass = Class.forName(type.getName() + "$Builder");
			Constructor<?> constructor = builderClass.getConstructor(String.class, Integer.class);
			Method setName = builderClass.getMethod("name", String.class); 		// not used
			Method setPrice = builderClass.getMethod("price", Integer.class);	// not used
			Method setStock = builderClass.getMethod("stock", Integer.class);
			Method setDiscount = builderClass.getMethod("discount", Double.class);
			
			Toy modifiedToy = (Toy) constructor.newInstance(name, price);
			setStock.invoke(modifiedToy, stock);
			setDiscount.invoke(modifiedToy, discount);
			
			for (int i = 0; i < this.toyList.size(); i++) {
				Toy currentToy = this.toyList.get(i);
				String currentToyName = null;
				if (currentToy instanceof Doll) {
					currentToyName = ((Doll) currentToy).getName();
				} else if (currentToy instanceof BlockToy) {
					currentToyName = ((BlockToy) currentToy).getName();
				}
				
				if (name.equals(currentToyName)) {
					this.toyList.remove(i);
					this.toyList.add(i, modifiedToy);
					return true;
				}
			}
		} catch (ClassNotFoundException 
				| NoSuchMethodException 
				| SecurityException 
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean removeToy(Toy toy) {
		boolean result = toy != null 
				&& this.toyList.remove(toy);
		
		return result;
	}
	
	public int buyAndGetTotalPrice(Toy toy, int amount) {
		if (toy == null
				|| amount <= 0) {
			return 0;
		}
		
		// Check amount vs stock
		int stock = 0;
		if (toy instanceof Doll) {
			stock = ((Doll) toy).getStock();
		} else if (toy instanceof BlockToy) {
			stock = ((BlockToy) toy).getStock();
		}
		
		if (stock < amount) {
			return 0;
		}
		
		int discountedUnitPrice = 0;
		
		if (toy instanceof Doll) {
			int unitPrice = ((Doll) toy).getPrice();
			double inversedDiscount = 1 - ((Doll) toy).getDiscount();
			discountedUnitPrice = (int) (unitPrice * inversedDiscount);
		} else if (toy instanceof BlockToy) {
			int unitPrice = ((BlockToy) toy).getPrice();
			double inversedDiscount = 1 - ((BlockToy) toy).getDiscount();
			discountedUnitPrice = (int) (unitPrice * inversedDiscount);
		}
		
		return discountedUnitPrice * amount;
	}
	
	public List<Toy> load() {
		// ※ try~catch~resources: Closable 객체들 close 해줌.
		File file = new File(filePath);
		
		if (!file.exists()) {
			toyList = new ArrayList<Toy>();
			return toyList;
		}
		
		try (FileInputStream fis = new FileInputStream(file)
				; ObjectInputStream ois = new ObjectInputStream(fis)) {
			Object object = ois.readObject();
			toyList = object != null ? (List<Toy>) object : new ArrayList<Toy>();
		} catch (FileNotFoundException e) {	// for "new FileInputStream(file)"
			e.printStackTrace();
		} catch (IOException e) {			// for "new ObjectInputStream(fis)"
			e.printStackTrace();
		} catch (ClassNotFoundException e) {// for "ois.readObject()"
			e.printStackTrace();
		} catch (ClassCastException e) {	// for "(List<Toy>) object"
			e.printStackTrace();
		}
		
		return toyList;
	}
	
	public boolean save() {
		// Distinguish the File and Directory for the next step.
		int lastIndexOfSeparator = filePath.replace("/", "\\").lastIndexOf("\\");
		String dirString = filePath.substring(0, lastIndexOfSeparator);
		
		// Create directory if not exists.
		File directory = new File(dirString);
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		// Write Object
		File file = new File(filePath);
		
		try (FileOutputStream fos = new FileOutputStream(file)
				; ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(toyList);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
