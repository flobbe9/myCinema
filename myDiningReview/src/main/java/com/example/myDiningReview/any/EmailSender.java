package com.example.myDiningReview.any;

import org.springframework.stereotype.Service;

@Service
public interface EmailSender {
    public void send(String to, String email);
}
