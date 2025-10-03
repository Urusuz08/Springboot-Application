package com.irtrains.train_service.service.booking;

import com.irtrains.train_service.model.booking.Booking;
import com.irtrains.train_service.repository.Booking.BookingRepository;
import com.irtrains.train_service.model.passenger.passenger;
import com.irtrains.train_service.service.passenger.PassengerService;
import com.irtrains.train_service.DTO.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final PassengerService passengerService;

    public BookingService(BookingRepository bookingRepository, PassengerService passengerService) {
        this.bookingRepository = bookingRepository;
        this.passengerService = passengerService;
    }

    @Transactional
    public Booking addBooking(BookingInfoDTO booking) {

        Booking newBooking = new Booking();
        newBooking.setUserId(booking.getUserId());
        newBooking.setTrainId(booking.getTrainId());
        newBooking.setSourceStationCode(booking.getSourceStationCode());
        newBooking.setDestinationStationCode(booking.getDestinationStationCode());
        newBooking.setTravelDate(booking.getJourneyDate());
        newBooking.setFare(booking.getTotalFare());
        newBooking.setContactNumber(booking.getContactNumber());
        newBooking.setEmail(booking.getEmail());
        newBooking.setCoachType(booking.getCoachType());
        newBooking.setStatus("BOOKED");


        List<Booking> existingBookings=bookingRepository.findAllByTrainIdAndJourneyDateAndCoachType(booking.getTrainId(),
                booking.getJourneyDate(),booking.getCoachType());


        Booking savedBooking = createBooking(newBooking);

        Long bookingId = savedBooking.getBookingId();
        List<PassengerDTO> passengers = booking.getPassengers();
        List<passenger> passengerList = new ArrayList<>();
        for(PassengerDTO passenger: passengers){
            passenger temp = new passenger();
            temp.setBookingId(bookingId);
            temp.setName(passenger.getName());
            temp.setAge(passenger.getAge());
            temp.setGender(passenger.getGender());
            passengerList.add(temp);
        }

//        PassengerService passengerService = new PassengerService();
        if(existingBookings==null) existingBookings=new ArrayList<>();

        passengerService.addPassengerM(passengerList, existingBookings,booking.getTrainId(),booking.getCoachType());
//fix the persist error in train_coach table, which JPA is unable to do it and because of it we are not able to create the entry in the train_coach table through the api.
        return savedBooking;
    }
    @Transactional
    public Booking createBooking(Booking booking) {
        // Generate PNR, set booking date, and status
        String timestampPart = DateTimeFormatter.ofPattern("yyMMddHHmmss").format(LocalDateTime.now());
        int randomPart = ThreadLocalRandom.current().nextInt(100, 1000); // 3-digit random number
        booking.setPnrNumber(timestampPart + randomPart);
        booking.setBookingDate(new java.util.Date());
        booking.setStatus("BOOKED");

        Booking savedBooking = bookingRepository.save(booking);

        return savedBooking;
    }

    public Booking getBookingByPnrNumber(String pnrNumber) {
        return bookingRepository.findByPnrNumber(pnrNumber)
                .orElseThrow(() -> new RuntimeException("Booking not found with PNR number: " + pnrNumber));
    }

    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public Booking updateBookingStatus(String pnrNumber, String newStatus) {
        Booking booking = getBookingByPnrNumber(pnrNumber);
        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void cancelBooking(String pnrNumber) {
        updateBookingStatus(pnrNumber, "CANCELLED");
    }
}
