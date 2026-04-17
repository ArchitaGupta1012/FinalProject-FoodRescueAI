package com.example.FinalProject.Repository;

import com.example.FinalProject.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RegRepository extends JpaRepository<Hotel,Long> {
    @Query("SELECT h FROM Hotel h WHERE " +
            "LOWER(h.hotelname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(h.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Hotel> searchHotel(@Param("keyword") String keyword);
    Hotel findByHotelnameAndMobileno(String hotelname, String mobileno);
    Hotel findByHotelname(String hotelname);
}

