package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.BenutzerRolle;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class BenutzerRolleListConverter implements AttributeConverter<List<BenutzerRolle>, String> {

	@Override
	public String convertToDatabaseColumn(List<BenutzerRolle> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		return attribute.stream()
			.map(BenutzerRolle::name)
			.collect(Collectors.joining(","));
	}

	@Override
	public List<BenutzerRolle> convertToEntityAttribute(String dbData) {
		if (dbData == null || dbData.isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.stream(dbData.split(","))
			.map(BenutzerRolle::valueOf)
			.collect(Collectors.toList());
	}
}
