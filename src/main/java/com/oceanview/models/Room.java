package com.oceanview.models;

/**
 * Room entity matching rooms table.
 */
public class Room {

    private int roomId;
    private String roomName;
    private String roomType;
    private double pricePerNight;
    private String description;
    private boolean hasSeaView;
    private boolean hasWifi;
    private boolean hasAc;
    private boolean hasPoolAccess;
    private boolean breakfastIncluded;
    private String imagePath;
    private boolean available;

    public Room() {}

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isHasSeaView() { return hasSeaView; }
    public void setHasSeaView(boolean hasSeaView) { this.hasSeaView = hasSeaView; }
    public boolean isHasWifi() { return hasWifi; }
    public void setHasWifi(boolean hasWifi) { this.hasWifi = hasWifi; }
    public boolean isHasAc() { return hasAc; }
    public void setHasAc(boolean hasAc) { this.hasAc = hasAc; }
    public boolean isHasPoolAccess() { return hasPoolAccess; }
    public void setHasPoolAccess(boolean hasPoolAccess) { this.hasPoolAccess = hasPoolAccess; }
    public boolean isBreakfastIncluded() { return breakfastIncluded; }
    public void setBreakfastIncluded(boolean breakfastIncluded) { this.breakfastIncluded = breakfastIncluded; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
