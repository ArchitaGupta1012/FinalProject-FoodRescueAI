package com.example.FinalProject.service;


import org.springframework.stereotype.Service;

    @Service
    public class AIService {

        public String getDistanceSuggestion(String ngoCity, String foodLocation) {

            if (ngoCity == null || foodLocation == null) {
                return "No Data";
            }

            // SAME LOGIC STYLE as your AI project (condition-based decision)
            if (foodLocation.toLowerCase().contains(ngoCity.toLowerCase())) {
                return "Nearby (within 5 km)";
            } else {
                return "Far Location";
            }
        }

}
