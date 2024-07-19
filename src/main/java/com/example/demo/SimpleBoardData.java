package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_simpleboard")
@Entity
@Builder
public class SimpleBoardData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //MySQL의 AUTO_INCREMENT를 사용
	private Long id;   
	
//	@Column(length = 50, nullable = false)
//	private String name;
	
	@Column(length = 200, nullable = false)    
	private String contents; 	
	
	@Column(length = 200, nullable = false)    
	private String name; 
	
	@Column(length = 200, nullable = false)    
	private String writer; 

}
