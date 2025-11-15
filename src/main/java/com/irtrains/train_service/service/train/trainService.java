package com.irtrains.train_service.service.train;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.model.enums.Type;
import com.irtrains.train_service.model.train.train;
import com.irtrains.train_service.model.trainSeatAvailability.trainSeatAvailability;
import com.irtrains.train_service.model.train_route.trainRoute;
import com.irtrains.train_service.repository.seatAvailabilityRepository.SeatAvailabilityRepository;
import com.irtrains.train_service.repository.train_coach.TrainCoachRepository;
import com.irtrains.train_service.repository.station.StationRepository;
import com.irtrains.train_service.model.train_coaches.train_coaches;
import com.irtrains.train_service.model.station.Station;

import com.irtrains.train_service.repository.train.TrainRepository;
import com.irtrains.train_service.repository.trainRoute.TrainRouteRepository;
import com.irtrains.train_service.DTO.*;
import com.irtrains.train_service.model.train_route.trainRoute;

import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.time.*;

/**
 * Service layer for Train aggregate. Provides validation, transactional boundaries and
 * a stable API surface decoupled from controller & persistence details.
 */
@Service
@Transactional(readOnly = true)
public class trainService {

    private final TrainRepository trainRepository;
    private final TrainRouteRepository trainRouteRepository;
    private final SeatAvailabilityRepository seatAvailabilityRepository;
    private final TrainCoachRepository trainCoachRepository;
    private final StationRepository stationRepository;

    public trainService(TrainRepository trainRepository,TrainCoachRepository trainCoachRepository,
                        TrainRouteRepository trainRouteRepository, SeatAvailabilityRepository seatAvailabilityRepository,
                        StationRepository stationRepository) {
        this.trainRepository = trainRepository;
        this.trainRouteRepository = trainRouteRepository;
        this.seatAvailabilityRepository = seatAvailabilityRepository;
        this.trainCoachRepository = trainCoachRepository;
        this.stationRepository = stationRepository;
    }


    @Transactional
    public List<trainSeatAvailability> addSeatAvailability() {

        List<train> trains=trainRepository.findAll();
        List<trainSeatAvailability> sA=new ArrayList<>();
        for(int i=0;i<trains.size();i++){
            String trainId=trains.get(i).getTrainId();
            List<train_coaches> coaches=trainCoachRepository.findByTrainId(trainId);
            HashMap<String,Integer> helper=new HashMap<>();

            for(int j=0;j<coaches.size();j++){
                train_coaches coach=coaches.get(j);

                helper.put(coach.getCoachType(),helper.getOrDefault(coach.getCoachType(),0)+coach.getTotalAvailableSeats());

            }
            for(String a: helper.keySet()){
                trainSeatAvailability seatAvailability=new trainSeatAvailability();
                seatAvailability.setTrainId(trainId);
                seatAvailability.setCoachId(a);
                seatAvailability.setDateOfJourney(LocalDate.now());
                seatAvailability.setAvailableSeats(helper.get(a));
                seatAvailability.setTotalSeats(helper.get(a));
                seatAvailability.setLastUpdated(System.currentTimeMillis());
                sA.add(seatAvailability);
            }
        }
        return seatAvailabilityRepository.saveAll(sA);
    }

    public trainSeatAvailability getSeatAvailabilityByTrainIdAndCoachIdAndDateOfJourney(String trainId, String coachId, LocalDate dateOfJourney) {
        return seatAvailabilityRepository.findByTrainIdAndCoachIdAndDateOfJourney(trainId, coachId, dateOfJourney);
    }

    public trainSeatAvailability getSeatAvailabilityByTrainId(String trainId) {
        return seatAvailabilityRepository.findByTrainId(trainId);
    }

