package com.example.stock.repository;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository { // redis 명령어 사용을 위한 repository 클래스 추가 
	
	// redis 명령어를 실행하기 위해 RedisTemplate을 변수로 추가 
	private RedisTemplate<String, String> redisTemplate;

	public RedisLockRepository(RedisTemplate<String, String> redisTemplate) { //생성자 
		this.redisTemplate = redisTemplate;
	}
	
	// lock 메소드 구현
	public Boolean lock(Long key) { // setnx 명령어를 활용 
		return redisTemplate
				.opsForValue()
				.setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
	}
	
	// unlock 메소드 구현
	public Boolean unlock(Long key) {
		return redisTemplate.delete(generateKey(key));
	}
	
	private String generateKey(Long key) {
		return key.toString();
	}

}
