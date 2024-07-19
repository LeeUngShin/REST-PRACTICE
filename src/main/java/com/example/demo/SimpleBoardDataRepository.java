package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface SimpleBoardDataRepository extends JpaRepository<SimpleBoardData, Long>{
	
	@Transactional
	void deleteById(Long id);
	
	Long countByIdAndWriter(Long id, String writer);
}
