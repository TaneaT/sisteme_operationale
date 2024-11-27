package org.example;

public class Main {
    public static void main(String[] args) {

        String keyName = "MyJavaApp";
        String appPath = System.getProperty("C:\\Users\\User\\IdeaProjects\\Projects\\sisteme_operationale\\so_lab5");

        AutoStartup autoStartup = new AutoStartup();

      autoStartup.addToRegistry(keyName, appPath);

      autoStartup.restartInMinutes(5);
      autoStartup.displayLocalTime(4);


        // autoStartup.removeFromRegistry(keyName);



    }
}