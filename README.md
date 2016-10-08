A Lost and Found Application

To run:
mvn clean exec:java

#Service
findme


#How to play with this app:

Post a user: curl -X POST -H "Content-Type: application/json" -d '{"id":"sharjjeel"}' http://localhost:8080/users

Get all items: curl -X GET http://localhost:8080/item

Post an item: curl -X POST -H "Content-Type: application/json" -d '{"user_id":"sharjjeel", "name":"lost item"}' http://localhost:8080/item

Tables:

users (
    id varchar(100) primary key,
    name varchar(100)
);

items (
    id varchar(100) primary key,
    name varchar(100),
    description text,
    timestamp varchar(100),
    location varchar(100),
    claim_status varchar(100),
    user_id varchar(100) references users(id),
    lost boolean
);

Add fields to cURL's data section