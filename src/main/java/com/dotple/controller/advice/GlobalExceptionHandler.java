package com.dotple.controller.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> HandleCustomException(CustomException e) {

		e.printStackTrace();
		ResponseCode responseCode = e.getResponseCode();

		return handleExceptionInternal(responseCode);
	}

	// Optional.get()에 대한 예외 처리
	@ExceptionHandler
	public ResponseEntity<Object> HandleNoSuchElementException(NoSuchElementException e) {
		e.printStackTrace();
		return handleExceptionInternal(ResponseCode.C4040);
	}

	// 적절하지 않은 데이터가 DB로 들어갈 뻔한 경우. 발생해서는 안 됨!
	@ExceptionHandler
	public ResponseEntity<Object> HandleDataIntegrityViolationException(DataIntegrityViolationException e) {
		e.printStackTrace();
		return handleExceptionInternal(ResponseCode.C5001);
	}

	// 요청 값이 무효한 경우
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return handleExceptionInternal(ResponseCode.C4000);
	}

	private ResponseEntity<Object> handleExceptionInternal (ResponseCode responseCode) {
		return ResponseEntity.status(responseCode.getStatus()).body(new ResponseMessage<>(responseCode, ""));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request){
		ex.printStackTrace();
		return ResponseEntity.status(500).body(new ResponseMessage<>(ResponseCode.C5000, ""));
	}
}
