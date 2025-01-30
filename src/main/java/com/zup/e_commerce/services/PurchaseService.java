package com.zup.e_commerce.services;

import com.zup.e_commerce.dtos.PurchaseRequest;

public interface PurchaseService {
    String processPurchase(PurchaseRequest request);
}
