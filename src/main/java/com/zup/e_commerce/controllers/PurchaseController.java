package com.zup.e_commerce.controllers;

import com.zup.e_commerce.dtos.PurchaseRequest;
import com.zup.e_commerce.services.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compras")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<String> processPurchase(@RequestBody PurchaseRequest request) {
        String response = purchaseService.processPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
