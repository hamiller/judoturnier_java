package de.sinnix.judoturnier.adapter.secondary;

import de.sinnix.judoturnier.model.Bewerter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BewerterRepository {
	@Autowired
	private BewerterJpaRepository bewerterJpaRepository;
	@Autowired
	private BewerterConverter bewerterConverter;

	public Bewerter findById(String bewerterUUID) {
		return bewerterJpaRepository.findById(bewerterUUID).map(jpa -> bewerterConverter.convertToBewerter(jpa)).orElseThrow();
	}
}
