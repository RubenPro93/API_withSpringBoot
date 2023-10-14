package com.exemple.springboot.repositories;

import com.exemple.springboot.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository  // Anotação que marca esta interface como um componente Spring
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    // Esta interface estende JpaRepository e trabalha com entidades do tipo ProductModel,
    // usando UUID como o tipo de dado para a chave primária

    // Métodos de acesso a dados (como findAll, findById, save) são herdados da JpaRepository
}
