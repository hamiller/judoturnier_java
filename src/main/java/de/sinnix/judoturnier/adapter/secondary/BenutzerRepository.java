package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Benutzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class BenutzerRepository {

	@Autowired
	private BenutzerJpaRepository benutzerJpaRepository;
	@Autowired
	private BenutzerConverter     benutzerConverter;


	public Benutzer getBenutzer(UUID benutzerId) {
		return benutzerJpaRepository.findById(benutzerId.toString())
			.map(jpa -> benutzerConverter.convertToBenutzer(jpa))
			.orElseThrow();
	}
}
