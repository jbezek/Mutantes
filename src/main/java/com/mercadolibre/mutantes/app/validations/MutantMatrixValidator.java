package com.mercadolibre.mutantes.app.validations;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MutantMatrixValidator implements ConstraintValidator<MutantMatrix, List<String>>{

	@Override
	public boolean isValid(List<String> matrix, ConstraintValidatorContext context) {

		return matrix != null && !matrix.isEmpty() && matrix.stream().noneMatch(row -> row.length() != matrix.size());
		
	}
	
}
