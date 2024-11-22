//package org.example.ilib.Controller;
//
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.springframework.web.bind.annotation.*;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import java.util.Arrays;  // Import thêm để sử dụng Arrays.asList
//
//@RestController
//@RequestMapping("/payment")
//public class PaymentController {
//
//    // Cấu hình API key của Stripe (Sử dụng Secret Key của Stripe)
//    static {
//        Stripe.apiKey = "sk_test_51QNBdZHX1v2ZIhpexSj9WjbzV31q9NaZHxsBrzAha4pPepDuxEQK7gnqVsY4DsWB89nsghxmPlfIHK3CiWNMZhfN00YqJtKGhH"; // Thay thế bằng API key thực tế
//    }
//
//    // Phương thức tạo Stripe Checkout Session
//    @PostMapping("/create-checkout-session")
//    public Session createCheckoutSession(@RequestParam double amount) {
//        // Tạo một session thanh toán từ Stripe
//        SessionCreateParams params = SessionCreateParams.builder()
//                .setPaymentMethodTypes(Arrays.asList("card"))  // Sử dụng Arrays.asList thay vì List.of()
//                .setLineItems(Arrays.asList(
//                        SessionCreateParams.LineItem.builder()
//                                .setPriceData(
//                                        SessionCreateParams.LineItem.PriceData.builder()
//                                                .setCurrency("usd") // Hoặc bạn có thể thay đổi sang VND nếu cần
//                                                .setUnitAmount((long)(amount * 100)) // Amount in cents (chuyển đổi sang đơn vị cents)
//                                                .build())
//                                .setQuantity(1L)
//                                .build()))
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl("https://your-site.com/success?session_id={CHECKOUT_SESSION_ID}") // URL khi thanh toán thành công
//                .setCancelUrl("https://your-site.com/cancel") // URL khi thanh toán bị hủy
//                .build();
//
//        try {
//            // Tạo Checkout session
//            Session session = Session.create(params);
//            return session;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null; // Trả về null nếu có lỗi xảy ra
//        }
//    }
//}
