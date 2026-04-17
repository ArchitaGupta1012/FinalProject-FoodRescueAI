package com.example.FinalProject.Repository;

import com.example.FinalProject.Model.Hotel;
import com.example.FinalProject.Model.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NGORepository extends JpaRepository<NGO,Long> {

        @Query("SELECT n FROM NGO n WHERE " +
                "LOWER(n.ngoName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(n.ngoCity) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(n.ngoState) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<NGO> searchNgo(@Param("keyword") String keyword);

    NGO findByNgoNameAndNgoPhone(String ngoName, String ngoPhone);
    NGO findByNgoName(String ngoName);
}
