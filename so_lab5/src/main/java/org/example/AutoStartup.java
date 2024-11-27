package org.example;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AutoStartup {

    public void addToRegistry(String keyName, String appPath) {
        try {

            String command = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v "
                    + keyName + " /t REG_SZ /d \"javaw -jar " + appPath + "\" /f";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Aplicația a fost adăugată la pornirea automată.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Eroare la adăugarea în registru: " + e.getMessage());
        }
    }


    public void removeFromRegistry(String keyName) {
        try {

            String command = "reg delete HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v "
                    + keyName + " /f";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Cheia a fost ștearsă din registru.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Eroare la ștergerea din registru: " + e.getMessage());
        }
    }

    public void restartInMinutes(int minutes) {
        try {
          
            int seconds = minutes * 60;
            String command = "shutdown -r -t " + seconds;
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Calculatorul se va reporni în " + minutes + " minute.");
        } catch (IOException | InterruptedException e) {
            System.err.println("Eroare la repornirea calculatorului: " + e.getMessage());
        }
    }

    public void displayLocalTime(int minutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime restartTime = now.plusMinutes(minutes);

        System.out.println("Timp inițial: " + now.format(formatter));
        System.out.println("Calculatorul va fi repornit la: " + restartTime.format(formatter));

        try {
            Thread.sleep(minutes * 60 * 1000);
        } catch (InterruptedException e) {
            System.err.println("Așteptarea a fost întreruptă: " + e.getMessage());
        }
    }
}
