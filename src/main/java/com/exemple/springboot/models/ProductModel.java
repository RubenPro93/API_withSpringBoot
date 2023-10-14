package com.exemple.springboot.models;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity  // Anotação que marca esta classe como uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Table(name = "TB_PRODUCTS")  // Especifica o nome da tabela no banco de dados
public class ProductModel extends RepresentationModel<ProductModel> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id  // Indica que o campo abaixo é a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO)  // Define a estratégia de geração automática do valor da chave primária
    private UUID idProduct;  // Identificador único do produto

    private String name;  // Nome do produto
    private BigDecimal value;  // Valor do produto

    public UUID getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(UUID idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

