A Lost and Found Application

To run:
mvn clean exec:java

Then open postgres and type the following queries as admin:

`create user test_username with password 'test_password';`

`create database lostandfound;`

`grant all privileges on database lostandfound to test_username;`

```
 drop table if exists items;
 drop table if exists users;

 create table users (
     id varchar(100) primary key,
     name varchar(100)
 );

create table items (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    longitude double precision,
    latitude double precision,
    claim_status varchar(100),
    user_id varchar(100) references users(id),
    lost boolean
);

 create index user_id on users (id);
 create index item_user on items (user_id);
 create index item_claims on items (claim_status);

```

#Service
findme


#How to play with this app:

Post a user: curl -X POST -H "Content-Type: application/json" -d '{"id":"sharjjeel", "name":"sharjjeel", "password":"pa$$word"}' http://localhost:8080/users

Get all items: curl -X GET http://localhost:8080/item

Post an item: curl -X POST -H "Content-Type: application/json" -d '{"user_id":"sharjjeel", "name":"lost item"}' http://localhost:8080/item

Put an item: curl -vv -X PUT -H "Content-Type: application/json" -d '{"id":"3feb2b72-ebbe-40c8-a3de-70263fc20aba", "user_id":"sharjjeel", "name":"lost items", "lost":"true"}' http://localhost:8080/item

Get an Item in a location: curl -vv -X GET -H "Content-Type: application/json" 'http://localhost:8080/item/lostItems?longitude=0&latitude=0&radius=5'
Tables:

users (
    id varchar(100) primary key,
    name varchar(100),
    password text
);

items (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    location varchar(100),
    user_id varchar(100) references users(id),
    lost boolean
);

Add fields to cURL's data section

            <property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/lostandfound" />
            <property name="javax.persistence.jdbc.driver" value="org.postgresql.Driver" />
            <property name="javax.persistence.jdbc.user" value="test_username" />
            <property name="javax.persistence.jdbc.password" value="test_password" />