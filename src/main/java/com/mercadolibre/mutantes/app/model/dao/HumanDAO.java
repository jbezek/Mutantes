package com.mercadolibre.mutantes.app.model.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.mercadolibre.mutantes.app.model.documents.Human;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HumanDAO extends ReactiveMongoRepository<Human, String>{

	public Mono<Human> findByDna(List<String> dna);
	
	public Flux<Human> findByMutant(Boolean isMutant);
	
	public Mono<Long> countByMutant(Boolean mutant);
	
}
