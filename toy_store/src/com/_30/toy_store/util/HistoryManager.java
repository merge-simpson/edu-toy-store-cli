package com._30.toy_store.util;

import java.util.ArrayList;
import java.util.List;
/**
 * @author merge-simpson
 */
public final class HistoryManager {
	
	private final List<String> history = new ArrayList<>();
	
	public String getCurrentHistory() {
		int lastIndex = history.size() - 1;
		return history.get(lastIndex);
	}
	
	public boolean isEmpty() {
		return this.history.isEmpty();
	}
	
	public void add(String menu) {
		history.add(menu);
	}
	
	public void removeLastMenu() {
		int lastIndex = history.size() - 1;
		this.history.remove(lastIndex);
	}
	
	public List<String> getMenuHistoryAll() {
		return this.history;
	}
	
	public void clear() {
		this.history.clear();
	}
}
