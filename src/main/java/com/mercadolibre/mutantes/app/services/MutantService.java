package com.mercadolibre.mutantes.app.services;

import com.mercadolibre.mutantes.app.vo.HumanValueObject;
import com.mercadolibre.mutantes.app.vo.MutantMatrixValueObject;
import com.mercadolibre.mutantes.app.vo.StatsValueObject;

import reactor.core.publisher.Mono;

public interface MutantService {

	public Mono<HumanValueObject> evaluateMutant(MutantMatrixValueObject mutantMatrixVO);
	
	public Mono<StatsValueObject> getStats();
	
}
