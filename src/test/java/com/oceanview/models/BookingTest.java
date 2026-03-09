package com.oceanview.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Booking model (getters/setters and defaults).
 */
class BookingTest {

    @Test
    @DisplayName("Booking getters and setters")
    void testBookingFields() {
        Booking b = new Booking();
        b.setBookingId(1);
        b.setReservationNumber("OV-20250601-001");
        b.setUserId(10);
        b.setRoomId(2);
        b.setCheckIn("2025-06-01");
        b.setCheckOut("2025-06-03");
        b.setTotalPrice(330.0);
        b.setStatus("CONFIRMED");

        assertEquals(1, b.getBookingId());
        assertEquals("OV-20250601-001", b.getReservationNumber());
        assertEquals(10, b.getUserId());
        assertEquals(2, b.getRoomId());
        assertEquals("2025-06-01", b.getCheckIn());
        assertEquals("2025-06-03", b.getCheckOut());
        assertEquals(330.0, b.getTotalPrice(), 0.001);
        assertEquals("CONFIRMED", b.getStatus());
    }
}
