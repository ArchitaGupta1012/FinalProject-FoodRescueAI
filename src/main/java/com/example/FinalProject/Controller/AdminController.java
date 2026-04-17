package com.example.FinalProject.Controller;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Model.Hotel;
import com.example.FinalProject.Model.NGO;
import com.example.FinalProject.Repository.FoodRepository;
import com.example.FinalProject.Repository.NGORepository;
import com.example.FinalProject.Repository.RegRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private RegRepository hotelRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private FoodRepository foodRepository;

    // 🔹 Show login page
    @GetMapping("/adminlogin")
    public String loginPage() {
        return "AdminLogin";
    }

    // 🔹 Handle login
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        if (username.equals("admin") && password.equals("admin123")) {
            session.setAttribute("admin", "true");
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid Admin Credentials");
            return "AdminLogin";
        }
    }
    // 🔹 Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        else{
        return "AdminDashboard";}
    }

    @GetMapping("/hoteldata")
    public String showhotel(Model model) {
        List<Hotel> hotels = hotelRepository.findAll();
        model.addAttribute("hotels", hotels);
        return "HotelData";
    }
    @GetMapping("/ngodata")
    public String showngo(Model model) {
        List<NGO> ngos = ngoRepository.findAll();
        model.addAttribute("ngos", ngos);
        return "NGOData";
    }
    @GetMapping("/foodlist")
    public String showfood(Model model) {
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("foods", foods);
        return "FoodListData";
    }
    @GetMapping("/delete-hotel/{id}")
    public String deleteHotel(@PathVariable Long id) {

        hotelRepository.deleteById(id);

        return "redirect:/admin/hoteldata";
    }
    @GetMapping("/viewfile/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try {
            Path path = Paths.get("uploads/").resolve(filename);
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // Detect file type
            String contentType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/delete-ngo/{id}")
    public String deleteNgo(@PathVariable Long id) {

        ngoRepository.deleteById(id);

        return "redirect:/admin/ngodata";
    }
    @GetMapping("/delete-food/{id}")
    public String deleteFood(@PathVariable Long id) {

        foodRepository.deleteById(id);

        return "redirect:/admin/foodlist";
    }
    @GetMapping("/search-ngo")
    @ResponseBody
    public List<NGO> searchNgo(@RequestParam("keyword") String keyword)
    {

        if (keyword == null || keyword.trim().isEmpty()) {
            return ngoRepository.findAll();
        }

        return ngoRepository.searchNgo(keyword);
    }
    @GetMapping("/search-hotel")
    @ResponseBody
    public List<Hotel> searchHotel(@RequestParam("keyword") String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return hotelRepository.findAll();
        }

        return hotelRepository.searchHotel(keyword);
    }
    @GetMapping("/search-food")
    @ResponseBody
    public List<Food> searchFood(@RequestParam("keyword") String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return foodRepository.findAll();
        }

        return foodRepository.searchFood(keyword);
    }

    // 🔹 Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/form/welcome";
    }

}

