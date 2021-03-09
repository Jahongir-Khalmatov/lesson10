package lesson10.hotelproject.controller;

import lesson10.hotelproject.entity.Hotel;
import lesson10.hotelproject.entity.Room;
import lesson10.hotelproject.playload.RoomDto;
import lesson10.hotelproject.repository.HotelRepository;
import lesson10.hotelproject.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    HotelRepository hotelRepository;

    @GetMapping("/allPage")
    public Page<Room> roomPage(@RequestParam int page){
        Pageable pageable = PageRequest.of(page,5);
        Page<Room> roomPage = roomRepository.findAll(pageable);
        return roomPage;
    }
    @GetMapping("/pageByHotelId/{hotelId}")
    public Page<Room> roomPageByHotelId(@PathVariable Integer hotelId,@RequestParam int page){
        Pageable pageable = PageRequest.of(page,5);
        Page<Room> roomPage = roomRepository.findAllByHotelId(hotelId, pageable);
        return roomPage;
    }
    @PostMapping("/add")
    public String addRoom(@RequestBody RoomDto roomDto){
        Room room = new Room();
        room.setNumber(roomDto.getNumber());
        room.setFloor(roomDto.getFloor());
        room.setSize(roomDto.getSize());
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if (!optionalHotel.isPresent()){
            return "hotel id not founded";
        }
        Hotel hotel = optionalHotel.get();
        room.setHotel(hotel);
        roomRepository.save(room);
        return "room added";
    }
    @PostMapping("/edit/{id}")
    public String editRoom(@PathVariable Integer id,@RequestBody RoomDto roomDto){
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (!optionalRoom.isPresent()){
            return "room id not founded";
        }
        Room room = optionalRoom.get();
        room.setNumber(roomDto.getNumber());
        room.setFloor(roomDto.getFloor());
        room.setSize(roomDto.getSize());
        Optional<Hotel> optionalHotel = hotelRepository.findById(roomDto.getHotelId());
        if (!optionalHotel.isPresent()){
            return "hotel id not founded";
        }
        Hotel hotel = optionalHotel.get();
        room.setHotel(hotel);
        roomRepository.save(room);
        return "room edited";
    }
    @DeleteMapping("/delete/{id}")
    public String  deleteRoom(@PathVariable Integer id){
        Optional<Room> optionalRoom = roomRepository.findById(id);
    if (!optionalRoom.isPresent()){
        return "room id not founded";
    }
        Room room = optionalRoom.get();
    roomRepository.delete(room);
    return "room deleted";
    }




}
