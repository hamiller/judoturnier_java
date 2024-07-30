package de.sinnix.judoturnier.application;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Helpers {

	/**
	 * Splittet ein Array in die gewünschte Anzahl von Teilen
	 *
	 * @param list
	 * @param parts
	 * @return
	 * @param <T>
	 */
	public <T> List<List<T>> splitArrayToParts(List<T> list, Integer parts) {
		int size = list.size();
		int minPartSize = size / parts;
		int extraElements = size % parts;

		List<List<T>> result = new ArrayList<>();

		int start = 0;
		for (int i = 0; i < parts; i++) {
			int currentPartSize = minPartSize + (i < extraElements ? 1 : 0);
			int end = start + currentPartSize;

			if (start < size) {
				result.add(new ArrayList<>(list.subList(start, Math.min(end, size))));
			} else {
				result.add(new ArrayList<>()); // Leere Liste, wenn keine Elemente mehr übrig sind
			}
			start = end;
		}

		return result;
	}
}
