//package org.example.prumpt_be.controller;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class PaymentResultController {
//
//    @GetMapping("/success")
//    public String success(
//            @RequestParam String orderId,
//            @RequestParam String paymentKey,
//            @RequestParam String amount) {
//        return "<h2>결제가 성공했습니다!</h2>"
//             + "<p>paymentKey: " + paymentKey + "</p>"
//             + "<p>orderId: " + orderId + "</p>"
//             + "<p>amount: " + amount + "</p>";
//    }
//
//    @GetMapping("/fail")
//    public String fail() {
//        return "결제 실패";
//    }
//}
