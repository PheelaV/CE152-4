package assignment;

import lab9.DoubleParser;
import testPreparaiton.Ball;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args){


        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\earth.xyz";
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\tesatData.xyz";
//        String fileName = "C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\Assignment\\CE152-4.xyz";

        var earth = new Earth();

        earth.readDataArray(fileName);
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
        var f = new JFrame();
        var renderer = new EarthRenderer(earth);
        f.getContentPane().setPreferredSize(
                new Dimension(renderer.getWidth(), renderer.getHeight()));
        f.add(renderer);
        f.setResizable(false);
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e);
            }
        });

var a = 2;
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
