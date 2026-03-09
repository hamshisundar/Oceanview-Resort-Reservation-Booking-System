package com.oceanview.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Room model.
 */
class RoomTest {

    @Test
    @DisplayName("Room getters and setters including boolean amenities")
    void testRoomFields() {
        Room r = new Room();
        r.setRoomId(1);
        r.setRoomName("Ocean View Premium");
        r.setRoomType("Ocean View Room");
        r.setPricePerNight(165.0);
        r.setDescription("Stunning view");
        r.setHasSeaView(true);
        r.setHasWifi(true);
        r.setHasAc(true);
        r.setHasPoolAccess(true);
        r.setBreakfastIncluded(true);
        r.setAvailable(true);

        assertEquals(1, r.getRoomId());
        assertEquals("Ocean View Premium", r.getRoomName());
        assertEquals("Ocean View Room", r.getRoomType());
        assertEquals(165.0, r.getPricePerNight(), 0.001);
        assertTrue(r.isHasSeaView());
        assertTrue(r.isHasWifi());
        assertTrue(r.isBreakfastIncluded());
        assertTrue(r.isAvailable());
    }
}
