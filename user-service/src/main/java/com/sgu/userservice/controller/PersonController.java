package com.sgu.userservice.controller;

import com.sgu.userservice.dto.request.PersonRequest;
import com.sgu.userservice.dto.response.HttpResponseEntity;
import com.sgu.userservice.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/get-all-person")
    public ResponseEntity<HttpResponseEntity> getAllAccount(){
        HttpResponseEntity httpResponseEntity = personService.getAllPerson();

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-all-person-with-pagination")
    public ResponseEntity<HttpResponseEntity> getAllAccountWithPagination(
            @RequestParam(name = "page", required = true) int page,
            @RequestParam(name = "size", required = true) int size
    ){
        HttpResponseEntity httpResponseEntity = personService.getAllPersonWithPagination(page,size);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @GetMapping("/get-person-by-id/{id}")
    public ResponseEntity<HttpResponseEntity> getAccountByPersonId(
            @PathVariable(name = "id") Long personId
    ){

        HttpResponseEntity httpResponseEntity = personService.getById(personId);


        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<HttpResponseEntity> updatePerson(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid PersonRequest personRequest
    ){
        HttpResponseEntity httpResponseEntity = personService.updatePerson(id,personRequest);

        return ResponseEntity.status(HttpStatus.OK).body(httpResponseEntity);
    }
}
