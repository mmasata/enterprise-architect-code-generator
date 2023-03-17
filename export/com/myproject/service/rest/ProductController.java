package com.myproject.service.rest;

import org.springframework.web.bind.annotations.RestController;
import org.springframework.web.bind.annotations.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.myproject.service.model.CreateProductResponse;
import com.myproject.service.model.GetProductDetailResponse;
import com.myproject.service.model.GetProductResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/products")
public interface ProductController {

      // Override this method
      @RequestMapping(method=RequestMethod.GET)
      default ResponseEntity<Flux<GetProductResponse>> getProducts (
           @RequestParam void dateFrom,
           @RequestParam void dateTo,
           @RequestHeader("Content-Type") void Content-Type) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }

      // Override this method
      @RequestMapping(method=RequestMethod.GET, value="/{id}")
      default ResponseEntity<Mono<GetProductDetailResponse>> getProductById (
           @PathVariable void id) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }

      // Override this method
      @RequestMapping(method=RequestMethod.POST)
      default ResponseEntity<Mono<CreateProductResponse>> createProduct (
           @RequestBody DTORoot dTORoot,
           @RequestHeader("Content-Type") void Content-Type) {

          return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
      }


}

