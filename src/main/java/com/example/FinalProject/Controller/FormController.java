package com.example.FinalProject.Controller;

import com.example.FinalProject.Model.Food;
import com.example.FinalProject.Model.Hotel;
import com.example.FinalProject.Model.NGO;
import com.example.FinalProject.Repository.FoodRepository;
import com.example.FinalProject.Repository.NGORepository;
import com.example.FinalProject.Repository.RegRepository;
import com.example.FinalProject.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/form")
public class FormController {
    @Autowired
    private RegRepository hotelRepository;

    @Autowired
    private NGORepository ngoRepository;

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private EmailService emailService;


    private static String UPLOAD_DIR = "uploads/";

    //Call the interface class object
    @Autowired
    private RegRepository regRepository;
    @GetMapping("/choose-role")
    public String chooseRole() {
        return "ChooseRole";
    }
    @GetMapping("/home")
    public String home() {
        return "HomePage";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    //This URL is call to Welcome.html page
    @GetMapping("/welcome")
    public String showForm() {
        return "Welcome";
    }

    //This URL is call to RegForm.html page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "HotelReg";
    }

    //This URL is action of RegForm.html page
    @PostMapping("/submit")
    public String submitForm(@ModelAttribute Hotel hotel, @RequestParam("file") MultipartFile file, Model model) {
        try {
            if (!file.isEmpty()) {
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                hotel.setFilepath(path.toString());
                hotel.setFilename(file.getOriginalFilename());
            }
            regRepository.save(hotel);
            model.addAttribute("submitted", true);
            model.addAttribute("hotel", hotel);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "File upload failed !");
        }
        return "Submit";
    }
    @GetMapping("/login")
    public String showLogin(Model model) {
        return "HotelLogin";
    }

    //This URL is action of UserLogin.html page
    @PostMapping("/afterlogin")
    public String afterLogin(@RequestParam String hotelname, @RequestParam String mobileno, Model model, HttpSession session) {
        Hotel hotel = regRepository.findByHotelnameAndMobileno(hotelname, mobileno);
        if (hotel != null) {
            session.setAttribute("hotel", hotel);
            return "redirect:/form/show";
        } else {
            model.addAttribute("error", "Invalid Credentials");
            return "HotelLogin";
        }
    }
    @GetMapping("/show")
    public String welcome(Model model, HttpSession session) {
        Hotel hotel = (Hotel) session.getAttribute("hotel");
        if (hotel == null) {
            return "redirect:/form/login";
        } else {
            model.addAttribute("hotel", hotel);
            return "Submit";
        }}

        @GetMapping("/edit")
        public String editHotel(HttpSession session, Model model) {

            Hotel hotel = (Hotel) session.getAttribute("hotel");

            model.addAttribute("hotel", hotel);

            return "edit";
        }
    @PostMapping("/update")
    public String update(@ModelAttribute Hotel hotel, @RequestParam("file") MultipartFile file,
                         HttpSession session) {
        try {

            //get old data form the session and update it with new data
            Hotel oldHotel = (Hotel) session.getAttribute("hotel");

            if (!file.isEmpty()) {
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                hotel.setFilepath(path.toString());
                hotel.setFilename(file.getOriginalFilename());
            }
            else {
                hotel.setFilepath(oldHotel.getFilepath());
                hotel.setFilename(oldHotel.getFilename());
            }
            Hotel saved = regRepository.save(hotel);
            session.setAttribute("hotel", saved);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Submit";
    }
    @GetMapping("/hotel-requests")
    public String showRequests(HttpSession session, Model model) {

        Hotel hotel = (Hotel) session.getAttribute("hotel");

        List<Food> requests = foodRepository
                .findByHotelNameAndRequestStatus(hotel.getHotelname(), "PENDING");

        model.addAttribute("requests", requests);

        return "HotelRequests";
    }
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {

        Food food = foodRepository.findById(id).orElseThrow();

        food.setRequestStatus("APPROVED");
        food.setStatus("CLAIMED");

        foodRepository.save(food);

        // ✅ FETCH NGO EMAIL
        NGO ngo = ngoRepository.findByNgoName(food.getRequestedByNgoName());

        if (ngo != null) {
            String ngoEmail = ngo.getNgoEmail();
            String body = "Hello,\n\n"
                    + "Good news! Your food request has been APPROVED.\n\n"

                    + "Food Details:\n"
                    + "Food Type: " + food.getFood_type() + "\n"
                    + "Quantity: " + food.getQuantity() + "\n\n"

                    + "Hotel Details:\n"
                    + "Hotel Name: " + food.getHotelName() + "\n"
                    + "Pickup Address: " + food.getPickup_address() + "\n"
                    + "Contact Person: " + food.getContact_person() + "\n"
                    + "Phone: " + food.getPh_no() + "\n\n"

                    + "Please collect the food as soon as possible.\n\n"

                    + "Food Rescue AI 🌱";

            emailService.sendEmail(
                    ngoEmail,
                    "Food Request Approved ✅",
                    body
            );
        }

        return "redirect:/form/hotel-requests";
    }
    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {

        Food food = foodRepository.findById(id).orElseThrow();

        food.setRequestStatus("REJECTED");

        foodRepository.save(food);

        NGO ngo = ngoRepository.findByNgoName(food.getRequestedByNgoName());

        if (ngo != null) {
            String ngoEmail = ngo.getNgoEmail();

            String body = "Hello,\n\n"
                    + "We regret to inform you that your food request has been REJECTED.\n\n"

                    + "Food Details:\n"
                    + "Food Type: " + food.getFood_type() + "\n"
                    + "Quantity: " + food.getQuantity() + "\n\n"

                    + "Hotel: " + food.getHotelName() + "\n\n"

                    + "You can explore other available food options on the platform.\n\n"
                    + "Keep making a difference ❤️\n\n"

                    + "Food Rescue AI 🌱";

            // ✅ SEND EMAIL
            emailService.sendEmail(
                    ngoEmail,
                    "Food Request Rejected ❌",
                    body
            );
        }

        return "redirect:/form/hotel-requests";
    }
}

