package lesson10.hotelproject.controller;

import lesson10.hotelproject.entity.Hotel;
import lesson10.hotelproject.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    HotelRepository hotelRepository;
    @GetMapping("/allPage")
    public Page<Hotel> getHotelPage(@RequestParam int page){
        Pageable pageable = PageRequest.of(page,5);
        Page<Hotel> hotelPage = hotelRepository.findAll(pageable);
    return hotelPage;
    }
    @GetMapping("/list")
    public List<Hotel> hotelList(){
        List<Hotel> hotelList = hotelRepository.findAll();
        return hotelList;
    }
    @PostMapping("/add")
    public String addHotel(@RequestBody Hotel hotel){
        Hotel hotel1 = new Hotel();
        hotel1.setHotelName(hotel.getHotelName());
        hotelRepository.save(hotel1);
        return "Hotel added";
    }
    @DeleteMapping("/delete")
    public String deleteHotel(@PathVariable Integer id){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()){
            return "hotel id not founded";
        }
        Hotel hotel = optionalHotel.get();
        hotelRepository.delete(hotel);
        return "Hotel deleted";
    }
    @PostMapping("/edit")
    public String editHotel(@PathVariable Integer id,@RequestBody Hotel hotel){
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (!optionalHotel.isPresent()){
            return "hotel id not founded";
        }
        Hotel hotel1 = optionalHotel.get();
        hotel1.setHotelName(hotel.getHotelName());
        hotelRepository.save(hotel1);
        return "hotel edited";
    }



}
