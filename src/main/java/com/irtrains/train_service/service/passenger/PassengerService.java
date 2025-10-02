package com.irtrains.train_service.service.passenger;

import com.irtrains.train_service.model.booking.Booking;
import com.irtrains.train_service.model.passenger.passenger;
import com.irtrains.train_service.repository.passenger.PassengerRepository;
import com.irtrains.train_service.model.train_coaches.train_coaches;
import com.irtrains.train_service.service.train_coach.TrainCoachService;

//You have to settle the trainCoachRepository part. Tmrw
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PassengerService {
    private final PassengerRepository passengerRepository;

    private final TrainCoachService trainCoachService;

    public PassengerService(PassengerRepository passengerRepository, TrainCoachService trainCoachService) {
        this.passengerRepository = passengerRepository;
        this.trainCoachService = trainCoachService;
    }



    @Transactional
    public List<passenger> addPassenger(List<passenger> passenger, List<Booking> existingBookings) {

        List<int[]> AllocatedSeats = new ArrayList<>();
        List<train_coaches> coaches = trainCoachService.getTrainCoachesbyTrainIdandCocahType(existingBookings.get(0).getCoachType(), existingBookings.get(0).getTrainId());

        for(train_coaches coach:coaches){
            int seats=coach.getTotalAvailableSeats();
            int[] seatArray=new int[seats];
            Arrays.fill(seatArray,0);
            AllocatedSeats.add(seatArray);
        }

        if(existingBookings.size()!=0){
//            Set<Integer> store=new HashSet<>();
            for(Booking book:existingBookings){
                if(!book.getStatus().equals("BOOKED")) continue;

                for(passenger a: getPassengersByBookingId(book.getBookingId())){
                    if(a.getSeatStatus().equals("CNF")){
//                        store.add(a.getCoachNumber()-1);
                        int seatNum=Integer.parseInt(a.getSeatNumber());
                        AllocatedSeats.get(a.getCoachNumber()-1)[seatNum-1]=1;
                    }
                }
            }
        }



        int checker=0;
        if(AllocatedSeats.size()!=0){
            for(int i=0;i<AllocatedSeats.size();i++){
                int cnt=0;
                for(int j=0;j<AllocatedSeats.get(i).length;j++){
                    if(AllocatedSeats.get(i)[j]==0) cnt++;
                    else {
                        cnt=0;
                    }
                    if(cnt==passenger.size()) {
                        checker = 1;
                        for (int k = 0; k < passenger.size(); k++) {
                            int coachNumber = i + 1;
                            int seatNumber = j - cnt + k + 1;
                            AllocatedSeats.get(i)[seatNumber - 1] = 1; // Mark seat as booked
                            passenger.get(k).setCoachNumber(coachNumber);
                            passenger.get(k).setSeatNumber(seatNumber + "");
                            passenger.get(k).setSeatStatus("BOOKED");
                        }
                        break;
                    }
                }
                if(checker==1) break;
            }
        }
        int cnt=0;
        if(checker==0){
            for(int i=0;i<AllocatedSeats.size();i++){

                for(int j=0;j<AllocatedSeats.get(i).length;j++){
                    if(AllocatedSeats.get(i)[j]==0) {
                        int coachNumber = i + 1;
                        int seatNumber = j + 1;
                        AllocatedSeats.get(i)[j] = 1; // Mark seat as booked
                        passenger.get(cnt).setCoachNumber(coachNumber);
                        passenger.get(cnt).setSeatNumber(seatNumber + "");
                        passenger.get(cnt).setSeatStatus("BOOKED");
                        cnt++;
                    }
                    if(cnt==passenger.size()) {
                        checker = 1;
                        break;
                    }
                }
                if(checker==1) break;
            }
        }
        for(int i=cnt;i<passenger.size();i++){
            passenger.get(i).setCoachNumber(0);
            passenger.get(i).setSeatNumber("0");
            passenger.get(i).setSeatStatus("WAITING");
        }

        return passengerRepository.saveAll(passenger);
    }

    public List<passenger> getPassengersByBookingId(Long bookingId) {
        return passengerRepository.findByBookingId(bookingId);
    }

//    public List<passenger> getPassengersByTrainIdAndJourneyDate(String trainId, Date journeyDate) {
//        return passengerRepository.findBytrainIdandJourneyDate(trainId, journeyDate);
//    }

    @Transactional
    public void cancelPassengersByBookingId(Long bookingId) {
        List<passenger> passengers = passengerRepository.findByBookingId(bookingId);
        for (passenger passenger : passengers) {
            passenger.setSeatStatus("CANCELLED");
        }
        passengerRepository.saveAll(passengers);
    }

    /**
     * Updates the status of a specific passenger.
     * @param passengerId The ID of the passenger to update.
     * @param newStatus The new status (e.g., "CANCELLED").
     * @return The updated passenger.
     */
    @Transactional
    public passenger updatePassengerStatus(Long passengerId, String newStatus) {
        passenger passengerToUpdate = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found with ID: " + passengerId));

        passengerToUpdate.setSeatStatus(newStatus);
        return passengerRepository.save(passengerToUpdate);
    }
}
