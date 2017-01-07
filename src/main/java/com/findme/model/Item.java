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
    private String title;

    @JsonView
    private String description;

    @JsonView
    private String timestamp;

    @JsonView
    private double longitude;

    @JsonView
    private double latitude;

    @JsonView
    private boolean lost;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonView
    private String contact;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", contact='" + contact + '\'' +
                ", lost=" + lost +
                '}';
    }
}
