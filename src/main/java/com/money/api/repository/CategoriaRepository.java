package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
