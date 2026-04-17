package com.example.FinalProject.Controller;

import com.example.FinalProject.Model.Hotel;
import com.example.FinalProject.Repository.RegRepository;
import com.example.FinalProject.service.AIService;
import com.example.FinalProject.service.EmailService;
import com.example.FinalProject.service.FoodService;
import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Model.NGO;
import com.example.FinalProject.Repository.FoodRepository;
import com.example.FinalProject.Repository.NGORepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/form")
public class NGOController {

    @Autowired
    private NGORepository ngoRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodService foodService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RegRepository regRepository;



    @GetMapping("/ngoreg")
    public String showRegistrationForm(Model model) {
        model.addAttribute("ngo", new NGO());
        return "NGOReg";
    }
    @PostMapping("/ngoreg")
    public String registerNgo(@ModelAttribute NGO ngo, Model model) {
        ngo.setLatitude(20.9374);   // Same city
        ngo.setLongitude(77.7796);


        ngoRepository.save(ngo);

        model.addAttribute("ngo", ngo);   // ✅ IMPORTANT LINE
        model.addAttribute("success", "Registration Successful!");

        return "NgoInfo";
    }
    @GetMapping("/ngoInfo")
    public String welcome(Model model, HttpSession session) {
        NGO ngo = (NGO) session.getAttribute("ngo");
        if (ngo == null) {
            return "redirect:/form/ngologin";
        } else {
            model.addAttribute("ngo", ngo);
            return "NgoInfo";
        }}
    @GetMapping("/ngologin")
    public String showLogin(Model model) {
        return "NgoLogin";
    }

    //This URL is action of UserLogin.html page
    @PostMapping("/ngologin")
    public String afterLogin(@RequestParam String ngoName,
                             @RequestParam String ngoPhone,
                             Model model,
                             HttpSession session) {

        NGO ngo = ngoRepository.findByNgoNameAndNgoPhone(ngoName, ngoPhone);

        if (ngo != null) {

            // ✅ SET LOCATION BASED ON CITY
            if (ngo.getNgoCity().equalsIgnoreCase("Amravati")) {
                ngo.setLatitude(20.9374);
                ngo.setLongitude(77.7796);
            }
            else if (ngo.getNgoCity().equalsIgnoreCase("Nagpur")) {
                ngo.setLatitude(21.1458);
                ngo.setLongitude(79.0882);
            }

            // ✅ STORE UPDATED NGO
            session.setAttribute("ngo", ngo);

            return "redirect:/form/ngoInfo";
        }
        else {
            model.addAttribute("error", "Invalid Credentials");
            return "NgoLogin";
        }
    }
    @GetMapping("/editngo")
    public String editNgo(HttpSession session, Model model) {

        NGO ngo = (NGO) session.getAttribute("ngo");

        // 🔥 ADD THIS BLOCK HERE
        if (ngo == null) {
            return "redirect:/form/ngologin";
        }

        model.addAttribute("ngo", ngo);

        return "edit_NGO";
    }
    @PostMapping("/update-ngo")
    public String updateNgo(@ModelAttribute NGO ngo, HttpSession session) {

        NGO oldNgo = (NGO) session.getAttribute("ngo");

        // 🔥 IMPORTANT
        ngo.setId(oldNgo.getId());

        ngoRepository.save(ngo);

        session.setAttribute("ngo", ngo);

        return "redirect:/form/ngoInfo";    }

    @GetMapping("/available-food")
    public String showAvailableFood(Model model, HttpSession session) {

        NGO ngo = (NGO) session.getAttribute("ngo");

        List<Food> foods = foodService.getAvailableFood();

        List<Food> suggestedFoods = new java.util.ArrayList<>();
        List<Food> otherFoods = new java.util.ArrayList<>();

        for (Food f : foods) {

            double distance = calculateDistance(
                    ngo.getLatitude(),
                    ngo.getLongitude(),
                    f.getLatitude(),
                    f.getLongitude()
            );

            if (distance <= ngo.getPreferredDistance()) {
                f.setDistanceStatus("Within Range ✅");
                suggestedFoods.add(f);
            } else {
                f.setDistanceStatus("Out of Range ❌ (" + Math.round(distance) + " KM)");
                otherFoods.add(f);
            }
        }

        model.addAttribute("suggestedFoods", suggestedFoods);
        model.addAttribute("otherFoods", otherFoods);

        return "NgoDashBoard";
    }

    @PostMapping("/claim/{id}")
    public String claimFood(@PathVariable Long id, HttpSession session) {

        Food food = foodRepository.findById(id).orElseThrow();
        NGO ngo = (NGO) session.getAttribute("ngo");

        if (ngo == null) {
            return "redirect:/form/ngologin";
        }

        food.setRequestStatus("PENDING");
        food.setStatus("REQUESTED");   // ✅ IMPORTANT (UI me show hone ke liye)

        food.setRequestedByNgoName(ngo.getNgoName());
        food.setRequestedByNgoPhone(ngo.getNgoPhone());

        foodRepository.save(food);

        // ✅ FETCH HOTEL EMAIL FROM DB
        Hotel hotel = regRepository.findByHotelname(food.getHotelName());

        if (hotel != null) {
            String hotelEmail = hotel.getEmail();

            String body = "Hello,\n\n"
                    + "You have received a new food request on Food Rescue AI.\n\n"

                    + "NGO Details:\n"
                    + "Name: " + ngo.getNgoName() + "\n"
                    + "Phone: " + ngo.getNgoPhone() + "\n\n"

                    + "Food Details:\n"
                    + "Food Type: " + food.getFood_type() + "\n"
                    + "Quantity: " + food.getQuantity() + "\n"
                    + "Pickup Address: " + food.getPickup_address() + "\n\n"

                    + "Action Required:\n"
                    + "Please login to your dashboard to approve or reject this request.\n\n"

                    + "Food Rescue AI 🌱";

            emailService.sendEmail(
                    hotelEmail,
                    "New Food Request - Food Rescue AI",

                    body
            );
        }

        return "redirect:/form/available-food";
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
    }
