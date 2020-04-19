package assignment;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){


        //java.lang.OutOfMemoryError: Java heap space
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\earthHD.xyz";
        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\earth.xyz";
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\CE152-4.xyz";

        var earth = new Earth();
        //necessary for the data visualisation

        System.out.println("Loading...");
        earth.readDataMap(fileName);
//        earth.generateMap(1);
        //necessary for the assignment systemInDemonstration
        earth.readDataArray(fileName);
        System.out.println("Done...");
        readingFromSystemIn(earth);

        var earthFrame = new EarthFrame(earth);
        if(args.length != 0 && args[0].equals("--initialSeaLevel")){
            int addedMainlandAltitude;

            try {
                //Inverted, because the seaLevel function works as an offset with respect to the real sea level
                // example: sea rose by 300 meters = mainland sunk by 300 meters
                addedMainlandAltitude = - Integer.parseInt(args[1]);
                earthFrame.setInitialSeaLevel(addedMainlandAltitude);
            } catch (Exception e){
                System.out.println("Invalid startup arguments (initialSeaLevel<INT>), continuing...");
            }
        }

        var tutorialFrame = new TutorialFrame();
    }

    private static void readingFromSystemIn(Earth earth){
        var input = new Scanner(System.in);
        var promptOptions = "Choose:\n\"1\"\t\tAltitude stats\n\"2\"\t\tCoordinate stats\n\"quit\"\t to quit the prompting and continue to visualisation\n: ";
        var promptInputAltitude = "Please enter an altitude or \"quit\" to end program\n: ";
        var promptInvalidInput = "Invalid input.";

        //SOURCE: https://docs.oracle.com/javase/8/docs/api/java/util/Formatter.html
        //SOURCE: https://docs.oracle.com/javase/tutorial/java/data/numberformat.html
        var formatPercentageAbove = "Proportion of coordinates above %1$.1f meters: %2$.1f%%";
        var formatPercentageBellow = "Proportion of coordinates bellow %1$.1f meters: %2$.1f%%";

        var formatDataNotLoaded = "Data not loaded: %s";
        var promptInputLongLat = "Please enter a longitude (0-360) and latitude (-90-90) separated by space or \"quit\" to end program\n: ";
        var formatCoordinateStats = "longitude=%.2f°\nlatitude=%.2f°\ntrueAltitude=%.2fm";
        String userEntry;

        while(true){
            userEntry = promptUser(promptOptions, 0, input);
            if(userEntry.equals("1")) { // Altitude stats
                while(true){
                    userEntry = promptUser(promptInputAltitude, 1, input);

                    if(isQuitArgument(userEntry)){
                        break;
                    }  else if (!isValidSingleArgument(userEntry)){
                        levelPrintln(2,promptInvalidInput);
                        continue;
                    }

                    try {
                        double altitude = Double.parseDouble(userEntry);

                        var percentageAbove = earth.percentageAbove(altitude);
                        var percentageBellow = earth.percentageBellow(altitude);

                        levelPrintln(2, String.format(formatPercentageAbove, altitude, percentageAbove));
                        levelPrintln(2, String.format(formatPercentageBellow, altitude, percentageBellow));
                    } catch (Earth.DataNotLoadedException e){
                        levelPrintln(2, String.format(formatDataNotLoaded, e.getMessage()));
                    } catch (Exception e){
                        levelPrintln(2, promptInvalidInput);
                    }
                }
            } else if (userEntry.equals("2")){ // Coordinate stats
                while(true){
                    userEntry = promptUser(promptInputLongLat, 1, input);

                    if(isQuitArgument(userEntry)){
                        break;
                    } else if (!isValidDoubleArgument(userEntry)){
                        levelPrintln(2, promptInvalidInput);
                        continue;
                    }

                    var coordinates = getMapCoordinates(userEntry);

                    try {
                        var altitude = earth.getAltitude(coordinates[0], coordinates[1]);

                        levelPrintln(2, String.format(formatCoordinateStats, coordinates[0], coordinates[1], altitude));
                    } catch (NoSuchElementException e){
                        levelPrintln(2, String.format(formatDataNotLoaded, e.getMessage()));
                    }
                }
            } else if (isQuitArgument(userEntry)){
                return;
            } else {
                levelPrintln(1, "ERROR: Invalid argument");
            }
        }
    }

    private static boolean isValidSingleArgument(String argument) {
        try {
            Double.parseDouble(argument);

            return true;
        } catch (Exception e){
            return false;
        }
    }

    private static boolean isValidDoubleArgument(String argument) {
        try {
            getMapCoordinates(argument);
        } catch (Exception e){
            return false;
        }

        return true;
    }

    private static double[] getMapCoordinates(String entry){
        return Arrays.stream(entry.split(" ")).mapToDouble((x -> Double.parseDouble(x))).toArray();
    }

    private static boolean isQuitArgument(String argument) {
        return "quit".equals(argument);
    }

//        __
//   hi  c(..)o    (
//     \__(-)     __)
//        /\     (
//       /(_)___)
//      w /|
//        | \
//        m  m

    private static String promptUser(String message, int level, Scanner input){
        levelPrint(level, message);
        return input.nextLine();
    }

    private static void levelPrintln(int level, String text){
        var inputArray = text.split("\n");
        for (var line: inputArray){
            System.out.println("\t".repeat(level) + line);
        }
    }
    private static void levelPrint(int level, String text){
        var inputArray = text.split("\n");
        for (var i = 0; i < inputArray.length - 1; i ++){
            System.out.println("\t".repeat(level) + inputArray[i]);
        }

        System.out.print("\t".repeat(level) + inputArray[inputArray.length - 1]);
    }
}
