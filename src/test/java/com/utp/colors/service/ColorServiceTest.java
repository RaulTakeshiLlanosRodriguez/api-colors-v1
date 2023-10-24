package com.utp.colors.service;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.utp.colors.model.Color;
import com.utp.colors.repository.ColorRepository;
import com.utp.colors.service.implementation.ColorServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTest {
	
	@Mock
	private ColorRepository colorRepository;
	
	@InjectMocks
	private ColorServiceImpl colorService;
	
	private Color color;
	
	@BeforeEach
	void setUp() {
		color = Color.builder()
				.id(1L)
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4200")
				.year(Year.of(2023))
				.build();
	}
	
	@DisplayName("Test para guardar un color")
	@Test
	void testSaveColor() {
		given(colorRepository.save(color)).willReturn(color);
		
		Color colorGuardado = colorService.saveColor(color);
		
		assertThat(colorGuardado).isNotNull();
	}
	
	@DisplayName("Test para listar colores")
	@Test
	void testGetColors() {
		Color color2 = Color.builder()
				.id(2L)
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4200")
				.year(Year.of(2024))
				.build();
		
		List<Color> colorsList = List.of(color,color2);
		Pageable pageable = PageRequest.of(0, 3);
	    Page<Color> page = new PageImpl<Color>(colorsList,pageable, colorsList.size());

	    given(colorRepository.findAllOrderByDateDesc(any(Pageable.class))).willReturn(page);

	    Page<Color> colors = colorService.getAllColors(pageable);
		
		assertThat(colors.getContent()).isNotNull();
		assertThat(colors.getNumberOfElements()).isEqualTo(2);
	}
	
	@DisplayName("Test para obtener un color por ID")
	@Test
	void testGetColorById() {
		given(colorRepository.findById(1L)).willReturn(Optional.of(color));
		
		Color colorObtenido = colorService.findById(color.getId()).get();
		
		assertThat(colorObtenido).isNotNull();
	}
	
	@DisplayName("Test para actualizar un color")
	@Test
	void testUpdateColor() {
		
		given(colorRepository.findById(color.getId())).willReturn(Optional.of(color));
		given(colorRepository.save(color)).willReturn(color);
		color.setColor("FFFFFF");
		color.setName("red");
		
		Color colorActualizado = colorService.updateColor(color);
		
		assertThat(colorActualizado).isNotNull();
		assertThat(colorActualizado.getColor()).isEqualTo("FFFFFF");
		assertThat(colorActualizado.getName()).isEqualTo("red");
	}
	
	@DisplayName("Test para eliminar un color")
	@Test
	void testEliminarColor() {
		
		long colorId = 1L;
		willDoNothing().given(colorRepository).deleteById(colorId);
		
		colorService.deleteColor(colorId);
		
		verify(colorRepository, times(1)).deleteById(colorId);
	}
}
