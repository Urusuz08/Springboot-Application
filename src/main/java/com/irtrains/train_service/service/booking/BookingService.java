package com.irtrains.train_service.service.booking;

import com.irtrains.train_service.model.booking.Booking;
import com.irtrains.train_service.repository.Booking.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        // Generate PNR, set booking date, and status
        booking.setPnrNumber(java.util.UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        booking.setBookingDate(new java.util.Date());
        booking.setStatus("CONFIRMED");
        return bookingRepository.save(booking);
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
