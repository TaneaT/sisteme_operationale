package org.example;

public class Main {
    public static void main(String[] args) {

         String keyName = "GoogleChromeAutoLaunch";
         String chromePath = "\"C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe\"";

AutoStartup autoStartup = new AutoStartup();

//       autoStartup.addAppToStartup(keyName,chromePath);


        autoStartup.disableAppFromStartup(keyName);

    }
}