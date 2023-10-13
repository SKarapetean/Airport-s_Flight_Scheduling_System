Introduction
This Java application simulates an airport's flight scheduling system. It handles multiple runways and gates, managing both arriving and departing flights. The application provides a command-line interface for users to interact with the simulation, allowing them to add, change, and cancel flights. Additionally, users can stop the simulation at any point.

Purpose of the Application
The primary purpose of the application is to demonstrate the implementation of a flight scheduling system using Java. The system employs object-oriented programming techniques, multithreading, and design patterns to model the entities involved in airport operations.

Getting Started
Prerequisites
Java Development Kit (JDK) installed
Maven or Gradle for building the project (optional)
Installation
Clone the repository:

bash
git clone <https://github.com/SKarapetean/Airport-s_Flight_Scheduling_System.git>
Build the project:

bash
mvn clean install   # Example for Maven
Running the Application
Run the compiled program:

bash
java -jar target/airport-simulation.jar
Follow the on-screen instructions to interact with the simulation.

Application Structure
The application is structured as follows:

org.example package: Root package for the application.
Subpackages for different components, such as flights, gates, managers, observer, runways, and airport_control.
App.java: Main class containing the entry point of the application.
Key Components
Airport: Class representing the airport
Flight: Base class representing flight details.
ArrivalFlight: Subclass representing arriving flights.
DepartingFlight: Subclass representing departing flights.
Gate: Class representing airport gates.
Runway: Class representing airport runways.
GateManager: Class managing gates and allocating them to flights.
RunwayManager: Class managing runways and allocating them to flights.
AirportTower: Class managing the central control tower.
EventManager: Class for handling and dispatching events.

Application Workflow
Users enter commands to add, change, or cancel flights, or stop the simulation.
The application processes these commands and updates the flight schedule accordingly.
Multithreading is used to simulate real-time behavior, with each runway and gate operating independently but coordinated by the control tower.

How to Run the Application
Preparing the Environment
Ensure that you have the required prerequisites installed.

Running the Application
Compile the Java file:

bash
javac FlightScheduler.java
Run the compiled program:

bash
java FlightScheduler
Follow the on-screen instructions to interact with the simulation.

Design Patterns Used
The following design patterns are used in this application:

Singleton Pattern
The Singleton Pattern is used to ensure that only one instance of key classes, such as Airport, GateManager, RunwayManager, EventManager, and AirportTower, are created. This ensures centralized and efficient control of these components.

Observer Pattern
The Observer Pattern is used to manage and dispatch events within the application. Key components, such as RunwayManager and GateManager, subscribe to events and respond to them as flights arrive and depart.

Explanation and Usage
Singleton Pattern: Ensures single instances of critical components, preventing conflicts and ensuring efficient resource allocation.
Observer Pattern: Facilitates communication and coordination among various components of the airport simulation, allowing them to react to flight-related events.