package bakehouse.controller;

import bakehouse.model.Payment;

public class PaymentController {
    public Payment processPayment(double amount, String paymentMethod) {
        return new Payment(amount, paymentMethod);
    }
}
