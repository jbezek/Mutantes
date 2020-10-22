package com.mercadolibre.mutantes.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mercadolibre.mutantes.app.model.dao.HumanDAO;
import com.mercadolibre.mutantes.app.model.documents.Human;
import com.mercadolibre.mutantes.app.vo.MutantMatrixValueObject;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class MutantRestControllerTest {

	@Autowired
	private WebTestClient client;

	@MockBean
	HumanDAO humanDAO;

	@Test
	void testEvaluateMutant() throws Exception {
		
		MutantMatrixValueObject mutantMatrixVO = new MutantMatrixValueObject();
		
		List<String> dnaList = new ArrayList<String>();
		dnaList.add("AAAA");
		dnaList.add("AAER");
		dnaList.add("AXAV");
		dnaList.add("ADSA");
		
		mutantMatrixVO.setDna(dnaList);
		
		Human human = new Human();
		human.setDna(dnaList);
		human.setMutant(Boolean.TRUE);
 
        Mockito.when(humanDAO.save(Mockito.any(Human.class))).thenReturn(Mono.just(human));
        Mockito.when(humanDAO.findByDna(dnaList)).thenReturn(Mono.empty());
		
		this.client.post().uri("/mutant")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(mutantMatrixVO), MutantMatrixValueObject.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.human.isMutant").isEqualTo(Boolean.TRUE);
	}
	
	@Test
	void testEvaluateExistentMutant() throws Exception {
		MutantMatrixValueObject mutantMatrixVO = new MutantMatrixValueObject();
		
		List<String> dnaList = new ArrayList<String>();
		dnaList.add("AAAA");
		dnaList.add("AAER");
		dnaList.add("AXAV");
		dnaList.add("ADSA");
		
		mutantMatrixVO.setDna(dnaList);
		
		Human human = new Human();
		human.setDna(dnaList);
		human.setMutant(Boolean.TRUE);
 
        Mockito.when(humanDAO.findByDna(dnaList)).thenReturn(Mono.just(human));
		
		this.client.post().uri("/mutant")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(mutantMatrixVO), MutantMatrixValueObject.class)
			.exchange()
			.expectStatus().isForbidden()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody();
	}
	
	@Test
	void testEvaluateMalformedDNAMutant() throws Exception {
		MutantMatrixValueObject mutantMatrixVO = new MutantMatrixValueObject();
		
		List<String> dnaList = new ArrayList<String>();
		dnaList.add("AAAA");
		dnaList.add("AAE");
		dnaList.add("AXAV");
		dnaList.add("SA");
		
		mutantMatrixVO.setDna(dnaList);
		
		this.client.post().uri("/mutant")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(mutantMatrixVO), MutantMatrixValueObject.class)
			.exchange()
			.expectStatus().isForbidden()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody();
	}

	@Test
	void testStats() throws Exception {

		Mockito.when(humanDAO.countByMutant(Boolean.TRUE)).thenReturn(Mono.just(8L));
		Mockito.when(humanDAO.countByMutant(Boolean.FALSE)).thenReturn(Mono.just(2L));
		
		this.client.get().uri("/stats").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
				.jsonPath("$.ratio").isEqualTo(0.8);
	}
}
