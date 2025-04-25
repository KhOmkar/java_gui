# Railway Management System

A Java Swing-based Railway Management System with modern UI and comprehensive functionality for managing train bookings, schedules, and passenger information.

## Features

### 1. User Authentication
- Secure login system with email and password
- User registration with passenger details
- Role-based access control (passenger/admin)
- Session management with last login tracking

### 2. Dashboard
- Modern UI with intuitive navigation
- Quick access to all major functions
- Real-time updates and notifications
- Personalized welcome message with user role display

### 3. Train Management
- View complete train schedules
- Real-time seat availability
- Multiple train classes (First, Second, Sleeper)
- Platform information
- Train type and capacity details

### 4. Booking System
- Interactive ticket booking interface
- Source and destination selection
- Date-based availability search
- Seat and coach selection
- Multiple payment method support
- Automatic seat allocation
- Real-time fare calculation

### 5. Ticket Management
- View booked tickets
- Cancel tickets with automatic refund
- Ticket status tracking
- Booking history
- Digital ticket generation

## Technical Details

### Architecture
```
RailwaySystem (Main Class)
├── Login System
│   ├── Authentication
│   └── User Registration
├── Dashboard
│   ├── Train Schedule
│   ├── Booking System
│   ├── Ticket Management
│   └── Search Functions
└── Database Management
    ├── User Data
    ├── Train Data
    ├── Booking Records
    └── Payment Processing
```

### Technologies Used
- Java Swing for GUI
- MySQL Database
- JDBC for database connectivity
- Modern UI components with Nimbus Look and Feel

### Class Structure
1. **RailwaySystem**: Main class handling initialization and login
2. **SignUpFrame**: User registration interface
3. **DashboardFrame**: Main user interface after login
4. **BookTicketFrame**: Ticket booking functionality
5. **CancelTicketFrame**: Ticket cancellation interface
6. **SearchTrainFrame**: Train search functionality
7. **MyTicketsFrame**: View booked tickets
8. **TrainScheduleFrame**: View train schedules
9. **StationItem**: Helper class for station data

### Program Flow

#### 1. Startup and Login
```java
// Initialize system
RailwaySystem system = new RailwaySystem();
system.show();

// Login validation
private void handleLogin(String email, String password) {
    // Validate credentials
    // Check user role
    // Update last login
    // Open dashboard
}
```

#### 2. Booking Process
1. Select source and destination
2. Choose travel date
3. View available trains
4. Select seat and coach
5. Make payment
6. Generate ticket
7. Update seat availability

#### 3. Database Operations
- Prepared statements for security
- Transaction management
- Connection pooling
- Error handling

## Database Schema

### Core Tables
```sql
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

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- MySQL Server
- MySQL Connector/J (JDBC driver)

### Configuration
1. Set up MySQL database using the provided schema
2. Update database connection parameters:
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/mini_dbms";
private static final String DB_USER = "your_username";
private static final String DB_PASSWORD = "your_password";
```

### Running the Application
1. Compile the Java files
2. Ensure MySQL server is running
3. Execute the main class `RailwaySystem`

## Security Features
- Password protection
- Prepared statements to prevent SQL injection
- Transaction management
- Session handling
- Input validation

## User Interface Features
- Modern color scheme:
  ```java
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
  ```
- Responsive design
- Interactive components
- Form validation
- Error handling
- Success notifications

## Future Enhancements
1. Password hashing implementation
2. Email verification
3. Ticket PDF generation
4. Advanced search filters
5. Mobile responsive design
6. Real-time train tracking
7. Payment gateway integration
8. Multi-language support

## Database Construction
[Previous database construction section remains unchanged...] 
