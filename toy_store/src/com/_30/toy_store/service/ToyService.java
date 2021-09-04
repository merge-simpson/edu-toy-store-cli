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
import java.util.List;

import com._30.toy_store.domain.BlockToy;
import com._30.toy_store.domain.Doll;
import com._30.toy_store.domain.Toy;

public final class ToyService {
	
	private String filePath;
	private List<Toy> toyList;
	
	public ToyService(String filePath) {
		this.filePath = filePath;
		this.load();
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
