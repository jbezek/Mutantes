package com.mercadolibre.mutantes.app.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.mercadolibre.mutantes.app.exceptions.BusinessException;
import com.mercadolibre.mutantes.app.services.MutantService;
import com.mercadolibre.mutantes.app.utils.MessageResourceProvider;
import com.mercadolibre.mutantes.app.vo.MutantMatrixValueObject;
import com.mercadolibre.mutantes.app.vo.StatsValueObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class MutanteRestController {

	private static final Logger logger = LoggerFactory.getLogger(MutanteRestController.class);

	@Autowired
	private MutantService mutantService;

	@Autowired
	protected MessageResourceProvider messageSourceProvider;

	@Operation(description = "Verifies the given DNA and determine if it belongs to a mutant", parameters = {
			@Parameter(name = "dna", required = true, description = "DNA for verification.") })
	@PostMapping("/mutant")
	public Mono<ResponseEntity<Map<String, Object>>> mutant(
			@Valid @RequestBody Mono<MutantMatrixValueObject> monoMutantMatrixVO) {

		Map<String, Object> response = new HashMap<String, Object>();
		return monoMutantMatrixVO.flatMap(mutantMatrixVO -> {
			return mutantService.evaluateMutant(mutantMatrixVO).map(humanVO -> {
				response.put("human", humanVO);
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
			});
		})
		.doOnNext(h -> logger.info("POST mutant success"))
		.onErrorResume(BusinessException.class, e -> {
			logger.info(e.getDetailedMessage());
			response.put("error", e.getDetailedMessage());
			return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response));
		}).onErrorResume(WebExchangeBindException.class, e -> {
			return Mono.just(e.getFieldErrors()).flatMapMany(fe -> Flux.fromIterable(fe))
					.map(fieldError -> {
						logger.error(fieldError.getDefaultMessage());
						return messageSourceProvider.get(fieldError.getDefaultMessage());
					})
					.collectList()
					.flatMap(list -> {
						response.put("error", list);
						return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response));
					});
		}).onErrorResume(Exception.class, e -> {
			logger.error(e.getMessage());
			return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).build());
		});

	}

	@Operation(description = "Get stats of verified human and mutant DNAs.")
	@GetMapping("/stats")
	public Mono<ResponseEntity<StatsValueObject>> stats() {
		return mutantService.getStats().map(stat -> ResponseEntity.status(HttpStatus.OK).body(stat))
				.doOnNext(stat -> logger.info("GET stats success"))
				.onErrorResume(t -> {
					logger.error(t.getMessage());
					return Mono.just(new ResponseEntity<>(HttpStatus.FORBIDDEN));
				});
	}

}
