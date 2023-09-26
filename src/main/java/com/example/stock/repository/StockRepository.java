package com.example.stock.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.example.stock.domain.Stock;

// 재고에 대한 CRUD 를 하기 위해 인터페이스 생성 
public interface StockRepository extends JpaRepository<Stock, Long> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE) // 어노테이션을 통해 쉽게 pessmistic lock 구현 가능 
	@Query("select s from Stock s where s.id = :id") // 네이티브 쿼리 
	Stock findByIdWithPessmisiticLock(Long id); // Lock을 걸고 데이터를 가져오는 메소드 작성
	
	@Lock(LockModeType.OPTIMISTIC) // optimisticLock 사용을 위한 쿼리 추가 
	@Query("select s from Stock s where s.id = :id")
	Stock findByIdWithOptimisticLock(Long id); // 메소드 이름 지어주기 

}
