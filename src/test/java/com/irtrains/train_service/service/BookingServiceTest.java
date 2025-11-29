package com.irtrains.train_service.service;

import com.irtrains.train_service.DTO.BookingInfoDTO;
import com.irtrains.train_service.DTO.PassengerDTO;
import com.irtrains.train_service.model.Booking;
import com.irtrains.train_service.repository.BookingRepository;
import java.time.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private BookingService bookingService;

    private BookingInfoDTO bookingInfoDTO;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingInfoDTO = new BookingInfoDTO();
        bookingInfoDTO.setUserId("testUser");
        bookingInfoDTO.setTrainId("12345");
        bookingInfoDTO.setSourceStationCode("SRC");
        bookingInfoDTO.setDestinationStationCode("DST");
        bookingInfoDTO.setJourneyDate(LocalDate.now());
        bookingInfoDTO.setTotalFare(100.0);
        bookingInfoDTO.setContactNumber("1234567890");
        bookingInfoDTO.setEmail("test@example.com");
        bookingInfoDTO.setCoachType("AC");
        List<PassengerDTO> passengers = new ArrayList<>();
        passengers.add(new PassengerDTO());
        bookingInfoDTO.setPassengers(passengers);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPnrNumber("PNR123456");
        booking.setUserId("testUser");
    }

    @Test
    void testAddBooking() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        doNothing().when(passengerService).addPassenger(anyList(), anyList());

        Booking result = bookingService.addBooking(bookingInfoDTO);

        assertNotNull(result);
        assertEquals(booking.getBookingId(), result.getBookingId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
        verify(passengerService, times(1)).addPassenger(anyList(), anyList());
    }

    @Test
    void testGetBookingByPnrNumber_Found() {
        when(bookingRepository.findByPnrNumber("PNR123456")).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingByPnrNumber("PNR123456");

        assertNotNull(result);
        assertEquals("PNR123456", result.getPnrNumber());
    }

    @Test
    void testGetBookingByPnrNumber_NotFound() {
        when(bookingRepository.findByPnrNumber("PNR123456")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            bookingService.getBookingByPnrNumber("PNR123456");
        });
    }

    @Test
    void testGetBookingsByUserId() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        when(bookingRepository.findByUserId("testUser")).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByUserId("testUser");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getUserId());
    }

    @Test
    void testCancelBooking() {
        when(bookingRepository.findByPnrNumber("PNR123456")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        bookingService.cancelBooking("PNR123456");

        assertEquals("CANCELLED", booking.getStatus());
        verify(bookingRepository, times(1)).save(booking);
    }
}

