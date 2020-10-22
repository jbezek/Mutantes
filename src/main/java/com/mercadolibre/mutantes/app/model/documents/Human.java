package com.mercadolibre.mutantes.app.model.documents;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="humans")
public class Human implements Serializable {

	@Id
	private String id;
	
	private List<String> dna;

	private Boolean mutant;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getDna() {
		return dna;
	}

	public void setDna(List<String> dna) {
		this.dna = dna;
	}

	public Boolean getMutant() {
		return mutant;
	}

	public void setMutant(Boolean mutant) {
		this.mutant = mutant;
	}
}
