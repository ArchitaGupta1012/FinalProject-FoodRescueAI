package com.example.FinalProject.service;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FoodExpiryService {

    @Autowired
    private FoodRepository foodRepository;

    // ⏰ runs every 1 minute
    @Scheduled(fixedRate = 60000)
    public void checkExpiry() {

        List<Food> foods = foodRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        for (Food food : foods) {
            try {
                LocalDateTime expiry = LocalDateTime.parse(food.getExpiry_time(), formatter);
                // 👉 if expired
                if (expiry.isBefore(LocalDateTime.now())) {
                    food.setStatus("EXPIRED");   // ✅ SET STATUS
                    foodRepository.save(food);
                }

            } catch (Exception e) {
                System.out.println("Wrong date format: " + food.getId());
            }
        }
    }

}
