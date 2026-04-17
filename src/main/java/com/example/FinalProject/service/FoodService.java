package com.example.FinalProject.service;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    // Get only available food
    public List<Food> getAvailableFood() {
        return foodRepository.findByStatus("AVAILABLE");
    }
}
