package com.utp.colors.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import com.utp.colors.model.Color;
import com.utp.colors.service.ColorService;

@RestController
@RequestMapping("/colors")
public class ColorController {
	
	@Autowired
	private ColorService colorService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	ResponseEntity<Color> createColor(@RequestBody Color color){
		
		if(colorService.isValidHexColorAndPantoneValue(color.getColor(), color.getPantoneValue())) {
			color.setColor(color.getColor().replace("#", ""));
			Color colorGuardado = colorService.saveColor(color);
			colorGuardado.setColor("#"+color.getColor());
			return ResponseEntity.status(HttpStatus.CREATED).body(colorGuardado);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(color);
	}
	
	@GetMapping
	ResponseEntity<Map<String, Object>> getAllColors(@RequestParam int page, @RequestHeader(value = "Accept") String acceptHeader){
		
		try {
			List<Color> colors = new ArrayList<Color>();
			Pageable pageable = PageRequest.of(page, 3);
			Page<Color> pageColors = colorService.getAllColors(pageable);
			
			colors = pageColors.getContent();
			
			Map<String, Object> response  = new HashMap<>();
			response.put("colors", colors);
			response.put("pages", pageColors.getTotalPages());
			response.put("nextPage", "http://localhost:8081/colors?page="+pageColors.nextOrLastPageable().getPageNumber());
			response.put("previousPage", "http://localhost:8081/colors?page="+pageColors.previousOrFirstPageable().getPageNumber());
			
			HttpHeaders headers = getHeaders(acceptHeader);
			return new ResponseEntity<>(response,headers, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	ResponseEntity<Color> findById(@PathVariable Long id, @RequestHeader(value = "Accept") String acceptHeader){
		
		Color color = colorService.findById(id).get();
		HttpHeaders headers = getHeaders(acceptHeader); 
		return new ResponseEntity<>(color,headers, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	ResponseEntity<String> deleteColor(@PathVariable Long id){
		colorService.deleteColor(id);
		return ResponseEntity.ok("Color eliminado con éxito");
	}
	
	@PutMapping("/{id}")
	ResponseEntity<?> updateColor(@PathVariable long id, @RequestBody Color color){
		Color colorData = colorService.findById(id).get();
	    if (colorService.isValidHexColorAndPantoneValue(color.getColor(), color.getPantoneValue())) {
	    	
	    	color.setColor(color.getColor().replace("#", ""));
	    	colorData.setName(color.getName());
	    	colorData.setColor(color.getColor());
	    	colorData.setPantoneValue(color.getPantoneValue());
	    	colorData.setYear(color.getYear());
			Color colorActualizado = colorService.updateColor(colorData);
			colorActualizado.setColor("#"+color.getColor());

			return new ResponseEntity<>(colorActualizado,HttpStatus.OK);
	    } else {
	        // Devuelve una respuesta BAD_REQUEST con un mensaje de error
	        String mensajeError = "Color y PantoneValue no son válidos";
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensajeError);
	    }
	}
	
	private HttpHeaders getHeaders(String acceptHeader) {
		HttpHeaders  headers = new HttpHeaders();
		
		if(acceptHeader != null && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
			headers.setContentType(MediaType.APPLICATION_JSON);
		}else {
			headers.setContentType(MediaType.APPLICATION_XML);
		}
		
		return headers;
	}
}
