package org.example;

import java.util.*;

public class ProcessScheduling {

    // First Come, First Serve (FCFS)
    public static void fcfs(List<Process> processes) {
        int time = 0;
        int totalExecutionTime = 0;
        int totalWaitingTime = 0;

        System.out.println("\nFCFS Scheduling:");

        for (Process p : processes) {
            time += p.burstTime;
            p.completionTime = time;
            totalExecutionTime += time;
            totalWaitingTime += (time - p.burstTime);
        }

        double avgExecutionTime = (double) totalExecutionTime / processes.size();
        double avgWaitingTime = (double) totalWaitingTime / processes.size();

        System.out.printf("Average Execution Time: %.2f\n", avgExecutionTime);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
    }

    // Shortest Job First (SJF)
    public static void sjf(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime));
        fcfs(processes);
    }

    // Shortest Remaining Time First (SRTF)
    public static void srtf(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int totalExecutionTime = 0;
        List<String> executionOrder = new ArrayList<>();

        while (completed < processes.size()) {
            Process current = processes.stream()
                    .filter(p -> p.remainingTime > 0)
                    .min(Comparator.comparingInt(p -> p.remainingTime))
                    .orElse(null);

            if (current != null) {
                int executedTime = current.remainingTime;
                executionOrder.add(current.name + "(" + executedTime + ")");
                time += executedTime;
                current.remainingTime = 0;
                current.completionTime = time;
                completed++;
            }
        }

        int n = executionOrder.size();
        double weightedExecutionSum = 0;
        for (int i = 0; i < executionOrder.size(); i++) {
            int multiplier = n - i;
            weightedExecutionSum += multiplier * Integer.parseInt(executionOrder.get(i).replaceAll("[^0-9]", ""));
        }
        double avgExecutionTime = weightedExecutionSum / n;

        double waitingSum = 0;
        for (Process p : processes) {
            waitingSum += (p.completionTime - p.burstTime);
        }
        double avgWaitingTime = waitingSum / processes.size();

        System.out.println("\nSRTF Scheduling:");
        System.out.println("Execution Order: " + String.join(" -> ", executionOrder));
        System.out.printf("Average Execution Time: %.2f\n", avgExecutionTime);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
    }

    // Round Robin (RR)
    public static void roundRobin(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int time = 0;
        int totalExecutionTime = 0;
        List<String> executionOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            Process current = queue.poll();
            if (current.remainingTime <= quantum) {
                time += current.remainingTime;
                executionOrder.add(current.name + "(" + current.remainingTime + ")");
                current.remainingTime = 0;
                current.completionTime = time;
                totalExecutionTime += time;
            } else {
                time += quantum;
                executionOrder.add(current.name + "(" + quantum + ")");
                current.remainingTime -= quantum;
                queue.add(current);
            }
        }

        double avgExecutionTime = (double) totalExecutionTime / processes.size();
        double totalWaitingTime = 0;

        for (Process p : processes) {
            totalWaitingTime += (p.completionTime - p.burstTime);
        }
        double avgWaitingTime = totalWaitingTime / processes.size();

        System.out.println("\nRound Robin Scheduling:");
        System.out.println("Execution Order: " + String.join(" -> ", executionOrder));
        System.out.printf("Average Execution Time: %.2f\n", avgExecutionTime);
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
    }

    // Priority Scheduling
    public static void priorityScheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.priority));
        fcfs(processes);
    }

    // Shortest Process Next with Quantum (Custom Scheduling)
    public static void spnWithQuantum(List<Process> processes, int quantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int time = 0;
        List<String> executionOrder = new ArrayList<>();
        int totalExecutionTime = 0;

        while (!queue.isEmpty()) {
            Process current = queue.poll();
            if (current.remainingTime <= quantum) {
                time += current.remainingTime;
                executionOrder.add(current.name + "(" + current.remainingTime + ")");
                current.remainingTime = 0;
                current.completionTime = time;
                totalExecutionTime += time;
            } else {
                time += quantum;
                executionOrder.add(current.name + "(" + quantum + ")");
                current.remainingTime -= quantum;
                queue.add(current);
            }
        }

        double avgExecutionTime = (double) totalExecutionTime / processes.size();
        System.out.println("\nSPN with Quantum Scheduling:");
        System.out.println("Execution Order: " + String.join(" -> ", executionOrder));
        System.out.printf("Average Execution Time: %.2f\n", avgExecutionTime);
    }

    public static void equityScheduling(List<List<Process>> userProcesses, int quantum) {
        Queue<List<Process>> userQueue = new LinkedList<>(userProcesses);
        int time = 0;
        List<String> executionOrder = new ArrayList<>();

        while (!userQueue.isEmpty()) {
            List<Process> currentUser = userQueue.poll();

            for (Process process : currentUser) {
                if (process.remainingTime > 0) {
                    if (process.remainingTime <= quantum) {
                        time += process.remainingTime;
                        executionOrder.add(process.name + "(" + process.remainingTime + ")");
                        process.remainingTime = 0;
                        process.completionTime = time;
                    } else {
                        time += quantum;
                        executionOrder.add(process.name + "(" + quantum + ")");
                        process.remainingTime -= quantum;
                    }
                }
            }

            if (currentUser.stream().anyMatch(p -> p.remainingTime > 0)) {
                userQueue.add(currentUser);
            }
        }

        System.out.println("\nEquity Scheduling:");
        System.out.println("Execution Order: " + String.join(" -> ", executionOrder));
    }
}