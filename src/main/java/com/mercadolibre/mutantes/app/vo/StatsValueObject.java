package com.mercadolibre.mutantes.app.vo;

public class StatsValueObject {

	private Long count_mutant_dna;
	private Long count_human_dna;
	private Float ratio;
	
	public StatsValueObject() {
	}

	public StatsValueObject(Long count_mutant_dna, Long count_human_dna, Float ratio) {
		super();
		this.count_mutant_dna = count_mutant_dna;
		this.count_human_dna = count_human_dna;
		this.ratio = ratio;
	}

	public Long getCount_mutant_dna() {
		return count_mutant_dna;
	}

	public void setCount_mutant_dna(Long count_mutant_dna) {
		this.count_mutant_dna = count_mutant_dna;
	}

	public Long getCount_human_dna() {
		return count_human_dna;
	}

	public void setCount_human_dna(Long count_human_dna) {
		this.count_human_dna = count_human_dna;
	}

	public Float getRatio() {
		return ratio;
	}

	public void setRatio(Float ratio) {
		this.ratio = ratio;
	}
}
