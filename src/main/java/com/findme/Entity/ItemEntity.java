package com.findme.Entity;

import com.findme.model.Item;

import javax.persistence.*;

/*
create table items (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    location varchar(100),
    claim_status varchar(100),
    user_id varchar(100) references users(id)
);
 */
@Entity(name = "items")
public class ItemEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "location")
    private String location;

    @Column(name = "claim_status")
    private String claim_status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    private UserEntity user;

    @Column(name = "lost")
    private boolean lost;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClaim_status() {
        return claim_status;
    }

    public void setClaim_status(String claim_status) {
        this.claim_status = claim_status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public Item getObject() {
        Item item = new Item();
        item.setClaim_status(claim_status);
        item.setDescription(description);
        item.setId(id);
        item.setLocation(location);
        item.setName(name);
        item.setTimestamp(timestamp);
        item.setUser_id(user.getId());
        return item;
    }
}