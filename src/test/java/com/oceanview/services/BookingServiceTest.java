package com.oceanview.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookingService (price and nights calculation).
 * Demonstrates TDD: tests define expected behaviour for core booking logic.
 */
class BookingServiceTest {

    @Test
    @DisplayName("TC007: Total price = nights × price per night")
    void testCalculateTotalPrice() {
        assertEquals(300.0, BookingService.calculateTotalPrice(2, 150.0), 0.001);
        assertEquals(0.0, BookingService.calculateTotalPrice(0, 150.0), 0.001);
        assertEquals(0.0, BookingService.calculateTotalPrice(2, 0.0), 0.001);
        assertEquals(0.0, BookingService.calculateTotalPrice(-1, 150.0), 0.001);
        assertEquals(255.5, BookingService.calculateTotalPrice(3, 85.17), 0.001);
    }

    @Test
    @DisplayName("TC008: Nights between check-in and check-out")
    void testCalculateNights() {
        assertEquals(3, BookingService.calculateNights("2025-06-01", "2025-06-04"));
        assertEquals(1, BookingService.calculateNights("2025-06-01", "2025-06-02"));
        assertEquals(0, BookingService.calculateNights("2025-06-01", "2025-06-01"));
        assertEquals(0, BookingService.calculateNights("2025-06-05", "2025-06-01"));
        assertEquals(0, BookingService.calculateNights(null, "2025-06-01"));
        assertEquals(0, BookingService.calculateNights("2025-06-01", null));
        assertEquals(0, BookingService.calculateNights("invalid", "2025-06-04"));
    }

    @Test
    @DisplayName("Price calculation edge case: single night")
    void testCalculateTotalPriceSingleNight() {
        assertEquals(85.0, BookingService.calculateTotalPrice(1, 85.0), 0.001);
    }

    @Test
    @DisplayName("Nights calculation: 7-night stay")
    void testCalculateNightsOneWeek() {
        assertEquals(7, BookingService.calculateNights("2025-07-01", "2025-07-08"));
    }
}
