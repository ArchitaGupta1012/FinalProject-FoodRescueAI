package com.example.FinalProject.Controller;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Model.Hotel;
import com.example.FinalProject.Repository.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/form")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/foodreg")
    public String food_list(Model model) {
        model.addAttribute("food", new Food());
        return "FoodReg";
    }
    @PostMapping("/foodreg")
    public String submit(@ModelAttribute Food food, Model model, HttpSession session) {

        food.setStatus("AVAILABLE");
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        food.setHotelName(hotel.getHotelname());
        food.setContact_person(hotel.getContactperson());

        // ✅ SET LOCATION BASED ON CITY
        if (food.getCity().equalsIgnoreCase("Amravati")) {
            food.setLatitude(20.9374);
            food.setLongitude(77.7796);
        }
        else if (food.getCity().equalsIgnoreCase("Nagpur")) {
            food.setLatitude(21.1458);
            food.setLongitude(79.0882);
        }

        // ✅ SAVE
        foodRepository.save(food);

        if (food != null) {
            session.setAttribute("food", food);
            return "redirect:/form/reg";
        } else {
            return "FoodReg";
        }
    }

    @GetMapping("/reg")
public String done(Model model, HttpSession session) {
    Food food = (Food) session.getAttribute("food");
    if (food == null) {
        return "redirect:/form/foodreg";
    } else {
        model.addAttribute("food", food);
        return "SubmitFoodList";
    }}

}



