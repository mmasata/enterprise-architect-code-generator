package com.myproject.service.rest;

import org.springframework.web.bind.annotations.RestController;
import org.springframework.web.bind.annotations.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.myproject.service.model.CreateProductResponse;
import com.myproject.service.model.GetProductDetailResponse;
import com.myproject.service.model.GetProductResponse;
import java.time.LocalDate;
import org.springframework.web.bind.annotations.PathVariable;
import org.springframework.web.bind.annotations.RequestHeader;
import org.springframework.web.bind.annotations.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/products")
public interface ProductController {

      // Override this method
      @RequestMapping(method=RequestMethod.GET)
      default ResponseEntity<Flux<GetProductResponse>> getProducts (
             @RequestParam LocalDate dateFrom,
             @RequestParam LocalDate dateTo,
             @RequestHeader("Content-Type") String Content-Type) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }

      // Override this method
      @RequestMapping(method=RequestMethod.GET, value="/{id}")
      default ResponseEntity<Mono<GetProductDetailResponse>> getProductById (
             @PathVariable Integer id) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }

      // Override this method
      @RequestMapping(method=RequestMethod.POST)
      default ResponseEntity<Mono<CreateProductResponse>> createProduct (
             @RequestBody DTORoot dTORoot,
             @RequestHeader("Content-Type") String Content-Type) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }


}

