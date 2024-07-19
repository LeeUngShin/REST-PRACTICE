package com.example.demo;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class SimpleBoardCommentDto {
	
	@NotBlank(message="이름이 빈칸입니다.")
	private String name;
	
	private String contents;
	
	private String password;
	
	

}
