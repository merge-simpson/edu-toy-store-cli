package com._30.toy_store.service;

import java.util.ArrayList;
import java.util.List;

import com._30.toy_store.domain.Toy;

public final class ToyService {
	
	// TODO Create initial properties.(filePath:String)
	private List<Toy> toyList;
	
	public ToyService(String filePath) {
		this.toyList = new ArrayList<>();
		// TODO 파일(filePath에 있는)에서 읽어서 toyList에 모두 add
	}
	
	// todo, fixme 대문자로 쓰면 tasks에 생김.
	public boolean addToy(Toy toy) {
		// TODO (유효성) toy는 null이 아니어야 하며
		// TODO (유효성) toy의 name이 다른 것들과 중복되지 않아야 한다.
		return true;
	}
	
	public List<Toy> getToyAll() {
		return toyList;
	}
	
	public List<Toy> getSearchedToy(String keyword) {
		List<Toy> filteredToyList = new ArrayList<>();
		// TODO filter list
		return filteredToyList;
	}
	
	public boolean modifyToy(Toy toy) {
		// TODO VALID CHECK: unique(toy.name)
		return true;
	}
	
	public boolean removeToy(Toy toy) {
		// TODO remove toy and always return true.
		return true;
	}
	
	public int buyToy(Toy toy, int amount) {
		// TODO purchase. Consider stock of toy.
		return amount;
	}
	
	public boolean save() {
		// TODO save into filePath.
		return true;
	}
	
}
