package com.oceanview.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * JSON body for create/update room via admin API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomWritePayload {

    @JsonAlias({"name", "roomName"})
    private String name;
    @JsonAlias({"type", "roomType"})
    private String type;
    @JsonAlias({"price", "pricePerNight"})
    private Double price;
    private String description;
    private Boolean available;
    private Boolean hasSeaView;
    private Boolean hasWifi;
    private Boolean hasAc;
    private Boolean hasPoolAccess;
    private Boolean breakfastIncluded;
    private String imagePath;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
    public Boolean getHasSeaView() { return hasSeaView; }
    public void setHasSeaView(Boolean hasSeaView) { this.hasSeaView = hasSeaView; }
    public Boolean getHasWifi() { return hasWifi; }
    public void setHasWifi(Boolean hasWifi) { this.hasWifi = hasWifi; }
    public Boolean getHasAc() { return hasAc; }
    public void setHasAc(Boolean hasAc) { this.hasAc = hasAc; }
    public Boolean getHasPoolAccess() { return hasPoolAccess; }
    public void setHasPoolAccess(Boolean hasPoolAccess) { this.hasPoolAccess = hasPoolAccess; }
    public Boolean getBreakfastIncluded() { return breakfastIncluded; }
    public void setBreakfastIncluded(Boolean breakfastIncluded) { this.breakfastIncluded = breakfastIncluded; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
