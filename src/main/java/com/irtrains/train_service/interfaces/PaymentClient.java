package com.irtrains.train_service.interfaces;

import com.irtrains.train_service.DTO.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payment-service", url="http://localhost:8082")
public interface PaymentClient{

    @PostMapping("/create-transaction")
    ResponseEntity<?> createTransaction(@RequestBody PaymentDTO trReq);

}
