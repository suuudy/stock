package com.example.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;

@Service
public class StockService { // 재고 감소 기능 
	// stock 에 대한 CRUD가 가능해야하기 때문에 추가 
	private final StockRepository stockRepository;

	//생성자 추가 
	public StockService(StockRepository stockRepository) { 
		this.stockRepository = stockRepository;
	}
	
	//재고 감소 메소드
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void decrease(Long id, Long quantity) { //감소시킬 id와 수량 
		// Stock 조회
		// 재고를 감소시킨 뒤
		// 갱신된 값을 저장하도록 하겠습니다.
		
		// stock 레포지토리를 사용해서 stock를 조회 
		Stock stock = stockRepository.findById(id).orElseThrow(); 
		stock.decrease(quantity); // 재고 감소 시키기 
		
		stockRepository.saveAndFlush(stock); // 갱신된 값을 저장 
		
	}

}
