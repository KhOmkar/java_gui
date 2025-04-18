# java_gui
Railway Management website with historical information

## Database contruction 
```
use mini_dbms;
show tables;

-- Table: Train
CREATE TABLE Train (
train_id INT PRIMARY KEY,
train_name VARCHAR(100) NOT NULL,
capacity INT NOT NULL,
train_type VARCHAR(50) NOT NULL,
no_of_coaches INT NOT NULL,
total_seats INT NOT NULL,
engine_type VARCHAR(50) NOT NULL,
fuel_efficiency DECIMAL(10,2)
);

-- Table: Station
CREATE TABLE Station (
station_id INT PRIMARY KEY,
station_name VARCHAR(100) NOT NULL,
location VARCHAR(255) NOT NULL,
no_of_platforms INT NOT NULL,
opening_hours TIME NOT NULL,
closing_hours TIME NOT NULL,
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

show tables;
desc station;

INSERT INTO Train (train_id, train_name, capacity, train_type, no_of_coaches, total_seats, engine_type, fuel_efficiency)
VALUES
(11061, 'Lokmanya Tilak - Darbhanga Pawan Express', 1500, 'Express', 24, 1450, 'Diesel', 3.5),
(12951, 'Mumbai Central - New Delhi Rajdhani Express', 1200, 'Superfast', 20, 1100, 'Electric', 5.2);

INSERT INTO Station (station_id, station_name, location, no_of_platforms, opening_hours, closing_hours, daily_passenger_capacity, parking_capacity)
VALUES
(1, 'Mumbai Central', 'Mumbai, Maharashtra', 7, '04:00:00', '23:59:00', 50000, 200),
(2, 'New Delhi', 'New Delhi, Delhi', 16, '00:00:00', '23:59:00', 60000, 300);

INSERT INTO Schedule (schedule_id, train_id, origin_station_id, dest_station_id, departure_time, arrival_time, platform_number)
VALUES
(1001, 11061, 1, 2, '2025-04-15 06:00:00', '2025-04-16 08:00:00', 3),
(1002, 12951, 1, 2, '2025-04-15 17:00:00', '2025-04-16 08:00:00', 5);

INSERT INTO Passenger (passenger_id, name, email, phone, date_of_birth, gender, preference)
VALUES
(501, 'Rahul Sharma', 'rahul.sharma@example.com', '+919876543210', '1990-05-20', 'Male', 'Window seat'),
(502, 'Anita Desai', 'anita.desai@example.com', '+919812345678', '1985-11-15', 'Female', 'Lower berth');

INSERT INTO Ticket (ticket_id, passenger_id, schedule_id, booking_date, seat_number, coach_number, fare_amount, payment_methods, ticket_status)
VALUES
(2001, 501, 1001, '2025-04-10', 'S1-45', 'S1', 750.00, 'UPI', 'Confirmed'),
(2002, 502, 1002, '2025-04-11', 'A1-12', 'A1', 1500.00, 'Credit Card', 'Confirmed');

INSERT INTO EnvironmentalImpact (impact_id, train_id, carbon_emission, noise_level, energy_consumption, measured_date, measuring_method, water_consumption)
VALUES
(301, 11061, 120.5, 85.0, 500.0, '2025-04-01', 'Standard Emission Test', 100.0),
(302, 12951, 95.0, 80.0, 450.0, '2025-04-01', 'Standard Emission Test', 90.0);

INSERT INTO RailwayHeritage (heritage_id, station_id, historical_significance, year_established, architectural_style, historical_events)
VALUES
(401, 1, 'One of the oldest stations in Mumbai, known for its colonial architecture.', 1930, 'Colonial', 'Inauguration of the first electric train in India.'),
(402, 2, 'Central hub connecting all parts of India, significant during independence movement.', 1926, 'British Colonial', 'Site of major freedom movement gatherings.');

INSERT INTO WeatherImpact (weather_id, schedule_id, weather_condition, delay_minutes, safety_measures, temperature, visibility, wind_speed)
VALUES
(601, 1001, 'Rainy', 30, 'Speed restrictions applied', 25.0, 2.0, 15.0),
(602, 1002, 'Clear', 0, 'Normal operations', 30.0, 10.0, 5.0);

INSERT INTO CrowdManagement (crowd_id, station_id, timestamp, density_level, congestion_zones, flow_rate, peak_hours, threshold_exceeded, diversion_measures)
VALUES
(701, 1, '2025-04-13 18:00:00', 'High', 'Platform 3, Exit Gate A', 150.0, '17:00-19:00', TRUE, 'Redirected passengers to Gate B'),
(702, 2, '2025-04-13 09:00:00', 'Medium', 'Platform 5', 100.0, '08:00-10:00', FALSE, 'Monitored by staff');

CREATE TABLE User (
user_id INT PRIMARY KEY,
email VARCHAR(100) UNIQUE NOT NULL,
password_hash VARCHAR(255) NOT NULL,
user_role ENUM('passenger', 'admin', 'staff') NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
last_login TIMESTAMP NULL
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
