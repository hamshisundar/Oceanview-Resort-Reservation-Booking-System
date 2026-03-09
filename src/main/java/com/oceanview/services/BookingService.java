package com.oceanview.services;

import com.oceanview.dao.BookingDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.models.Booking;
import com.oceanview.models.Room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for creating bookings, calculating total, generating reservation number.
 */
public class BookingService {

    private static final DateTimeFormatter RES_NUM_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final BookingDAO bookingDAO = new BookingDAO();
    private final RoomDAO roomDAO = new RoomDAO();

    public Optional<Booking> createBooking(int userId, int roomId, String checkIn, String checkOut) {
        Optional<Room> roomOpt = roomDAO.findById(roomId);
        if (roomOpt.isEmpty()) return Optional.empty();
        if (bookingDAO.hasConflict(roomId, checkIn, checkOut, null))
            return Optional.empty();
        Room room = roomOpt.get();
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.parse(checkIn), LocalDate.parse(checkOut));
        if (nights <= 0) return Optional.empty();
        double totalPrice = nights * room.getPricePerNight();
        String reservationNumber = "OV-" + LocalDate.now().format(RES_NUM_DATE) + "-" + String.format("%03d", (int)(System.currentTimeMillis() % 1000));
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setTotalPrice(totalPrice);
        booking.setReservationNumber(reservationNumber);
        booking.setStatus("CONFIRMED");
        return bookingDAO.create(booking);
    }

    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDAO.findByUserId(userId);
    }

    public Optional<Booking> getByReservationNumber(String reservationNumber) {
        return bookingDAO.findByReservationNumber(reservationNumber);
    }

    public Optional<Booking> getById(int bookingId) {
        return bookingDAO.findById(bookingId);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    public boolean cancelBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "CANCELLED");
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        return bookingDAO.updateStatus(bookingId, status);
    }

    public boolean isRoomAvailable(int roomId, String checkIn, String checkOut) {
        return !bookingDAO.hasConflict(roomId, checkIn, checkOut, null);
    }

    /**
     * Calculate total price for a stay (nights × price per night). Rounded to one decimal place
     * to avoid floating-point drift (e.g. 3 × 85.17). Exposed for unit testing.
     */
    public static double calculateTotalPrice(long nights, double pricePerNight) {
        if (nights <= 0 || pricePerNight < 0) return 0;
        double total = nights * pricePerNight;
        return Math.round(total * 10.0) / 10.0;
    }

    /**
     * Compute number of nights between two dates (checkOut exclusive). Returns 0 if checkOut is
     * before or equal to checkIn, or if inputs are null/invalid. Exposed for unit testing.
     */
    public static long calculateNights(String checkIn, String checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        try {
            LocalDate in = LocalDate.parse(checkIn);
            LocalDate out = LocalDate.parse(checkOut);
            long nights = java.time.temporal.ChronoUnit.DAYS.between(in, out);
            return nights < 0 ? 0 : nights;
        } catch (Exception e) {
            return 0;
        }
    }
}