    @Transactional
    public trainSeatAvailability updateSeatAvailability(Integer seatAvailabilityId, trainSeatAvailability seatAvailabilityDetails) {
        trainSeatAvailability existingSeatAvailability = seatAvailabilityRepository.findById(seatAvailabilityId)
                .orElseThrow(() -> new RuntimeException("SeatAvailability not found with id: " + seatAvailabilityId));

        if (seatAvailabilityDetails.getTrainId() != null) {
            existingSeatAvailability.setTrainId(seatAvailabilityDetails.getTrainId());
        }
        if (seatAvailabilityDetails.getCoachId() != null) {
            existingSeatAvailability.setCoachId(seatAvailabilityDetails.getCoachId());
        }
        if (seatAvailabilityDetails.getDateOfJourney() != null) {
            existingSeatAvailability.setDateOfJourney(seatAvailabilityDetails.getDateOfJourney());
        }
        if (seatAvailabilityDetails.getAvailableSeats() != null) {
            existingSeatAvailability.setAvailableSeats(seatAvailabilityDetails.getAvailableSeats());
        }
        if (seatAvailabilityDetails.getTotalSeats() != null) {
            existingSeatAvailability.setTotalSeats(seatAvailabilityDetails.getTotalSeats());
        }
        existingSeatAvailability.setLastUpdated(System.currentTimeMillis());

        return seatAvailabilityRepository.save(existingSeatAvailability);
    }

    @Transactional
    public void deleteSeatAvailability(Integer seatAvailabilityId) {
        seatAvailabilityRepository.deleteById(seatAvailabilityId);
    }

    /* ===================== Creation ===================== */
    @Transactional
    public train createTrain(train t) {
        validateNewTrain(t);
        try {
            return trainRepository.save(t);
        } catch (DataIntegrityViolationException ex) {
            // Re-throw with clearer message while preserving root cause
            throw new IllegalArgumentException("Train constraints violated (duplicate id or name)", ex);
        }
    }

    @Transactional
    public CoachDTO createTrainCoach(CoachDTO tr) {

//        train_coaches temp=new train_coaches();
//        temp.setTrainId(tr.getTrainId());
        List<train_coaches> coaches=new ArrayList<>();
        List<CoachD> details=tr.getDetails();
        for(int i=0;i<details.size();i++) {
            CoachD dto = details.get(i);
            for (int j = 0; j < dto.getNoOfCoaches(); j++) {
                train_coaches coach = new train_coaches();
                coach.setTrainId(tr.getTrainId());
                coach.setCoachType(dto.getCoachType());
                coach.setCoachNumber(j + 1);
                coach.setTotalAvailableSeats(dto.getAvailableSeatsperCoach());
                coaches.add(coach);
            }

        }
        trainCoachRepository.saveAll(coaches);
        return tr;
    }

