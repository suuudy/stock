package com.example.stock.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;

@Component
public class NamedLockStockFacade { // lock 획득, 해제를 위한 클래스 작성 

	private final LockRepository lockRepository;
	private final StockService stockService;
	
	public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
		this.lockRepository = lockRepository;
		this.stockService = stockService;
	}
	
	@Transactional
	public void decrease(Long id, Long quantity) {
		try {
			lockRepository.getLock(id.toString()); // lock 획득 
			stockService.decrease(id, quantity); // 재고감소 
		} finally {
			lockRepository.releaseLock(id.toString()); // 모든 로직 종료 후 lock 해제 
		}
	}
	
}
