package com.irtrains.train_service.service;

import com.irtrains.train_service.model.train_coaches;
import com.irtrains.train_service.repository.TrainCoachRepository;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class TrainCoachService {

    private TrainCoachRepository trainCoachRepository;

    public TrainCoachService(TrainCoachRepository trainCoachRepository) {
        this.trainCoachRepository = trainCoachRepository;
    }

    @Transactional
    public train_coaches addTrainCoach(train_coaches trainCoach) {
        return trainCoachRepository.save(trainCoach);
    }

    public List<train_coaches> getTrainCoachesByTrainId(String trainId) {
        return trainCoachRepository.findByTrainId(trainId);
    }

    public List<train_coaches> getTrainCoachesbyTrainIdandCoachType(String trainId, String coachType) {
        List<train_coaches> coachList=trainCoachRepository.findByCoachTypeAndTrainId(coachType, trainId);
        return coachList==null? new ArrayList<>():coachList;
    }

//    public List<train_coaches> getAllTrainCoaches() {
//        return trainCoachRepository.findAll();
//    }

    public Optional<train_coaches> getcoachById(Integer coachId) {
        return trainCoachRepository.findBycoachId(coachId);
    }

    public List<train_coaches> getTrainCoachesByCoachType(String coachType) {
        return trainCoachRepository.findByCoachType(coachType);
    }

    public Optional<train_coaches> getTrainCoachByTrainIdAndCoachNumber(String trainId, Integer coachNumber) {
        return trainCoachRepository.findByTrainIdAndCoachNumber(trainId, coachNumber);
    }

    @Transactional
    public void deleteTrainCoachById(Integer coachId) {
        trainCoachRepository.deleteById(coachId);
    }

    @Transactional
    public train_coaches updateTrainCoach(train_coaches trainCoach) {
        train_coaches tempCoach=trainCoachRepository.findById(trainCoach.getCoachId())
                .orElseThrow(()-> new RuntimeException("Train Coach not Found"));

        if(trainCoach.getCoachType()!=null){
            tempCoach.setCoachType(trainCoach.getCoachType());
        }

        if(trainCoach.getTotalAvailableSeats()!=null){
            tempCoach.setTotalAvailableSeats(trainCoach.getTotalAvailableSeats());
        }

        if(trainCoach.getCoachNumber()!=null){
            tempCoach.setCoachNumber(trainCoach.getCoachNumber());
        }

        return trainCoachRepository.save(tempCoach);
    }



}
