package org.example;

import java.util.*;

class Process {
    String name;
    int burstTime;
    int remainingTime;
    int completionTime;
    int priority;

    public Process(String name, int burstTime) {
        this.name = name;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
    }

    public Process(String name, int burstTime, int priority) {
        this.name = name;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.priority = priority;
    }
}




