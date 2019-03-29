package br.com.cr.rest.jerseyagendamentos.model.domain;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserialize extends JsonDeserializer<LocalDateTime> {

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		System.out.println("LocalDateDeserialize");
		System.out.println(p.getText());
		
		return LocalDateTime.parse(p.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

}
