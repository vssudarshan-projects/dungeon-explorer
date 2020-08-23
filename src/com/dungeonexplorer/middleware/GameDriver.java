package com.dungeonexplorer.middleware;

import java.util.List;
import java.util.Scanner;

public class GameDriver {

    private static Player player;
    private static final Scanner userInputScanner = new Scanner(System.in);

    public static void initialize(Player player) {
        GameDriver.player = player;
        helpMessage();
        while (!readUserCommand().equalsIgnoreCase("exit")) ;
    }

    private static String readUserCommand() {

        String userCommandString = userInputScanner.nextLine();

        if (!userCommandString.equals("")) {
            String[] commandString = userCommandString.split(" ");
            executeCommand(commandString);
        }
        return userCommandString.split(" ")[0];
    }


    private static void executeCommand(String[] commandString) {

        switch (commandString[0].toLowerCase()) {
            case "help":
                helpMessage();
                break;
            case "go":
                move(commandString);
                break;
            case "look":
                look(commandString);
                break;
            case "search":
                search(commandString);
                break;
            case "pick":
                pickUp(commandString);
                break;
            case "equip":
                equip(commandString);
                break;
            case "unequip":
                unequip(commandString);
                break;
            case "attack":
                attack(commandString);
                break;
            case "clear":
                for (int i = 0; i < 50; i++)
                    System.out.println();
                break;
            case "show":
                player.getStage().showAllZoneObjects(player.getZoneId());
                break;
            case "exit":
                message("You attempt to flee the realms but your soul is crushed.");
                System.exit(0);
                break;
            default:
                message("You feel like you have heard those words before, but don't remember what they mean...");
        }
        System.gc();
    }

    private static void move(String[] commandString) {

        int zoneId = player.getZoneId();
        if (player.move(commandString)) {
            removeZoneObjects(zoneId);
            generateZoneObjects();
            moveMessage();
        }
        message(player.getZoneName() + " : x: " + player.getPosition().getX() + " y: " + player.getPosition().getY());
    }

    private static void look(String[] commandString) {
        List<String[]> strings = player.look(commandString);
        if (strings != null && strings.size() != 0) {
            message("You can see,");

            for (String[] stringArray : strings) {
                stringArray[0] = stringArray[0].concat(": ");
                stringArray[1] = stringArray[1].concat("\n");
            }

            message(strings);
        } else
            message("There's nothing to look at...");

    }

    private static void search(String[] commandString) {
        String[] strings = player.search(commandString);
        if (strings != null)
            message("Found: " + strings[0] + ": " + strings[1]);
        else
            message("Found nothing...");
    }

    private static void pickUp(String[] commandString) {
        int count = player.pickUp(commandString);

        if (count != 0) {
            GameDriver.message(count + " items added to inventory.");
        } else
            GameDriver.message("could not add to inventory.");
    }

    private static void equip(String[] commandString) {
        if (player.equip(commandString))
            message("Item equipped.");
        else
            message("Item could not be equipped.");
    }

    private static void unequip(String[] commandString) {
        if (player.unequip(commandString))
            message("Unequipped.");
        else
            message("Nothing to unequip.");
    }

    public static String readUserAction() {
        String userCommandString = userInputScanner.nextLine();
        return userCommandString.split(" ")[0];
    }

    private static void attack(String[] commandString) {
        int result = player.attack(commandString);
        if (result == -1) {
            message("Got away safely...");
        } else if(result == 0){
            message("Ugh! You have died and have been revived by the Goddess.");
        }else if(result == 1){
            message("You grew a little stronger.");
        }
    }

    private static void removeZoneObjects(int zoneId) {
        player.getStage().removeZoneObjects(zoneId);
    }

    private static void generateZoneObjects() {
        player.getStage().generateZoneObjects(player.getZoneId());
    }

    private static void helpMessage() {
        message("             ======Level " + player.getStage().getStageNumber() + "======\n" +
                "======= Welcome to " + player.getStage().getStageName() + "=======");
        message("           ====================\n             Available Actions\n           ====================\n" +
                "1. help => view command list\n" +
                "2. go => go west|go w\n" +
                "3. look => look north | look n | look inventory | look inv \n" +
                "4. search => search|search monster|search treasure\n" +
                "5. pick => pick| pick 'item name' (type command without single quotes('))\n" +
                "6. equip => equip 'item name' (type command without single quotes('))\n" +
                "7. unequip => unequip armour | unequip weapon | unequip scroll1 | unequip \n" +
                "8. attack => attack 'monster name' (type command without single quotes('))\n" +
                "9. flee => usable only during combat\n" +
                "10. exit => quit game\n");
        //clear is not listed
        //show all zoneObjects is not listed
    }

    private static void moveMessage() {
        message("You have entered " +
                player.getZoneName()
                + "\n" + player.getZoneDescription());
    }

    public static void message(String message) {
        System.out.println(message);
    }

    public static void message(String[] messageArray) {
        for (String message : messageArray)
            System.out.println(message);
    }

    public static void stringMessage(List<String> stringlist) {
        for (String string : stringlist)
            System.out.println(string);
    }

    public static void message(List<String[]> stringList) {
        for (String[] stringArray : stringList) {
            for (String message : stringArray)
                System.out.print("\n" + message);

            System.out.println();
        }
    }


}
