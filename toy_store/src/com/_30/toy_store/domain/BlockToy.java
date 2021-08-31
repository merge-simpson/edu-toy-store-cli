package com._30.toy_store.domain;

public class BlockToy implements Toy {
	private String name;
	private int price;
	private int stock;
	private double discount;
	
	private BlockToy(Builder builder) {
		this.name = builder.name;
		this.price = builder.price;
		this.stock = builder.stock;
		this.discount = builder.discount;
	}
	
	// inner class는 static 습관
	public static class Builder {
		// 우리가 직접 체크할 필수 인자
		private String name = null;
		private int price = -1;
		
		// 초기값 줘도 되는 애들
		private int stock = 0;
		private double discount = 0.0;
		
//		public Builder() {
//			
//		}
		
		public Builder(String name, int price) {
			if (name == null) {
				throw new RuntimeException("Property NAME must not be NULL.");
			}
			if (price < 0) {
				throw new RuntimeException("Property PRICE must be bigger than 0.");
			}
			this.name = name;
			this.price = price;
		}
		
		public Builder name(String name) {
			if (name == null) {
				throw new RuntimeException("Property NAME must not be NULL.");
			}
			this.name = name;
			return this;
		}
		
		public Builder price(int price) {
			if (price < 0) {
				throw new RuntimeException("Property PRICE must be bigger than 0.");
			}
			this.price = price;
			return this;
		}
		
		public Builder stock(int stock) {
			if (stock < 0) {
				throw new RuntimeException("속성 STOCK은(재고는) 0보다 커야 합니다.");
			}
			this.stock = stock;
			return this;
		}
		
		public Builder discount(double discount) {
			if (discount < 0.0 || discount > 1.0) {
				throw new RuntimeException("속성 DISCOUNT는 0 ~ 1 범위여야 합니다.");
			}
			this.discount = discount;
			return this;
		}
		
		public BlockToy build() {
			if (name == null) {
				// throw new Exception();		--> checked exception: 외부에서 예외 처리 해줘야 함.
				// throw new RuntimeException(); --> 외부에서 try~catch로 감싸지 않아도 됨.
				throw new RuntimeException("Property NAME must not be NULL.");
			}
			if (price < 0) {
				throw new RuntimeException("Property PRICE must be bigger than 0.");
			}
			return new BlockToy(this);
		}
	}// inner class: Builder
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name == null) {
			throw new RuntimeException("Property NAME must not be NULL.");
		}
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		if (price < 0) {
			// return; 대신:
			throw new RuntimeException("Property PRICE must be bigger than 0.");
		}
		
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		if (stock < 0) {
			throw new RuntimeException("속성 STOCK은(재고는) 0보다 커야 합니다.");
		}
		this.stock = stock;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		if (discount < 0.0 || discount > 1.0) {
			throw new RuntimeException("속성 DISCOUNT는 0 ~ 1 범위여야 합니다.");
		}
		this.discount = discount;
	}
	@Override
	public String toString() {
		return String.format("BlockToy [name=%s, price=%d, stock=%d, discount=%4.2f]"	// %4.2f 소숫점 포함 4칸, 소숫점 아래 2칸.
				, this.name
				, this.price
				, this.stock
				, this.discount);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(discount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + price;
		result = prime * result + stock;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		BlockToy other = (BlockToy) obj;
		if (Double.doubleToLongBits(this.discount) != Double.doubleToLongBits(other.discount))
			return false;
		if (this.name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price != other.price)
			return false;
		if (stock != other.stock)
			return false;
		return true;
	}
}
