package org.example;


import org.example.airport.Airport;

import java.time.LocalDateTime;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {

        System.out.println(
                "Commands instruction " + "\n" +
                "Add:\n" +
                "   Rule: If a new flight is added.\n" +
                "   Action: Add the new flight with its scheduled time to the flight schedule.\n" +
                        " Scheduled time should be in that format year/mont/day/time/minutes." +
                        " Flight should be arrival or departure!" +
                "\n" +
                "Change:\n" +
                "   Rule: If the scheduled time of a flight is changed.\n" +
                "   Action: Update the scheduled time of the specified flight to the new time.\n" +
                        "Estimated time should be in that format year/mont/day/time/minutes " +
                        "Flight should be arrival or departure!" +
                        "\n" +
                "Cancel:\n" +
                "   Rule: If a flight is canceled.\n" +
                "   Action: Remove the specified flight from the flight schedule.\n" +
                "\n" +
                "Stop:" +
                "\n" +
                "   Rule: If the process is stopped.\n" +
                "   Action: Stop the execution of the flight scheduling process."
                );

        System.out.println("\n");
        Scanner scanner = new Scanner(System.in);
        int runways;
        int gates;

        while (true) {
            System.out.println("Number of gates should be more than 1");
            if (scanner.hasNextInt() && (gates = scanner.nextInt()) >= 1) {
                scanner.nextLine();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }


        while (true) {
            System.out.println("Number of runways should be more than 1");
            if (scanner.hasNextInt() && (runways = scanner.nextInt()) >= 1) {
                scanner.nextLine();
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
            }
        }

        Airport airport = Airport.getInstance(runways, gates);
        String flightCode = "ORY";
        String flightNumber;
        LocalDateTime localDateTime;
        String command;
        String flightType;
        String time;
        int[] splits;
        System.out.println("Simulation starts!\n");
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Enter the command!");
            command = scanner.next();
            try {
                switch (command.toLowerCase()) {
                    case "add":
                        System.out.println("Enter the flight number");
                        flightNumber = scanner.next();
                        System.out.println("Enter the flight type!");
                        flightType = scanner.next();
                        System.out.println("Enter the scheduled time!");
                        time = scanner.next();
                        splits = parseStringsToIntegers(time.split("/"));
                        localDateTime = LocalDateTime.of(splits[0], splits[1], splits[2], splits[3], splits[4]);
                        airport.addFlight(flightNumber, flightCode, localDateTime, flightType);
                        break;
                    case "change":
                        System.out.println("Enter the flight number");
                        flightNumber = scanner.next();
                        System.out.println("Enter the estimated time!");
                        time = scanner.next();
                        splits = parseStringsToIntegers(time.split("/"));
                        localDateTime = LocalDateTime.of(splits[0], splits[1], splits[2], splits[3], splits[4]);
                        airport.changeTimeOfFlight(flightNumber, localDateTime);
                        break;
                    case "cancel":
                        System.out.println("Enter the flight number");
                        flightNumber = scanner.next();
                        airport.cancelFlight(flightNumber);
                        break;
                    case "stop":
                            airport.stop();
                            Thread.currentThread().interrupt();
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        while (!airport.isStop()) {

        }

        System.out.println("Simulation ended!");
        System.exit(0);
    }

    private static int[] parseStringsToIntegers(String[] stringArray) throws NumberFormatException{
        int[] intArray = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
                intArray[i] = Integer.parseInt(stringArray[i]);
        }

        return intArray;
    }

}
