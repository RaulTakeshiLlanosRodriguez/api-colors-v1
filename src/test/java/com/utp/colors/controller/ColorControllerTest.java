package com.utp.colors.controller;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.colors.model.Color;
import com.utp.colors.service.ColorService;

@WebMvcTest(ColorController.class)
public class ColorControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ColorService colorService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	void testSaveColor() throws Exception {
		Color color = Color.builder()
				.id(1L)
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4201")
				.year(Year.of(2024))
				.build();
		given(colorService.isValidHexColorAndPantoneValue(color.getColor(), color.getPantoneValue())).willReturn(true);
		given(colorService.saveColor(any(Color.class)))
		.willReturn(color);
		
		ResultActions response = mockMvc.perform(post("/colors")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(color)));
		
		response.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.name", is(color.getName())))
		.andExpect(jsonPath("$.color", is(color.getColor())))
		.andExpect(jsonPath("$.pantoneValue", is(color.getPantoneValue())))
		.andExpect(jsonPath("$.year", is(Integer.toString(color.getYear().getValue()))));					
	}
	
	@Test
	void testGetColorById() throws Exception {
		
		long colorId = 1L;
		Color colorPrueba = Color.builder()
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4201")
				.year(Year.of(2024))
				.build();
		given(colorService.findById(colorId)).willReturn(Optional.of(colorPrueba));
		
		ResultActions response = mockMvc.perform(get("/colors/{id}",colorId)
				.header("Accept", "application/json, application/xml"));
		response.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.name", is(colorPrueba.getName())))
		.andExpect(jsonPath("$.color", is(colorPrueba.getColor())))
		.andExpect(jsonPath("$.pantoneValue", is(colorPrueba.getPantoneValue())))
		.andExpect(jsonPath("$.year", is(Integer.toString(colorPrueba.getYear().getValue()))));
	}
	
	@Test
	void testUpdateColor() throws Exception {
		long colorId = 1L;
		Color colorPrueba = Color.builder()
				.name("fuchsia rose")
				.color("C74375")
				.pantoneValue("15-4201")
				.year(Year.of(2024))
				.build();
		Color colorActualizado = Color.builder()
				.name("cerulean")
				.color("#98B2D1")
				.pantoneValue("15-4202")
				.year(Year.of(2024))
				.build();
		
		given(colorService.findById(colorId)).willReturn(Optional.of(colorPrueba));
		given(colorService.isValidHexColorAndPantoneValue(colorActualizado.getColor(), colorActualizado.getPantoneValue())).willReturn(true);
		given(colorService.updateColor(any(Color.class))).willReturn(colorActualizado);
		
		ResultActions response = mockMvc.perform(put("/colors/{id}",colorId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(colorActualizado)));
		
		response.andExpect(status().isOk())
		.andDo(print())
		.andExpect(jsonPath("$.name", is(colorActualizado.getName())))
		.andExpect(jsonPath("$.color", is(colorActualizado.getColor())))
		.andExpect(jsonPath("$.pantoneValue", is(colorActualizado.getPantoneValue())))
		.andExpect(jsonPath("$.year", is(Integer.toString(colorActualizado.getYear().getValue()))));
	}
	
	@Test
	void testGetAllColors() throws Exception {
		Color color = Color.builder()
				.id(1L)
				.name("cerulean")
				.color("#98B2D1")
				.pantoneValue("15-4202")
				.year(Year.of(2024))
				.build();
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
	    given(colorService.getAllColors(any(Pageable.class))).willReturn(page);
	    Page<Color> colors = colorService.getAllColors(pageable);
	    
	    ResultActions response = mockMvc.perform(get("/colors?page=0")
	    		.header("Accept", "application/json, application/xml"));
	    
	    response.andExpect(status().isOk())
	    .andDo(print())
	    .andExpect(jsonPath("$.pages", is(colors.getTotalPages())));
	}
	
	@Test
	void testDeleteColor() throws Exception {
		
		long colorId = 1L;
		willDoNothing().given(colorService).deleteColor(colorId);
		
		ResultActions response = mockMvc.perform(delete("/colors/{id}",colorId));
		
		response.andExpect(status().isOk())
		.andDo(print());
	}
	
}
