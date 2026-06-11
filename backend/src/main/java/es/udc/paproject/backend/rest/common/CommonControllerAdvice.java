package es.udc.paproject.backend.rest.common;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.udc.paproject.backend.model.exceptions.CollectedTicketsException;
import es.udc.paproject.backend.model.exceptions.DuplicateInstanceException;
import es.udc.paproject.backend.model.exceptions.IncorrectCreditCardException;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.IncorrectLoginException;
	import es.udc.paproject.backend.model.exceptions.IncorrectPasswordException;
	import es.udc.paproject.backend.model.exceptions.InvalidSearchDateException;
import es.udc.paproject.backend.model.exceptions.LocalitiesExceededException;
import es.udc.paproject.backend.model.exceptions.MovieAlreadyStartedException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@SuppressWarnings("null")
public class CommonControllerAdvice {
	
	private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
	private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";
	private final static String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";
	private final static String INVALID_SEARCH_DATE_EXCEPTION_CODE = "project.exceptions.InvalidSearchDateException";
	private final static String MOVIE_ALREADY_STARTED_EXCEPTION_CODE = "project.exceptions.MovieAlreadyStartedException";
	private final static String LOCALITIES_EXCEEDED_EXCEPTION_CODE = "project.exceptions.LocalitiesExceededException";
	private final static String COLLECTED_TICKETS_EXCEPTION_CODE = "project.exceptions.CollectedTicketsException";
	private final static String INCORRECT_CREDIT_CARD_EXCEPTION_CODE = "project.exceptions.IncorrectCreditCardException";
	private final static String INCORRECT_LOGIN_EXCEPTION_CODE = "project.exceptions.IncorrectLoginException";
	private final static String INCORRECT_PASSWORD_EXCEPTION_CODE = "project.exceptions.IncorrectPasswordException";
	
	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
				
		List<FieldErrorDto> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
			.map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage())).collect(Collectors.toList());
		
		return new ErrorsDto(fieldErrors);
	    
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleConstraintViolationException(ConstraintViolationException exception) {

		List<FieldErrorDto> fieldErrors = exception.getConstraintViolations().stream()
			.map(error -> new FieldErrorDto(error.getPropertyPath().toString(), error.getMessage()))
			.collect(Collectors.toList());

		return new ErrorsDto(fieldErrors);

	}
	
	@ExceptionHandler(InstanceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();
		String exceptionName = String.valueOf(exception.getName());
		
		String nameMessage = messageSource.getMessage(exceptionName, null, exceptionName, currentLocale);
		String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, INSTANCE_NOT_FOUND_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@ExceptionHandler(DuplicateInstanceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();
		String exceptionName = String.valueOf(exception.getName());
		
		String nameMessage = messageSource.getMessage(exceptionName, null, exceptionName, currentLocale);
		String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE, 
				new Object[] {nameMessage, exception.getKey().toString()}, DUPLICATE_INSTANCE_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);
		
	}
	
	@ExceptionHandler(PermissionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ErrorsDto handlePermissionException(PermissionException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();
		
		String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE, null, PERMISSION_EXCEPTION_CODE,
			currentLocale);

		return new ErrorsDto(errorMessage);
		
	}

	@ExceptionHandler(InvalidSearchDateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleInvalidSearchDateException(InvalidSearchDateException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(INVALID_SEARCH_DATE_EXCEPTION_CODE,
			new Object[] {exception.getDate().toString()}, INVALID_SEARCH_DATE_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(MovieAlreadyStartedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleMovieAlreadyStartedException(MovieAlreadyStartedException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(MOVIE_ALREADY_STARTED_EXCEPTION_CODE,
			new Object[] {Long.toString(exception.getSessionId()), Long.toString(exception.getMovieId())},
			MOVIE_ALREADY_STARTED_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(LocalitiesExceededException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleLocalitiesExceededException(LocalitiesExceededException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(LOCALITIES_EXCEEDED_EXCEPTION_CODE, null,
			LOCALITIES_EXCEEDED_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(CollectedTicketsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleCollectedTicketsException(CollectedTicketsException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(COLLECTED_TICKETS_EXCEPTION_CODE, null,
			COLLECTED_TICKETS_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(IncorrectCreditCardException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorsDto handleIncorrectCreditCardException(IncorrectCreditCardException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(INCORRECT_CREDIT_CARD_EXCEPTION_CODE, null,
			INCORRECT_CREDIT_CARD_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(IncorrectLoginException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectLoginException(IncorrectLoginException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(INCORRECT_LOGIN_EXCEPTION_CODE, null,
			INCORRECT_LOGIN_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

	@ExceptionHandler(IncorrectPasswordException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorsDto handleIncorrectPasswordException(IncorrectPasswordException exception, Locale locale) {

		Locale currentLocale = locale != null ? locale : Locale.getDefault();

		String errorMessage = messageSource.getMessage(INCORRECT_PASSWORD_EXCEPTION_CODE, null,
			INCORRECT_PASSWORD_EXCEPTION_CODE, currentLocale);

		return new ErrorsDto(errorMessage);

	}

}
