package model.exception;

//EXCE��O PERSONALIZADA para evitar os erros que explodem de forma abrupta no console
public class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// Exce��o personalizada
	public DomainException(String msg) {
		super(msg);
	}
}
