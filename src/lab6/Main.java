package lab6;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        EEG eeg = new EEG();

        try {
            eeg.readData("C:\\Users\\filip\\OneDrive - University of Essex\\Documents\\School\\Object Oriented Programming-CE-152\\EEGData\\eegdata3.txt");
        } catch (FileNotFoundException e){
            System.out.println("The filePath is incorrect");
        } catch (Exception e) {
            System.out.println("The file format is incorrect");
        }
        System.out.println(String.format("The avgData is %s", Arrays.toString(eeg.getAvgData())));
        System.out.println(String.format("The peak amplitude is %s", eeg.getPeakAmplitude()));
        System.out.println(String.format("The peak latency is %s", eeg.getPeakLatency()));
    }
}
