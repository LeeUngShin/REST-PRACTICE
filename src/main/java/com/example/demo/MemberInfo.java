package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_memberInfoComment")
@Entity
@Builder
public class MemberInfo {
	
	@Id
	@Column(length = 50, nullable = false)
	private String id;   
	
	@Column(length = 200, nullable = false)    
	private String password; 
	
	@Column(length = 200, nullable = false)
	private String name;
	
	@Column(nullable = false)    
	private int grade;
}