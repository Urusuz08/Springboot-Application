package com.irtrains.train_service.controller.bookings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.irtrains.train_service.DTO.BookingInfoDTO;
import com.irtrains.train_service.model.booking.Booking;
import com.irtrains.train_service.service.booking.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private ObjectMapper objectMapper;

    private Booking booking;
    private BookingInfoDTO bookingInfoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        booking = new Booking();
        booking.setBookingId(1L);
        booking.setPnrNumber("PNR123456");
        booking.setUserId("testUser");
        booking.setBookingDate(new Date());
        booking.setStatus("BOOKED");

        bookingInfoDTO = new BookingInfoDTO();
        // Set properties for bookingInfoDTO as needed for tests
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        when(bookingService.addBooking(any(BookingInfoDTO.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingInfoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pnrNumber").value("PNR123456"));
    }

    @Test
    void testCreateBooking_Failure() throws Exception {
        when(bookingService.addBooking(any(BookingInfoDTO.class))).thenThrow(new RuntimeException("Failed to create booking"));

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingInfoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCancelBooking_Success() throws Exception {
        doNothing().when(bookingService).cancelBooking("PNR123456");

        mockMvc.perform(put("/api/bookings/PNR123456/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking with PNR PNR123456 cancelled successfully."));
    }

    @Test
    void testCancelBooking_NotFound() throws Exception {
        when(bookingService.getBookingByPnrNumber("PNR123456")).thenThrow(new RuntimeException("Booking not found"));

        mockMvc.perform(get("/api/bookings/PNR123456"))
                .andExpect(status().isNotFound());
    }


    @Test
    void testGetBookingByPnrNumber_Success() throws Exception {
        when(bookingService.getBookingByPnrNumber("PNR123456")).thenReturn(booking);

        mockMvc.perform(get("/api/bookings/PNR123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pnrNumber").value("PNR123456"));
    }

    @Test
    void testGetBookingByPnrNumber_NotFound() throws Exception {
        when(bookingService.getBookingByPnrNumber("PNR123456")).thenThrow(new RuntimeException("Booking not found"));

        mockMvc.perform(get("/api/bookings/PNR123456"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBookingsByUserId_Success() throws Exception {
        when(bookingService.getBookingsByUserId("testUser")).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/api/bookings/user/testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("testUser"));
    }
}
