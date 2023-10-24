package com.utp.colors.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.utp.colors.model.Color;
import com.utp.colors.repository.ColorRepository;
import com.utp.colors.service.ColorService;

@Service
public class ColorServiceImpl implements ColorService{
	
	@Autowired
	private ColorRepository colorRepository;

	@Override
	public Color saveColor(Color color) {
		return colorRepository.save(color);
	}

	@Override
	public Page<Color> getAllColors(Pageable pageable) {
		
		Page<Color> page = colorRepository.findAllOrderByDateDesc(pageable);
		page.forEach(producto -> {
			producto.setColor("#"+producto.getColor());
		});
		
		return page;
	}

	@Override
	public Optional<Color> findById(Long id) {
		return colorRepository.findById(id);
	}

	@Override
	public void deleteColor(Long id) {
		colorRepository.deleteById(id);
	}

	@Override
	public Color updateColor(Color color) {
		Color colorData = colorRepository.findById(color.getId()).get();
		return colorRepository.save(colorData);
	}
	
	@Override
	public boolean isValidHexColorAndPantoneValue(String color, String pantoneValue) {
        if ((color == null || !color.matches("^#[0-9A-Fa-f]{6}$")) || (pantoneValue == null || !pantoneValue.matches("\\d{2}-\\d{4}"))) {
            return false;
        }
        return true;
    }
	
}
