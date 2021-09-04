package com._30.toy_store.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class Paginator<T> {
	
	private List<T> targetList;
	private int page;
	private int pageSize;
	
	private int lastPage;
	
	private int beginIndexOfThisPage;
	private int endIndexOfThisPage;
	
	public Paginator(List<T> list, int pageSize) {
		super();
		this.targetList = list;
		this.page = 0;
		this.pageSize = pageSize;
		this.lastPage = this.targetList.size() / this.pageSize;
		setIndexStatusByPageNumber();
	}
	
	public void setPage(int page) {
		this.page = page;
		setIndexStatusByPageNumber();
	}
	
	public int getPage() {
		return this.page;
	}
	
	public void nextPage() {
		this.page++;
	}
	
	public void prevPage() {
		this.page--;
	}
	
	public void modifyPageStatus(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public boolean isFirstPage() {
		return this.page == 0;
	}
	
	public boolean hasNextPage() {
		return this.page < this.lastPage;
	}
	
	// Ignore this.
	// During education, just use "new ArrayList<>()".
	public List<T> getPagedPart() {
		List<T> pagedList = this.newInstance();
		if (pagedList == null) {
			return null;
		}
		
		for (int inputtedIndex = this.beginIndexOfThisPage;
				inputtedIndex < this.endIndexOfThisPage; inputtedIndex++ ) {
			pagedList.add(this.targetList.get(inputtedIndex));
		}
		return pagedList;
	}
	
	// Ignore this.
	// During education, just use "new ArrayList<>()".
	public List<T> getPartBetweenIndexOf(int firstIndex, int lastIndexPlusOne) {
		List<T> pagedList = this.targetList.subList(firstIndex, lastIndexPlusOne);
		return pagedList;
	}
	
	// Ignore this.
	// During education, just use "new ArrayList<>()".
	private List<T> newInstance() {
		List<T> newList = null;
		try {
			@SuppressWarnings("unchecked")
			Constructor<? extends List<T>> constructor = 
					(Constructor<? extends List<T>>) 
					this.targetList.getClass().getConstructor();
			newList = constructor.newInstance();
		} catch (NoSuchMethodException 
				| SecurityException 
				| InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException
				/* ▼ Unchecked Exception: */
				| NullPointerException e) {
			e.printStackTrace();
		}
		return newList;
	}
	
	private void setIndexStatusByPageNumber() {
		this.beginIndexOfThisPage = this.page * this.pageSize;
		this.endIndexOfThisPage = this.beginIndexOfThisPage
				+ (this.hasNextPage() ? this.pageSize : this.targetList.size() % this.pageSize);
	}
	
}
