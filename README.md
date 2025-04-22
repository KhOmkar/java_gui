# java_gui
Railway Management website with historical information

## Database contruction 
```
create database mini_dbms;
use mini_dbms;

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

INSERT INTO Train (train_id, train_name, capacity, train_type, no_of_coaches, total_seats, engine_type, fuel_efficiency) VALUES
(1, 'Rajdhani Express', 72, 'Express', 20, 1440, 'Electric', 3.5),
(2, 'Shatabdi Express', 78, 'Express', 16, 1248, 'Electric', 3.8),
(3, 'Duronto Express', 80, 'Express', 18, 1440, 'Electric', 3.6),
(4, 'Gatimaan Express', 78, 'Semi-High-Speed', 12, 936, 'Electric', 4.0),
(5, 'Tejas Express', 78, 'Semi-High-Speed', 12, 936, 'Electric', 4.2);

INSERT INTO Station (station_id, station_name, location, no_of_platforms, working_hours, daily_passenger_capacity, parking_capacity) VALUES
(1, 'Chhatrapati Shivaji Maharaj Terminus (CSMT)', 'Mumbai, Maharashtra', 18, '24:00:00', 1000000, 500),
(2, 'Howrah Junction (HWH)', 'Howrah, West Bengal', 23, '24:00:00', 1800000, 600),
(3, 'Chennai Central (MAS)', 'Chennai, Tamil Nadu', 17, '24:00:00', 550000, 400),
(4, 'Royapuram Station (RPM)', 'Chennai, Tamil Nadu', 4, '24:00:00', 50000, 100),
(5, 'Jhansi Junction (VGLB)', 'Jhansi, Uttar Pradesh', 8, '24:00:00', 200000, 200),
(6, 'Old Delhi Railway Station (DLI)', 'Delhi', 16, '24:00:00', 200000, 300);

INSERT INTO Schedule (schedule_id, train_id, origin_station_id, dest_station_id, departure_time, arrival_time, platform_number) VALUES
(1, 1, 1, 2, '2025-04-22 16:00:00', '2025-04-23 10:00:00', 8),
(2, 2, 3, 2, '2025-04-22 06:00:00', '2025-04-22 18:00:00', 5),
(3, 3, 2, 6, '2025-04-22 20:00:00', '2025-04-23 08:00:00', 10),
(4, 4, 5, 1, '2025-04-22 07:00:00', '2025-04-22 15:00:00', 3),
(5, 5, 1, 3, '2025-04-22 14:00:00', '2025-04-22 22:00:00', 6);

INSERT INTO EnvironmentalImpact (impact_id, train_id, carbon_emission, noise_level, energy_consumption, measured_date, measuring_method, water_consumption) VALUES
(1, 1, 150.00, 85.00, 1200.00, '2025-04-01', 'Onboard Sensors', 500.00),
(2, 2, 130.00, 80.00, 1100.00, '2025-04-01', 'Onboard Sensors', 450.00),
(3, 3, 140.00, 82.00, 1150.00, '2025-04-01', 'Onboard Sensors', 470.00),
(4, 4, 120.00, 78.00, 1000.00, '2025-04-01', 'Onboard Sensors', 400.00),
(5, 5, 115.00, 75.00, 950.00, '2025-04-01', 'Onboard Sensors', 380.00);

INSERT INTO RailwayHeritage (heritage_id, station_id, historical_significance, year_established, architectural_style, historical_events) VALUES
(1, 1, 'UNESCO World Heritage Site; blend of Victorian Gothic Revival and traditional Indian architecture.', 1888, 'Indo-Saracenic', 'Headquarters of the Great Indian Peninsular Railway.'),
(2, 2, 'One of the oldest and busiest stations in India; significant Victorian-era architecture.', 1854, 'Victorian', 'Central hub connecting Kolkata to major cities.'),
(3, 4, 'Oldest operational station in India; first train in South India started here.', 1856, 'Colonial', 'Madras and Southern Mahratta Railway HQ until 1922.'),
(4, 6, 'Inspired by Red Fort; integral to Delhi\'s railway history.', 1864, 'Mughal-inspired', 'Key station during British era, connecting Delhi.');

INSERT INTO WeatherImpact (weather_id, schedule_id, weather_condition, delay_minutes, safety_measures, temperature, visibility, wind_speed) VALUES
(1, 1, 'Clear', 0, 'Standard precautions', 32.5, 10.0, 5.2),
(2, 2, 'Rain', 15, 'Slower speeds and wet brake checks', 26.0, 6.5, 12.4),
(3, 3, 'Fog', 30, 'Horn alerts and low visibility protocols', 19.0, 2.0, 3.1),
(4, 4, 'Clear', 0, 'Standard precautions', 35.0, 12.0, 4.8),
(5, 5, 'Thunderstorm', 45, 'Delays and route monitoring', 28.0, 5.0, 15.0);

INSERT INTO CrowdManagement (crowd_id, station_id, timestamp, density_level, congestion_zones, flow_rate, peak_hours, threshold_exceeded, diversion_measures) VALUES
(1, 1, '2025-04-20 09:00:00', 'High', 'Entry Gate 1, Platform 4', 250.5, '08:00-10:00,17:00-19:00', TRUE, 'Redirect to platform 6, open extra gates.'),
(2, 2, '2025-04-20 18:00:00', 'Very High', 'Footbridge, Platform 1', 400.0, '17:00-20:00', TRUE, 'Use alternate entry, deploy police.'),
(3, 3, '2025-04-20 07:30:00', 'Medium', 'Ticket Counter', 180.0, '07:00-09:00', FALSE, 'No action needed.'),
(4, 6, '2025-04-20 20:30:00', 'High', 'Exit gate, Platform 3', 290.0, '18:00-21:00', TRUE, 'Opened secondary exits.'),
(5, 5, '2025-04-20 08:45:00', 'Low', 'Main Hall', 75.0, '08:00-10:00', FALSE, 'No action needed.');

INSERT INTO SeatAvailability (schedule_id, coach_number, seat_number, is_available) VALUES
-- Schedule 1: Rajdhani Express
(1, 'C1', 'S1', TRUE),
(1, 'C1', 'S2', FALSE),
(1, 'C1', 'S3', TRUE),
(1, 'C2', 'S1', TRUE),
(1, 'C2', 'S2', TRUE),
(1, 'C2', 'S3', FALSE),

-- Schedule 2: Shatabdi Express
(2, 'C1', 'S1', TRUE),
(2, 'C1', 'S2', TRUE),
(2, 'C1', 'S3', TRUE),
(2, 'C2', 'S1', FALSE),
(2, 'C2', 'S2', TRUE),
(2, 'C2', 'S3', TRUE),

-- Schedule 3: Duronto Express
(3, 'C1', 'S1', TRUE),
(3, 'C1', 'S2', TRUE),
(3, 'C1', 'S3', TRUE),
(3, 'C2', 'S1', TRUE),
(3, 'C2', 'S2', FALSE),
(3, 'C2', 'S3', TRUE),

-- Schedule 4: Gatimaan Express
(4, 'C1', 'S1', TRUE),
(4, 'C1', 'S2', TRUE),
(4, 'C1', 'S3', TRUE),
(4, 'C2', 'S1', TRUE),
(4, 'C2', 'S2', TRUE),
(4, 'C2', 'S3', FALSE),

-- Schedule 5: Tejas Express
(5, 'C1', 'S1', FALSE),
(5, 'C1', 'S2', TRUE),
(5, 'C1', 'S3', TRUE),
(5, 'C2', 'S1', TRUE),
(5, 'C2', 'S2', TRUE),
(5, 'C2', 'S3', TRUE);

-- Change departure_time and arrival_time from DATETIME to TIME
ALTER TABLE Schedule
MODIFY COLUMN departure_time TIME NOT NULL,
MODIFY COLUMN arrival_time TIME NOT NULL;


-- Add a new column for frequency
ALTER TABLE Schedule
ADD COLUMN frequency VARCHAR(50) DEFAULT 'Daily';

UPDATE Schedule SET
departure_time = '16:00:00',
arrival_time = '10:00:00',
frequency = 'Daily'
WHERE schedule_id = 1;

UPDATE Schedule SET
departure_time = '06:00:00',
arrival_time = '18:00:00',
frequency = 'Daily'
WHERE schedule_id = 2;

UPDATE Schedule SET
departure_time = '20:00:00',
arrival_time = '08:00:00',
frequency = 'Daily'
WHERE schedule_id = 3;

UPDATE Schedule SET
departure_time = '07:00:00',
arrival_time = '15:00:00',
frequency = 'Weekdays'
WHERE schedule_id = 4;

UPDATE Schedule SET
departure_time = '14:00:00',
arrival_time = '22:00:00',
frequency = 'Weekends'
WHERE schedule_id = 5;


```
