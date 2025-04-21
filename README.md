# java_gui
Railway Management website with historical information

## Database contruction 
```
create database mini_dbms;
use mini_dbms;
show tables;

-- Table: Train
CREATE TABLE Train (
    train_id INT PRIMARY KEY,
    train_name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    train_type VARCHAR(50) NOT NULL,
    no_of_coaches INT NOT NULL,
    total_seats INT  NULL,
    engine_type VARCHAR(50) NOT NULL,
    fuel_efficiency DECIMAL(10,2)
);

-- Table: Station
CREATE TABLE Station (
    station_id INT PRIMARY KEY,
    station_name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    no_of_platforms INT NOT NULL,
    working_hours TIME NOT NULL,
    daily_passenger_capacity INT,
    parking_capacity INT
);

-- Table: Schedule
CREATE TABLE Schedule (
    schedule_id INT PRIMARY KEY,
    train_id INT,
    origin_station_id INT,
    dest_station_id INT,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    platform_number INT,
    FOREIGN KEY (train_id) REFERENCES Train(train_id),
    FOREIGN KEY (origin_station_id) REFERENCES Station(station_id),
    FOREIGN KEY (dest_station_id) REFERENCES Station(station_id)
);

-- Table: Passenger
CREATE TABLE Passenger (
    passenger_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20),
    preference VARCHAR(255)
);

-- Table: Ticket
CREATE TABLE Ticket (
    ticket_id INT PRIMARY KEY,
    passenger_id INT,
    schedule_id INT,
    booking_date DATE NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    coach_number VARCHAR(10) NOT NULL,
    fare_amount DECIMAL(10,2) NOT NULL,
    payment_methods VARCHAR(50) NOT NULL,
    ticket_status VARCHAR(50) NOT NULL,
    FOREIGN KEY (passenger_id) REFERENCES Passenger(passenger_id),
    FOREIGN KEY (schedule_id) REFERENCES Schedule(schedule_id)
);

-- Table: EnvironmentalImpact
CREATE TABLE EnvironmentalImpact (
    impact_id INT PRIMARY KEY,
    train_id INT UNIQUE,
    carbon_emission DECIMAL(10,2),
    noise_level DECIMAL(10,2),
    energy_consumption DECIMAL(10,2),
    measured_date DATE NOT NULL,
    measuring_method VARCHAR(100),
    water_consumption DECIMAL(10,2),
    FOREIGN KEY (train_id) REFERENCES Train(train_id)
);

-- Table: RailwayHeritage
CREATE TABLE RailwayHeritage (
    heritage_id INT PRIMARY KEY,
    station_id INT UNIQUE,
    historical_significance TEXT,
    year_established INT,
    architectural_style VARCHAR(100),
    historical_events TEXT,
    FOREIGN KEY (station_id) REFERENCES Station(station_id)
);

-- Table: WeatherImpact
CREATE TABLE WeatherImpact (
    weather_id INT PRIMARY KEY,
    schedule_id INT,
    weather_condition VARCHAR(50) NOT NULL,
    delay_minutes INT,
    safety_measures TEXT,
    temperature FLOAT,
    visibility FLOAT,
    wind_speed FLOAT,
    FOREIGN KEY (schedule_id) REFERENCES Schedule(schedule_id)
);

-- Table: CrowdManagement
CREATE TABLE CrowdManagement (
    crowd_id INT PRIMARY KEY,
    station_id INT,
    timestamp DATETIME NOT NULL,
    density_level VARCHAR(50) NOT NULL,
    congestion_zones VARCHAR(255),
    flow_rate FLOAT,
    peak_hours VARCHAR(100),
    threshold_exceeded BOOLEAN NOT NULL,
    diversion_measures TEXT,
    FOREIGN KEY (station_id) REFERENCES Station(station_id)
);

CREATE TABLE User (
    user_id INT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Payment (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    ticket_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATETIME NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES Ticket(ticket_id)
);

CREATE TABLE SeatAvailability (
    availability_id INT PRIMARY KEY AUTO_INCREMENT,
    schedule_id INT NOT NULL,
    coach_number VARCHAR(10) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (schedule_id) REFERENCES Schedule(schedule_id),
    UNIQUE KEY unique_seat (schedule_id, coach_number, seat_number)
);
```
