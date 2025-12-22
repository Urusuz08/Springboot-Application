package com.irtrains.train_service.service;

import com.irtrains.train_service.model.*;
import com.irtrains.user_service.model.*;
import com.irtrains.user_service.repository.UserRepository;
import com.irtrains.train_service.repository.*;
import com.irtrains.train_service.DTO.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.irtrains.train_service.interfaces.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.*;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TrainRouteRepository trainRouteRepository;
    private final PassengerService passengerService;
    private final SeatAvailabilityRepository seatAvailabilityRepository;
    private final FareRuleSetRepo fareRuleSetRepo;
    private final FareClassRateRepo fareClassRateRepo;
    private final UserRepository userRepository;
    private final PaymentClient paymentClient;

    public BookingService(BookingRepository bookingRepository, PassengerService passengerService, SeatAvailabilityRepository seatAvailabilityRepository,
                          FareRuleSetRepo fareRuleSetRepo, FareClassRateRepo fareClassRateRepo, UserRepository userRepository,
                          TrainRouteRepository trainRouteRepository, PaymentClient paymentClient) {
        this.trainRouteRepository = trainRouteRepository;
        this.userRepository = userRepository;
        this.fareRuleSetRepo = fareRuleSetRepo;
        this.fareClassRateRepo = fareClassRateRepo;
        this.bookingRepository = bookingRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
        this.passengerService = passengerService;
        this.paymentClient = paymentClient;
    }

    @Transactional
    public Map<String,Object> addBooking(BookingInfoDTO booking) {

        Booking newBooking = new Booking();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = (authentication != null) ? authentication.getName() : "system";

        Optional<User> userOpt = userRepository.findByUsername(currentPrincipalName);

        if(userOpt.isEmpty()){
            throw new RuntimeException("User not found: " + currentPrincipalName);
        }

        newBooking.setUserId(userOpt.get().getId()+"");
        newBooking.setTrainId(booking.getTrainId());
        newBooking.setSourceStationCode(booking.getSourceStationCode());
        newBooking.setDestinationStationCode(booking.getDestinationStationCode());
        newBooking.setJourneyDate(booking.getJourneyDate());

        int distance = trainRouteRepository.distance(booking.getTrainId(), booking.getSourceStationCode(), booking.getDestinationStationCode());
        Map<String, Object> fareDetails = calculateFare(booking.getCoachType(), booking.getPassengers().size(),
                distance);

        newBooking.setFare((Double) fareDetails.get("Total Fare"));

        newBooking.setFareBreakup(fareDetails);
        newBooking.setContactNumber(booking.getContactNumber());
        newBooking.setEmail(booking.getEmail());
        newBooking.setCoachType(booking.getCoachType());
        newBooking.setStatus("BOOKED");


        List<Booking> existingBookings=bookingRepository.findAllByTrainIdAndJourneyDateAndCoachType(booking.getTrainId(),
                booking.getJourneyDate(),booking.getCoachType());

        trainSeatAvailability tempList = seatAvailabilityRepository.findByTrainIdAndCoachIdAndDateOfJourney(booking.getTrainId(), booking.getCoachType(), booking.getJourneyDate());


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

        Map<String,Object> response=new HashMap<>();
        response.put("booking", savedBooking);

        passengerService.addPassengerM(passengerList,tempList,response);
//fix the persist error in train_coach table, which JPA is unable to do it and because of it we are not able to create the entry in the train_coach table through the api.
        return response;
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
    public Booking cancelBooking(String pnrNumber) {
        Booking booking = getBookingByPnrNumber(pnrNumber);
        booking.setStatus("CANCELLED");
        return bookingRepository.save(booking);

        // Notify payment service for refund

    }

    //helper function to calculate fare
    public Map<String,Object> calculateFare(String classType, int numOfPassengers, int distance){
            fareRuleSet fr= fareRuleSetRepo.findByEffectiveDate(LocalDate.now());
            if(fr==null){
                throw new RuntimeException("Fare Rule Set not found for the current date");
            }

            Optional<fareClassRate> fcr= fareClassRateRepo.findByClassTypeAndFareRuleSetId(classType, fr.getRuleSetId());

            if(fcr.isEmpty()){
                throw new RuntimeException("Fare Class Rate not found for the given class type");
            }

            double farePerKm=fcr.get().getRatePerKm();
            double baseFare= fcr.get().getBaseFare();

            double totalFare= (farePerKm * distance + baseFare) * numOfPassengers;

            totalFare=Math.max(totalFare, fcr.get().getMinFare() * numOfPassengers);

            Map<String,Object> fareBreakup=new HashMap<>();
            fareBreakup.put("Base Fare", baseFare);
            fareBreakup.put("totalDistnaceFare", farePerKm*distance);
            fareBreakup.put("Number of Passengers", numOfPassengers);
            fareBreakup.put("Total Fare", totalFare);
            return fareBreakup;

    }


}
