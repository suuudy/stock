package com.example.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;

// lock을 걸고 재고감소 로직을 작성하기 위한 클래스 
@Service
public class PessmisticLockStockService { //Stock에 대한 CRUD가 가능한 클래스
	
	private final StockRepository stockRepository; // 필드 추가 

	// 생성자 생성 
	public PessmisticLockStockService(StockRepository stockRepository) { 
		this.stockRepository = stockRepository;
	}
	
	@Transactional
	public void decrease(Long id, Long quantity) {
		// lock을 활용해서 데이터를 가져옴 
		Stock stock = stockRepository.findByIdWithPessmisiticLock(id);
		 
		stock.decrease(quantity); // 재고를 감소시키고 
		stockRepository.save(stock); // 데이터를 저장 
	}

}
