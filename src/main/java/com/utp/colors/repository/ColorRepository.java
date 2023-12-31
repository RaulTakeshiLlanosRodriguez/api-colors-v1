package com.utp.colors.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utp.colors.model.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long>{
	
	@Query("SELECT c FROM Color c ORDER BY c.updatedAt DESC")
	Page<Color> findAllOrderByDateDesc(Pageable pageable);
	
}
