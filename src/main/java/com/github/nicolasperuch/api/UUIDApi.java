package com.github.nicolasperuch.api;

import com.github.nicolasperuch.service.UUIDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UUIDApi {

	@Autowired
	private UUIDService service;

	//I know its a get method who creates an uuid and it should be an post and bla bla bla
	//but I am not worried about that now
	@GetMapping("/generate/uuid/{value}")
	public Mono<String> generateUUID(@PathVariable String value) {
		return service.generateUUID(value);
	}
}


