package de.sinnix.judoturnier.adapter.secondary;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "wettkampfgruppe")
public class WettkampfGruppeJpa {
	@Id
	Integer id;
	String             name;
	String             typ;
	@OneToMany
	@JoinColumn(name = "begegnungen")
	List<BegegnungJpa> begegnungen;
}
