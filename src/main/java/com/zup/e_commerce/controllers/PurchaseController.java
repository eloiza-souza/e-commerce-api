package com.zup.e_commerce.controllers;

import com.zup.e_commerce.dtos.PurchaseRequest;
import com.zup.e_commerce.services.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "Processar uma compra", description = "Recebe os dados de uma compra e realiza o processamento da transação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Os dados enviados na requisição são inválidos"),
            @ApiResponse(responseCode = "500", description = "Ocorreu um erro interno no servidor")
    })
    public ResponseEntity<String> processPurchase(@RequestBody PurchaseRequest request) {
        String response = purchaseService.processPurchase(request);
        return ResponseEntity.ok().body(response);
    }

}