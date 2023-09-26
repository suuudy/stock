package com.example.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;

@Service
public class OptimisticLockStockService {
	
	private final StockRepository stockRepository; // 필드 추가 

	public OptimisticLockStockService(StockRepository stockRepository) { // 생성자 추가 
		this.stockRepository = stockRepository;
	}
	
	@Transactional
	public void decrease(Long id, Long quantity) { // 재고 감소 메소드 작성 
		
		Stock stock = stockRepository.findByIdWithOptimisticLock(id); // 데이터 가져오기 
		
		stock.decrease(quantity); // 수량 감소 시켜주고 
		stockRepository.save(stock); // 데이터 저장 
	}

}
