package de.sinnix.judoturnier.adapter.secondary;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class UuidGenerator implements IdentifierGenerator {
	@Override
	public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) {
		if (object instanceof AbstractEntity) {
			AbstractEntity entity = (AbstractEntity) object;
			// Wenn die UUID null ist, wird eine neue generiert
			if (entity.getUuid() == null || entity.getUuid().isEmpty()) {
				return UUID.randomUUID().toString();
			}

			// Wenn bereits eine UUID vorhanden ist, wird diese zurückgegeben
			return entity.getUuid();
		}
		// Standard-UUID generieren, falls nicht AbstractEntity
		return UUID.randomUUID().toString();
	}
}
