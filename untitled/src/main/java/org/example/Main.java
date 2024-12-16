package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.ProcessScheduling.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter processes as NAME(BURST_TIME, PRIORITY), e.g., A(5,1), B(9,2). Type 'END' to stop:");

        List<Process> processes = new ArrayList<>();
        while (true) {
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("END")) break;
            String[] parts = input.split("[(),]");
            if (parts.length == 2) {
                processes.add(new Process(parts[0], Integer.parseInt(parts[1])));
            } else if (parts.length == 3) {
                processes.add(new Process(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
            }
        }

        System.out.println("Enter scheduling type (FCFS, SJF, SRTF, RR, PRIORITY, SPN, EQUITY):");
        String schedulingType = sc.nextLine().trim().toUpperCase();

        switch (schedulingType) {
            case "FCFS":
                fcfs(processes);
                break;
            case "SJF":
                sjf(processes);
                break;
            case "SRTF":
                srtf(processes);
                break;
            case "RR":
                System.out.println("Enter quantum time:");
                int quantumRR = sc.nextInt();
                roundRobin(processes, quantumRR);
                break;
            case "PRIORITY":
                priorityScheduling(processes);
                break;
            case "SPN":
                System.out.println("Enter quantum time:");
                int quantumSPN = sc.nextInt();
                spnWithQuantum(processes, quantumSPN);
                break;
            case "EQUITY":
                System.out.println("Enter number of users:");
                int users = sc.nextInt();
                sc.nextLine();
                List<List<Process>> userProcesses = new ArrayList<>();

                for (int i = 0; i < users; i++) {
                    System.out.println("Enter processes for user " + (i + 1) + " as NAME(BURST_TIME), separated by spaces:");
                    String userInput = sc.nextLine();
                    List<Process> userList = new ArrayList<>();

                    for (String process : userInput.split(" ")) {
                        String[] userParts = process.split("[()]\s*");
                        userList.add(new Process(userParts[0], Integer.parseInt(userParts[1])));
                    }

                    userProcesses.add(userList);
                }

                System.out.println("Enter quantum time:");
                int quantumEquity = sc.nextInt();
                equityScheduling(userProcesses, quantumEquity);
                break;
            default:
                System.out.println("Invalid scheduling type!");
                break;
        }

        sc.close();
    }
}

