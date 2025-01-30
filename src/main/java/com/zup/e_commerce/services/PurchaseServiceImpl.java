package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.PurchaseRequest;
import com.zup.e_commerce.exceptions.CustomerNotFoundException;
import com.zup.e_commerce.exceptions.ProductNotFoundException;
import com.zup.e_commerce.exceptions.ProductUnavailableException;
import com.zup.e_commerce.models.Customer;
import com.zup.e_commerce.models.Product;
import com.zup.e_commerce.repositories.CustomerRepository;
import com.zup.e_commerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public String processPurchase(PurchaseRequest request) {
        String cpf = request.cpf();
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com CPF: " + cpf + " não encontrado."));

        List<String> unavailableProducts = new ArrayList<>();
        List<Product> productsToPurchase = new ArrayList<>();

        for (String productName : request.productNames()) {
            Product product = productRepository.findByName(productName).
                    orElseThrow(() -> new ProductNotFoundException("Produto \"" + productName + "\" não cadastrado."));

            if (product.getQuantity() <= 0) {
                unavailableProducts.add(productName);
            } else {
                productsToPurchase.add(product);
            }
        }
        if (!unavailableProducts.isEmpty()) {
            throw new ProductUnavailableException("Produtos em falta: " + String.join(", ", unavailableProducts));
        }

        productsToPurchase.forEach(product -> {
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
        });

        return "Compra realizada com sucesso para o cliente " + customer.getName();
    }

}


