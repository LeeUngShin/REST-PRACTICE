package com.example.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, String>{
	
	Long countByIdAndPassword(String id, String pw);
	
	// Optional<MemberInfo> findById(String id);


}
