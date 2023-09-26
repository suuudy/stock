package com.example.stock.facade;

import org.springframework.stereotype.Component;

import com.example.stock.service.OptimisticLockStockService;

@Component
public class OptimisticLockStockFacade { // 실패했을 때 재시도를 해야하므로 facade 클래스 생성 
	
	private final OptimisticLockStockService optimisticLockStockService; // 필드 추가 

	// 생성자 추가 
	public OptimisticLockStockFacade(OptimisticLockStockService optimisticLockStockService) {
		this.optimisticLockStockService = optimisticLockStockService;
	}
	
	public void decrease(Long id, Long quantity) throws InterruptedException {
		while (true) { // 업데이트 실패 시, 재시도를 해야하므로 while 문에 작성 
			try {
				optimisticLockStockService.decrease(id, quantity); // 재고 감소 메소드 호출 
				
				break; // 정상적으로 업데이트가 되었다면 빠져나오기 
			} catch (Exception e) {
				Thread.sleep(50); // 수량 감소 실패 시, 50 밀리세컨즈 뒤에 재시도 
			}
		}
	}

}