    @Transactional
    public void createTrainCoachesFromCsv(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<train_coaches> allCoaches = new ArrayList<>();
            String[] coachTypes = {"SL", "3A", "2A", "1A", "2S", "GN"};

            for (CSVRecord csvRecord : csvParser) {
                String trainId = csvRecord.get("trainId");

                for (String coachType : coachTypes) {
                    if (csvRecord.isMapped(coachType)) {
                        String coachInfo = csvRecord.get(coachType);
                        if (coachInfo != null && !coachInfo.isBlank()) {
                            String[] parts = coachInfo.split("-");
                            if (parts.length == 2) {
                                int numberOfCoaches = Integer.parseInt(parts[0]);
                                int seatsPerCoach = Integer.parseInt(parts[1]);

                                for (int i = 0; i < numberOfCoaches; i++) {
                                    train_coaches coach = new train_coaches();
                                    coach.setTrainId(trainId);
                                    coach.setCoachType(coachType);
                                    coach.setCoachNumber(i + 1);
                                    coach.setTotalAvailableSeats(seatsPerCoach);
                                    allCoaches.add(coach);
                                }
                            }
                        }
                    }
                }
            }
            trainCoachRepository.saveAll(allCoaches);
        }
    }

    @Transactional
    public List<train> createTrainsFromJson(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<train>> typeReference = new TypeReference<List<train>>(){};
        List<train> trains = mapper.readValue(inputStream, typeReference);
        return trainRepository.saveAll(trains);
    }

    @Transactional
    public TrainDTO createTrain(TrainDTO tr) {
            train ttr = new train();
            ttr.setTrainID(tr.getTrainId());
            ttr.setName(tr.getTrainName());
            ttr.setType(Type.valueOf(tr.getTrainType().toUpperCase()));
            ttr.setMonday(tr.isMonday());
            ttr.setSaturday(tr.isSaturday());
            ttr.setSunday(tr.isSunday());
            ttr.setThursday(tr.isThursday());
            ttr.setWednesday(tr.isWednesday());
            ttr.setTuesday(tr.isTuesday());
            ttr.setFriday(tr.isFriday());

            List<TrainRouteDTO> routeDTOs = tr.getRoute();
            List<trainRoute> routes = new ArrayList<>();
            for(int i=0;i<routeDTOs.size();i++){
                if(i==0){
                    ttr.setSourceStation(routeDTOs.get(i).getStationCode());
                }

                if(i==routeDTOs.size()-1){
                    ttr.setDestinationStation(routeDTOs.get(i).getStationCode());
                }

                TrainRouteDTO dto = routeDTOs.get(i);
                trainRoute route = new trainRoute();
                route.setTrainId(tr.getTrainId());
                route.setStationCode(dto.getStationCode());
                Station temp=stationRepository.findByStationCode(dto.getStationCode());
                if(temp!=null){
                    route.setPlace(temp.getPlace());
                }
                route.setArrivalTime(dto.getArrivalTime());
                route.setDepartureTime(dto.getDepartureTime());
                route.setDayNumber(dto.getDayNumber());
                route.setSequence(i);
                route.setDistanceFromSource(dto.getDistanceFromSource());
                routes.add(route);
            }
            trainRepository.save(ttr);
            trainRouteRepository.saveAll(routes);
//        return trainRouteRepository.save(tr);
        return tr;
    }


    @Transactional // Ensures the entire method runs in a single transaction
    public void processAndSaveRoutes(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                // --- 1. Process Train Information ---
                Long trainNumber = Long.parseLong(csvRecord.get("train_id"));
                String trainName = csvRecord.get("name");
                String trainType = csvRecord.get("type");

                // Find existing train or create a new one
                train train = new train();
                train.setTrainID(trainNumber + "");
                train.setName(trainName);
                train.setType(Type.valueOf(trainType.toUpperCase()));
                 // Placeholder, you might want to extract from CSV
//                train.setDestinationStation("DST"); // Placeholder, you might want to extract from CSV


                List<trainRoute> routes=new ArrayList<>();
                // --- 2. Process Route Information ---
                // We assume station columns start after the first 3 columns
                int stopNumber = 0;
                for (int i = 3; i < csvRecord.size(); i++) {
                    String routeInfo = csvRecord.get(i);

                    if (routeInfo == null || routeInfo.isBlank()) break;

                    String[] parts = routeInfo.split("-"); // Split by "-"
                    if (parts.length != 5) {
                        // Handle malformed data for a specific cell, maybe log it
                        continue;
                    }

                    String stationCode = parts[0];
                    Station station = stationRepository.findByStationCode(stationCode);
                    String place=new String();
                    if(station!=null){
                        place=station.getPlace();
                    }

                    LocalTime arrivalTime = LocalTime.parse(parts[1]);
                    LocalTime departureTime = LocalTime.parse(parts[2]);
                    int journeyDay = Integer.parseInt(parts[3]);
                    int distanceFromSource = Integer.parseInt(parts[4]); // Placeholder, you might want to extract from CSV


                    if(i==3)  train.setSourceStation(stationCode);   //Setting source Station of Train.

                    if(i==csvRecord.size()-1 || csvRecord.get(i+1).isBlank() || csvRecord.get(i+1)==null) train.setDestinationStation(stationCode); //Setting Destination Station.


                    // Find existing station or create a new one
//                    Station station = stationRepository.findByStationCode(stationCode)
//                            .orElse(new Station(stationCode, "Station Name Placeholder")); // You might need a way to get full station names
//                    stationRepository.save(station);

                    // Create the Route entity
                    trainRoute route = new trainRoute();
                    route.setTrainId(trainNumber + "");
                    route.setStationCode(stationCode);
                    route.setPlace(place);
                    route.setArrivalTime(arrivalTime);
                    route.setDepartureTime(departureTime);
                    route.setDayNumber(journeyDay);
                    route.setSequence(stopNumber++);
                    route.setDistanceFromSource(distanceFromSource);

                    routes.add(route);
                }
                trainRepository.save(train);
                trainRouteRepository.saveAll(routes);
            }
        }
    }

        @Transactional
        public List<train> createTrainsFromCsv(InputStream inputStream) throws IOException {
            // Use CSVFormat.Builder to construct the format, as withFirstRecordAsHeader() is deprecated.
            CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader() // This indicates the first record is the header.
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            try (Reader reader = new InputStreamReader(inputStream);
                 CSVParser csvParser = new CSVParser(reader, csvFormat)) {

                List<train> trains = new ArrayList<>();
                for (CSVRecord csvRecord : csvParser) {
                    train t = new train();
                    // Corrected the typo from "triainid" to "trainId"
                    t.setTrainID(csvRecord.get("trainid"));
                    t.setName(csvRecord.get("name"));
                    t.setType(Type.valueOf(csvRecord.get("type").toUpperCase()));
                    t.setSourceStation(csvRecord.get("sourceStation"));
                    t.setDestinationStation(csvRecord.get("destinationStation"));
                    trains.add(t);
                }
                return trainRepository.saveAll(trains);
            }
        }

    /* ===================== Update ===================== */
    @Transactional
    public train updateTrain(train t) {
        if (t.getTrainId() == null || t.getTrainId().isBlank()) {
            throw new IllegalArgumentException("trainId is required for update");
        }
        train existing = trainRepository.findById(t.getTrainId())
                .orElseThrow(() -> new IllegalArgumentException("Train with id " + t.getTrainId() + " not found"));

        // Preserve fields not provided if partial updates are allowed (currently all required, so direct copy)
        existing.setName(t.getName());
        existing.setType(t.getType());
        existing.setSourceStation(t.getSourceStation());
        existing.setDestinationStation(t.getDestinationStation());

        try {
            return trainRepository.save(existing);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Update violates unique constraints (possibly name)", ex);
        }
    }

    /* ===================== Retrieval ===================== */
    public Optional<train> findById(String id) { // PK lookup
        return trainRepository.findById(id);
    }

    public List<train> findAll() {
        return trainRepository.findAll();
    }

    public Optional<train> findByName(String name) {
        return trainRepository.findByName(name);
    }

    public List<train> findByType(Type type) {
        return trainRepository.findByType(type);
    }

    public List<train> findBySourceStation(String sourceStation) {
        return trainRepository.findBySourceStation(sourceStation);
    }

    @Cacheable(value = "trains", key = "#source + '-' + #destination + '-' + #dateOfJourney.toString()")
    public List<train> findTrains(String source, String destination, LocalDate dateOfJourney) {

        System.out.println("Fetching from DB for " + source + " to " + destination + " on " + dateOfJourney.toString());
        List<train> trains=new ArrayList<>();
        Set<train> trainSet=new HashSet<>();
        HashMap<String,Integer> trainMap=new HashMap<>();

        String dayOfWeek=dateOfJourney.getDayOfWeek().toString();

        Station src= stationRepository.findByStationCode(source);
        Station dest= stationRepository.findByStationCode(destination);
        List<trainRoute> sourceTrains=trainRouteRepository.findByPlace(src.getPlace());
        List<trainRoute> destinationTrains=trainRouteRepository.findByPlace(dest.getPlace());

        // Filtering trains that do not run on the specified day
        Iterator<trainRoute> sourceIterator = sourceTrains.iterator();
        while (sourceIterator.hasNext()) {
            trainRoute a = sourceIterator.next();
            Optional<train> temp = trainRepository.findById(a.getTrainId());
            if (temp.isPresent() && !runsOnDay(temp.get(), dayOfWeek)) {
                sourceIterator.remove();
            }
        }

        Iterator<trainRoute> destIterator = destinationTrains.iterator();
        while (destIterator.hasNext()) {
            trainRoute a = destIterator.next();
            Optional<train> temp = trainRepository.findById(a.getTrainId());
            if (temp.isPresent() && !runsOnDay(temp.get(), dayOfWeek)) {
                destIterator.remove();
            }
        }



        for(int i=0;i<sourceTrains.size();i++){
            trainMap.put(sourceTrains.get(i).getTrainId(),sourceTrains.get(i).getSequence());
        }

        for(trainRoute a:destinationTrains){
            if(trainMap.containsKey(a.getTrainId()) && trainMap.get(a.getTrainId())<a.getSequence()){
                Optional<train> temp=trainRepository.findById(a.getTrainId());
                if(temp.isPresent() && !trainSet.contains(temp.get())){
                    trains.add(temp.get());
                    trainSet.add(temp.get());
                }


            }
        }

        return trains;
    }
    public boolean runsOnDay(train t, String dayOfWeek) {
//

            return switch (dayOfWeek) {
                case "MONDAY" -> t.isMonday();
                case "TUESDAY" -> t.isTuesday();
                case "WEDNESDAY" -> t.isWednesday();
                case "THURSDAY" -> t.isThursday();
                case "FRIDAY" -> t.isFriday();
                case "SATURDAY" -> t.isSaturday();
                case "SUNDAY" -> t.isSunday();
                default ->false;
            };


    }
    public List<train> findByDestinationStation(String destinationStation) {
        return trainRepository.findByDestinationStation(destinationStation);
    }

    public List<train> findBySourceAndDestination(String source, String destination) {
        return trainRepository.findBySourceStationAndDestinationStation(source, destination);
    }

    public List<train> findBidirectionalBetween(String stationA, String stationB) {
        return trainRepository.findBidirectionalBetween(stationA, stationB);
    }

    public List<train> searchByNameOrId(String term) {
        if (term == null || term.isBlank()) return List.of();
        return trainRepository.searchByNameOrId(term.trim());
    }

    /* ===================== Existence ===================== */
    public boolean existsByName(String name) {
        return name != null && trainRepository.existsByName(name);
    }

    public boolean existsByTrainId(String trainId) {
        return trainId != null && trainRepository.existsByTrainId(trainId);
    }

    /* ===================== Deletion ===================== */
    @Transactional
    public void deleteById(String id) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("trainId is required for delete");
        if (!trainRepository.existsByTrainId(id)) return; // idempotent
        trainRepository.deleteById(id);
    }

    @Transactional
    public void deleteByTrainId(String trainId) { // alias for clarity
        deleteById(trainId);
    }

    @Transactional
    public void deleteByName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name is required for delete");
        trainRepository.deleteByName(name);
    }

    /* ===================== Validation Helpers ===================== */
    private void validateNewTrain(train t) {
        if (t == null) throw new IllegalArgumentException("Train cannot be null");
        if (t.getTrainId() == null || t.getTrainId().isBlank()) {
            throw new IllegalArgumentException("trainId is required");
        }
        if (existsByTrainId(t.getTrainId())) {
            throw new IllegalArgumentException("Train with id " + t.getTrainId() + " already exists");
        }
        if (t.getName() == null || t.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (existsByName(t.getName())) {
            throw new IllegalArgumentException("Train with name '" + t.getName() + "' already exists");
        }
        if (t.getType() == null) {
            throw new IllegalArgumentException("type is required");
        }
        if (t.getSourceStation() == null || t.getSourceStation().isBlank()) {
            throw new IllegalArgumentException("sourceStation is required");
        }
        if (t.getDestinationStation() == null || t.getDestinationStation().isBlank()) {
            throw new IllegalArgumentException("destinationStation is required");
        }
    }
}

