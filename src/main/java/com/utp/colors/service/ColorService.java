package com.utp.colors.service;




import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.utp.colors.model.Color;

public interface ColorService {
	
	Color saveColor(Color color);
	Page<Color> getAllColors(Pageable pageable);
	Optional<Color> findById(Long id);
	Color updateColor(Color color);
	void deleteColor(Long id);
	boolean isValidHexColorAndPantoneValue(String color, String pantoneValue);
	
}
