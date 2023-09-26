package com.example.stock.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;

@SpringBootTest
class StockServiceTest { // 테스트 코드 작성 

	@Autowired
	private PessmisticLockStockService stockService;
	
	@Autowired
	private StockRepository stockRepository;

	
	@BeforeEach // 테스트를 하기 위한 디비 재고 작업 (재고가 있어야 실행이 되니까) 
	public void before() {
		stockRepository.saveAndFlush(new Stock(1L, 100L));
	}
	
	@AfterEach // 테스트 케이스 종료 후, 모든 아이템 삭제 
	public void after() {
		stockRepository.deleteAll();
	}
	
	@Test
	public void 재고감소() { // 재고 로직에 대한 테스트 케이스 작성 
		stockService.decrease(1L, 1L); // 1번 물건 1개 감소 시키기 
		
		//100 - 1 = 99
		Stock stock = stockRepository.findById(1L).orElseThrow();
		
		assertEquals(99, stock.getQuantity()); // 99개가 남아있는지 확인하기 위한 코드 
	}
	
	@Test
	public void 동시에_100개의_요청() throws InterruptedException {
		int threadCount = 100; //동시에 여러개의 요청을 보내야 하기 때문에 멀티쓰레드 사용
		// 멀티쓰레드 사용을 위해 ExecutorService 사용
		// ExecutorService : 비동기로 실행하는 작업을 단순화하여 사용할 수 있게 도와주는 자바의 API 
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		// 100개의 요청이 끝날 때까지 기다려야하기 때문에 CountDownLatch 사용 
		// CountDownLatch : 다른 스레드에서 수행 중인 작업이 완료될 때까지 대기 할 수 있도록 도와주는 클래
		CountDownLatch latch = new CountDownLatch(threadCount);
		
		for (int i = 0; i < threadCount; i++) { // for문을 활용하여 100개의 요청 보내기 
			executorService.submit(() -> {
				try {
					stockService.decrease(1L, 1L); // 감소 시킬 id와 수량 
				} finally {
					latch.countDown();
				}
				
			});
		}
		
		latch.await();
		
		// 모든 작업이 완료되면 수량 비교 작업 
		Stock stock = stockRepository.findById(1L).orElseThrow();
		//100 - (1 * 100) = 0 -> 이게 예상한 작업 
		assertEquals(0, stock.getQuantity()); // 우리의 예상은 0개, 그리고 실제로 남아있는 재고 비교해줘 
		
	}
	
	
	
}
