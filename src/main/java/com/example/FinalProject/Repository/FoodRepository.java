package com.example.FinalProject.Repository;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Model.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Long> {
    @Query("SELECT f FROM Food f WHERE " +
            "LOWER(f.food_description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.hotelName)LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.pickup_address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.status) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Food> searchFood(@Param("keyword") String keyword);
    List<Food> findByStatus(String status);
    List<Food> findByHotelNameAndRequestStatus(String hotelName, String requestStatus);}