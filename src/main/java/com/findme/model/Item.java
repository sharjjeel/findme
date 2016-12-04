package com.findme.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;

/**
 * Created by sharjjeel on 7/11/16.
 */
/*
create table item (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    location varchar(100),
    claim_status varchar(100),
    user_id varchar(100) references users(id),
    lost boolean
);
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // TODO: fix this and make it getUserId
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    @JsonView
    private String id;

    @JsonView
    private String name;

    @JsonView
    private String description;

    @JsonView
    private String timestamp;

    @JsonView
    private double longitude;

    @JsonView
    private double latitude;

    @JsonView
    private String user_id;

    @JsonView
    private boolean lost;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", user_id='" + user_id + '\'' +
                ", lost=" + lost +
                '}';
    }
}
