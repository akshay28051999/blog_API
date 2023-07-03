package com.codewithdurgesh.blog.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiException extends RuntimeException{

	public ApiException(String message) {
		super(message);
	
	}

	public ApiException() {
		super();
		
	}

	
//	String resourceName;
//	String fieldName;
//	
//	
//	public ApiException(String resourceName, String fieldName) {
//		super(String.format("%s not found with %s", resourceName,fieldName));
//		this.resourceName = resourceName;
//		this.fieldName = fieldName;
//		
//	}
	
	
	
	
	
}
