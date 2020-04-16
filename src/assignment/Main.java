package assignment;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){


        //java.lang.OutOfMemoryError: Java heap space
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\earthHD.xyz";

        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\earth.xyz";
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\tesatData.xyz";
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\CE152-4.xyz";

        var earth = new Earth();

        //DEMONSTRATION
//        earth.readDataArray(fileName);
        earth.readDataMap(fileName);
//        earth.generateMap(1);

        var breakInput = false;
        var input = new Scanner(System.in);

//        for(;;){
//            System.out.println("Please enter an altitude:");
//            String argument = input.nextLine();
//
//            if (isValidSingleArgument(argument)){
//                double altitude = Double.parseDouble(argument);
//
//                try {
//                    earth.percentageAbove(altitude);
//                } catch (Earth.DataNotLoadedException e){
//                    System.out.println(String.format("Data not loaded: %s", e.getMessage()));
//                }
//
//            } else if(isQuitArgument(argument)){
//                break;
//            }
//        }

//        for(;;){
//            System.out.println("Please enter a longitude (0-360) and latitude (-90-90):");
//            String argument = input.nextLine();
//
//            if (isValidDoubleArgument(argument)){
//                double altitude = Double.parseDouble(argument);
//
//                try {
//                    earth.percentageAbove(altitude);
//                } catch (Earth.DataNotLoadedException e){
//                    System.out.println(String.format("Data not loaded: %s", e.getMessage()));
//                }
//
//            } else if(isQuitArgument(argument)){
//                break;
//            }
//        }
///ENDDEMONSTRATION

        var earthFrame = new EarthFrame(earth);

        var tutorialFrame = new TutorialFrame();

//        Please enter a longitude (0-360) and latitude (-90-90):
//        30 45
//        The altitude at longitude 30.0 and latitude 45.0 is -37.0 meters.
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
            var values = Arrays.stream(argument.split(" ")).mapToDouble((x -> Double.parseDouble(x))).toArray();

            return true;
        } catch (Exception e){
            return false;
        }
    }

    private static boolean isQuitArgument(String argument) {
        return argument.equals(argument);
    }
}

//        __
//   hi  c(..)o    (
//     \__(-)     __)
//        /\     (
//       /(_)___)
//      w /|
//        | \
//        m  m
