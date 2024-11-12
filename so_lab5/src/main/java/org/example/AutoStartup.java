package org.example;

import java.io.IOException;

public class AutoStartup {

    public void addAppToStartup(String keyName, String chromePath) {
        try {
            String command = "reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v " + keyName + " /t REG_SZ /d " + chromePath + " /f";
            Runtime.getRuntime().exec(command);
            System.out.println("Google Chrome adaugat la pornirea automata.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disableAppFromStartup(String keyName) {
        try {
            String command = "reg delete HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v " + keyName + " /f";
            Runtime.getRuntime().exec(command);
            System.out.println("Google Chrome eliminat de la pornirea automata.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
