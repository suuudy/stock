package com.example.stock.facade;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.example.stock.service.StockService;

@Component
public class RedissonLockStockFacade { // lock 관련된 클래스는 라이브러리로 제공해줌. (별도의 repository 작성 필요 없음)
	//실행 전 후로 lock 획득, 해제를 위한 facade 클래스 작성 
	
	private RedissonClient redissonClient; // lock 획득에 사용 할 필드 추가 
	private StockService stockService; // 재고 감소를 위해 추가 
	
	//생성자 
	public RedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
		
		this.redissonClient = redissonClient;
		this.stockService = stockService;
	}
	
	public void decrease(Long id, Long quantity) {
		RLock lock = redissonClient.getLock(id.toString()); // redissonClient를 활용해서 lock 객체를 가져옴 
		
		try {
			// 몇 초 동안 lock 획득할 건지, 점유할건지 설정하여 lock 획득 
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS); 
			//테스트 실패 시, lock 기다리는 시간 조금 늘려 준 후 다시 실행  (10 -> 15)
			
			if(!available) { // lock 획득 실패 시, 로그 남김 
				System.out.println("lock 획득 실패");
				return;
			}
			
			stockService.decrease(id, quantity); // 정상적으로 lock 획득했다면 재고 감소 실행 
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			// 로직이 정상적으로 종료 후, lock 해제 
			lock.unlock();
		}
	}

}
