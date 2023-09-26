package com.example.stock.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity //database의 테이블이라고 생각
public class Stock {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long productId; //상품의 아이디 
	private Long quantity; //수량 
	
	@Version
	private Long version; // optimisticLock 사용을 위한 필드 추가 
	
	public Stock() { // 기본 생성자 
	}

	public Stock(Long productId, Long quantity) { // 상품의 아이디와 수량을 가지는 생성자 
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getQuantity() { // 수량 확인을 위한 수량 getter 생성 
		return quantity;
	}

	public void decrease(Long quantity) { //재고를 감소시키기 위한 메소드 
		if (this.quantity - quantity < 0) { //현재 가진 수량 - 감소 시킬 수량 
			throw new RuntimeException("재고는 0개 미만이 될 수 없습니다."); //익셉션 발생 
		}
		
		this.quantity -= quantity; //0개 미만이 아니라면 현재 수량 감소 
	}
	
}
