package lsg.demo.xml2db.service;

public class BankEventProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BankEventProcessingException(Exception ex) {
		super(ex);
	}
	
	public BankEventProcessingException(String ex) {
		super(ex);
	}
}	