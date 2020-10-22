package com.mercadolibre.mutantes.app.vo;

import java.io.Serializable;
import java.util.List;

public class HumanValueObject implements Serializable {

	private String id;

	private Boolean isMutant;
	
	private List<String> dna;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public Boolean getIsMutant() {
		return isMutant;
	}

	public void setIsMutant(Boolean isMutant) {
		this.isMutant = isMutant;
	}
	
	public List<String> getDna() {
		return dna;
	}

	public void setDna(List<String> dna) {
		this.dna = dna;
	}

}
