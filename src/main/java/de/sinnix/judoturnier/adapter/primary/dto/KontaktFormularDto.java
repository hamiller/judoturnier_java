package de.sinnix.judoturnier.adapter.primary.dto;

import lombok.ToString;

@ToString
public class KontaktFormularDto {
	private String name;
	private String email;
	private String message;

	// Getter und Setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
