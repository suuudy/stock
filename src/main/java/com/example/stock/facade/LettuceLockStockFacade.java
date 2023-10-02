package com.example.stock.facade;

import org.springframework.stereotype.Component;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;

@Component
public class LettuceLockStockFacade { // 로직 실행 전 후로 해제를 수행해야하기 때문에 facade 클래스 생성 
	
	private RedisLockRepository redisLockRepository; // redis를 활용해서 lock 수행을 위해 변수로 추가 
	private final StockService stockService; // 재고 감소를 위해 변수로 추가 
	
	// 생성자 
	public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
		this.redisLockRepository = redisLockRepository;
		this.stockService = stockService;
	}
	
	public void decrease(Long id, Long quantity) throws InterruptedException {
		while (!redisLockRepository.lock(id)) { // while 문을 활용해서 lock 획득 시도 
			Thread.sleep(100); 
			// lock 획득 실패 시, 재시도 하기 위해 sleep 코드 추가 (redis에 갈 수 있는 부하를 줄이기 위함)
		}
		
		try {
			// lock 획득 성공시에 재고 감소 
			stockService.decrease(id, quantity);
		} finally {
			// 로직 종료 되었다면 lock 하제 
			redisLockRepository.unlock(id);
		}
	}

}
