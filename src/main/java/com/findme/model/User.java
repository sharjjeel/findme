package com.findme.model;

import com.findme.Entity.ItemEntity;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

/*
create table users (
    id varchar(100) primary key,
    name varchar(100)
);
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonView
    private String id;

    @JsonView
    private String name;

    @JsonView
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ItemEntity> items;


    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}