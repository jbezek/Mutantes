package com.mercadolibre.mutantes.app.utils;

import java.util.stream.IntStream;

public class DnaUtils {

	public static final Integer MUTANT_SEQUENCE_LENGHT = 4;
	
	public static Boolean isMutant(String[] mutantMatrix) {
		return IntStream.range(0, mutantMatrix.length).map(i -> getDnaCount(i, mutantMatrix)).sum() >= 2;
	}

	private static Integer getDnaCount(Integer rowIndex, String[] mutantMatrix) {
		return IntStream.range(0, mutantMatrix[rowIndex].length())
				.map(charIndex -> getDnaCount(charIndex, rowIndex, mutantMatrix)).sum();
	}

	private static Integer getDnaCount(Integer charIndex, Integer rowIndex, String[] mutantMatrix) {
		Integer horizontalCount = isHorizontalMutantSequence(charIndex, mutantMatrix[rowIndex]) ? 1 : 0;
		Integer verticalCount = isVerticalMutantSequence(charIndex, rowIndex, mutantMatrix) ? 1 : 0;
		Integer diagonalCount = isDiagonalMutantSequence(charIndex, rowIndex, mutantMatrix) ? 1 : 0;
		Integer inverseDiagonalCount = isInverseDiagonalMutantSequence(charIndex, rowIndex, mutantMatrix) ? 1 : 0;
		return horizontalCount + verticalCount + diagonalCount + inverseDiagonalCount;
	}

	private static Boolean isHorizontalMutantSequence(Integer charIndex, String row) {
		return (row.length() - charIndex) >= MUTANT_SEQUENCE_LENGHT && IntStream
				.range(charIndex, MUTANT_SEQUENCE_LENGHT).allMatch(index -> row.charAt(index) == row.charAt(charIndex));
	}

	private static Boolean isVerticalMutantSequence(Integer charIndex, Integer rowIndex, String[] mutantMatrix) {
		return (mutantMatrix.length - rowIndex) >= MUTANT_SEQUENCE_LENGHT
				&& IntStream.range(rowIndex, MUTANT_SEQUENCE_LENGHT).allMatch(
						index -> mutantMatrix[index].charAt(charIndex) == mutantMatrix[rowIndex].charAt(charIndex));
	}

	private static Boolean isDiagonalMutantSequence(Integer charIndex, Integer rowIndex, String[] mutantMatrix) {
		return (mutantMatrix[0].length() - charIndex) >= MUTANT_SEQUENCE_LENGHT
				&& (mutantMatrix.length - rowIndex) >= MUTANT_SEQUENCE_LENGHT
				&& IntStream.range(rowIndex, MUTANT_SEQUENCE_LENGHT).allMatch(index -> mutantMatrix[index]
						.charAt(charIndex + index) == mutantMatrix[rowIndex].charAt(charIndex));
	}

	private static Boolean isInverseDiagonalMutantSequence(Integer charIndex, Integer rowIndex, String[] mutantMatrix) {
		return (charIndex + 1) >= MUTANT_SEQUENCE_LENGHT
				&& (mutantMatrix.length - rowIndex) >= MUTANT_SEQUENCE_LENGHT
				&& IntStream.range(rowIndex, MUTANT_SEQUENCE_LENGHT).allMatch(index -> mutantMatrix[index]
						.charAt(charIndex - index) == mutantMatrix[rowIndex].charAt(charIndex));
	}
	
}
