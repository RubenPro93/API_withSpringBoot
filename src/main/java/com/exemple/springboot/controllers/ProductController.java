package com.exemple.springboot.controllers;

import com.exemple.springboot.dto.ProductRecordDto;
import com.exemple.springboot.models.ProductModel;
import com.exemple.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController  // Indica que esta classe é um controlador Spring MVC que lida com solicitações HTTP
public class ProductController {
    @Autowired  // Anotação de injeção de dependência - injeta uma instância de ProductRepository automaticamente
    ProductRepository productRepository;

    // Método POST mapeado para "/products" que cria e salva um novo produto
    @PostMapping("/products")
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto){
        // Cria uma instância de ProductModel e copia os dados do DTO recebido
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        // Retorna uma resposta com o objeto do produto recém-criado e o status HTTP 201 (CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    // Método GET mapeado para "/products" que recupera todos os produtos
    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAllProducts(){
        // Recupera a lista de todos os produtos do repositório
        List<ProductModel> productsList = productRepository.findAll();
        // Adiciona links de auto-relação (HATEOAS) a cada produto, se a lista não estiver vazia
        if (!productsList.isEmpty()){
            for (ProductModel product : productsList){
                UUID id = product.getIdProduct();
                product.add(linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel());
            }
        }
        // Retorna uma resposta com a lista de produtos e o status HTTP 200 (OK)
        return ResponseEntity.status(HttpStatus.OK).body(productsList);
    }

    // Método GET mapeado para "/products/{id}" que recupera um único produto com base em seu ID
    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") UUID id){
        // Tenta encontrar um produto com o ID fornecido
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()){
            // Se o produto não for encontrado, retorna uma resposta com status HTTP 404 (NOT FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        // Adiciona um link para a lista de produtos e retorna uma resposta com o produto encontrado
        product0.get().add(linkTo(methodOn(ProductController.class).getAllProducts()).withRel("Products list"));
        return ResponseEntity.status(HttpStatus.OK).body(product0.get());
    }

    // Método PUT mapeado para "/products/{id}" que atualiza um produto existente com base em seu ID
    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id, @RequestBody @Valid ProductRecordDto productRecordDto){
        // Tenta encontrar um produto com o ID fornecido
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) {
            // Se o produto não for encontrado, retorna uma resposta com status HTTP 404 (NOT FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        // Copia os dados do DTO recebido para o produto existente e o salva
        var productModel = product0.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        // Retorna uma resposta com o produto atualizado e status HTTP 200 (OK)
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }

    // Método DELETE mapeado para "/products/{id}" que exclui um produto com base em seu ID
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        // Tenta encontrar um produto com o ID fornecido
        Optional<ProductModel> product0 = productRepository.findById(id);
        if (product0.isEmpty()) {
            // Se o produto não for encontrado, retorna uma resposta com status HTTP 404 (NOT FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        // Exclui o produto encontrado e retorna uma resposta com status HTTP 200 (OK)
        productRepository.delete(product0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Product Deleted successfully");
    }
}

