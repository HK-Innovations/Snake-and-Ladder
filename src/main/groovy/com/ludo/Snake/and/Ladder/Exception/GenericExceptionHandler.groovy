package com.ludo.Snake.and.Ladder.Exception


import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GenericExceptionHandler {

//        @ExceptionHandler(NoSuchElementException.class)
//        static  ResponseEntity<ErrorResponse> inputMismatchException(NoSuchElementException ex) {
//                ErrorResponse errorResponse = new ErrorResponse()
//                errorResponse.setErrorMessage(ex.getMessage())
//                errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value())
//                errorResponse.setTimeStamp(LocalDateTime.now())
//                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST)
//        }

//        @ExceptionHandler(Exception.class)
//        static ResponseEntity<ErrorResponse> handleException(Exception ex) {
//                ErrorResponse errorResponse = new ErrorResponse()
//                errorResponse.setErrorMessage(ex.getMessage())
//                errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                errorResponse.setTimeStamp(LocalDateTime.now())
//                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
//        }
}
