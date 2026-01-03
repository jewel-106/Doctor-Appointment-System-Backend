package com.appointment.demo.Service;
import org.springframework.stereotype.Service;
@Service
public class SmsService {
    public void sendOtp(String phone, String otp) {
        System.out.println("SMS sent to " + phone + ": Your OTP is " + otp);
    }
}