package com.utp.colors.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Year;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.utp.colors.model.Color;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ColorRepositoryTest {
	
	
	@Autowired
	private ColorRepository colorRepository;
	
	private Color color;
	
	@BeforeEach
	void setUp() {
		color = Color.builder()
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4200")
				.year(Year.of(2023))
				.build();
	}
	
	@DisplayName("Test para guardar un color")
	@Test
	void testSaveColor() {
		Color colorGuardado = colorRepository.save(color);
		assertThat(colorGuardado).isNotNull();
		assertThat(color.getId()).isGreaterThan(0);
	}
	
	@DisplayName("Test para listar colores")
	@Test
	void testGetAllColors() {
		Color color2 = Color.builder()
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4200")
				.year(Year.of(2024))
				.build();
		colorRepository.save(color);
		colorRepository.save(color2);
		
		Pageable pageable = PageRequest.of(0, 3);
		Page<Color> listColors = colorRepository.findAll(pageable);
		
		assertThat(listColors.getContent()).isNotNull();
		assertThat(listColors.getNumberOfElements()).isEqualTo(2);
	}
	
	@DisplayName("Test para obtener un color por ID")
	@Test
	void testGetColorById() {
		colorRepository.save(color);
		
		Color colorDB = colorRepository.findById(color.getId()).get();
		
		assertThat(colorDB).isNotNull();
	}
	
	@DisplayName("Test para actualizar un empleado")
	@Test
	void testUpdateColor() {
		colorRepository.save(color);
		
		Color colorGuardado = colorRepository.findById(color.getId()).get();
		colorGuardado.setName("fuchsia rose");
		colorGuardado.setColor("C74375");
		colorGuardado.setPantoneValue("15-4201");
		colorGuardado.setYear(Year.of(2025));
		
		Color colorActualizado = colorRepository.save(colorGuardado);
		
		assertThat(colorActualizado.getName()).isEqualTo("fuchsia rose");
		assertThat(colorActualizado.getColor()).isEqualTo("C74375");
		assertThat(colorActualizado.getPantoneValue()).isEqualTo("15-4201");
		assertThat(colorActualizado.getYear()).isEqualTo(Year.of(2025));
	}
	
	@DisplayName("Test para eliminar un color")
	@Test
	void testDeleteColor() {
		colorRepository.save(color);
		
		colorRepository.deleteById(color.getId());
		Optional<Color> colorOptional = colorRepository.findById(color.getId());
		
		assertThat(colorOptional).isEmpty();
	}
}
