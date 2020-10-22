package com.mercadolibre.mutantes.app.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadolibre.mutantes.app.MutantTranslator;
import com.mercadolibre.mutantes.app.exceptions.BusinessException;
import com.mercadolibre.mutantes.app.model.dao.HumanDAO;
import com.mercadolibre.mutantes.app.model.documents.Human;
import com.mercadolibre.mutantes.app.utils.DnaUtils;
import com.mercadolibre.mutantes.app.utils.MessageResourceProvider;
import com.mercadolibre.mutantes.app.vo.HumanValueObject;
import com.mercadolibre.mutantes.app.vo.MutantMatrixValueObject;
import com.mercadolibre.mutantes.app.vo.StatsValueObject;

import reactor.core.publisher.Mono;

@Service
public class MutantServiceImpl implements MutantService {

	private static final Logger logger = LoggerFactory.getLogger(MutantServiceImpl.class);

	@Autowired
	private HumanDAO humanDAO;

	@Autowired
	protected MessageResourceProvider messageSourceProvider;

	@Override
	public Mono<HumanValueObject> evaluateMutant(MutantMatrixValueObject mutantMatrixVO) {

		return humanDAO.findByDna(mutantMatrixVO.getDna())
				.flatMap(h -> Mono.error(new BusinessException(messageSourceProvider.get("dna.nonUnique"))))
				.switchIfEmpty(Mono.defer(() -> {

					String[] arrayMutante = mutantMatrixVO.getDna().toArray(new String[0]);
					Boolean isMutant = DnaUtils.isMutant(arrayMutante);
					Human human = new Human();
					human.setMutant(isMutant);
					human.setDna(mutantMatrixVO.getDna());

					return humanDAO.save(human).onErrorResume(e -> {
						logger.error(e.getMessage());
						return Mono.error(new BusinessException(messageSourceProvider.get("error.general")));
					});

				}))
				.cast(Human.class).map(MutantTranslator.getInstance()::getValueObject);

	}

	@Override
	public Mono<StatsValueObject> getStats() {
		Mono<Long> humans = humanDAO.countByMutant(false);
		Mono<Long> mutants = humanDAO.countByMutant(true);

		Mono<StatsValueObject> result = humans.zipWith(mutants, (human, mutant) -> {
			Float ratio = mutant + human > 0 ? Float.valueOf(mutant) / Float.valueOf(mutant + human) : 0F;
			return new StatsValueObject(mutant, human, ratio);
		});
		return result;
	}

}
