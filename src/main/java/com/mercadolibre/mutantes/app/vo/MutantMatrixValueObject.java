package com.mercadolibre.mutantes.app.vo;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mercadolibre.mutantes.app.validations.MutantMatrix;

public class MutantMatrixValueObject {
	
//	private String[] dna;
//
//	public String[] getDna() {
//		return dna;
//	}
//
//	public void setDna(String[] dna) {
//		this.dna = dna;
//	}

	@MutantMatrix
	private List<String> dna;

	public List<String> getDna() {
		return dna;
	}

	public void setDna(List<String> dna) {
		this.dna = dna;
	}

}
