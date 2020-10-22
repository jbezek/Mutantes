package com.mercadolibre.mutantes.app.exceptions;

public class BusinessException extends Exception {

	String detailedMessage;
	
	public BusinessException(String message) {
		super(message);
		this.setDetailedMessage(message);
	}
	
	public String getDetailedMessage() {
		return detailedMessage;
	}

	public void setDetailedMessage(String detailedMessage) {
		this.detailedMessage = detailedMessage;
	}

}
