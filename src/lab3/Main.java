package lab3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
//        ArrayStatistics myArray = new ArrayStatistics(new double[] { 1.0, -2.0, 3.0, -4.0, 5.0 });
//        System.out.println("Array = " + myArray);
//        System.out.println("Sum = " + myArray.sum());
//        System.out.println("Average = " + myArray.mean());
//        System.out.println("Min = " + myArray.min());
//        System.out.println("Max = " + myArray.max());

        int[] integers = {1,2,3,4,5,6};

        System.out.println(Arrays.toString(integers));
        System.out.println(Arrays.toString(reversedCopy(integers)));

        System.out.println(Arrays.toString(integers));
        reversed(integers);
        System.out.println(Arrays.toString(integers));
        int number = 0;
        System.out.println(copyInt(number));
        changeInt(number);
        System.out.println(number);

        System.out.println(factors(663116866759L));
        System.out.println(factors(9223372036854775807L));
        System.out.println(Long.MAX_VALUE);
    }

    public static void factors(long[] x){
        for(long number : x){
            System.out.println("The factors of are: ");

            System.out.print(factors(number));
        }
    }

    private static String factors(long number) {
        ArrayList<Long> factors = new ArrayList();

        for (long i = 2; i <= number; i++){
            if(number % i == 0){
                factors.add(i);
                number /= i;
                i = 1;
            }
        }

        return Arrays.toString(factors.toArray());
    }

    public static int copyInt(int number){
        int a = number;

        return a;
    }

    public static void changeInt(int number)
    {
        number +=1;
    }

    public static void reversed(int[] data){

        for (int i = 0; i < data.length/2; i++){
            int tempInt = data[i];
            data[i] = data[data.length - 1 - i];
            data[data.length - 1 - i] = tempInt;
        }
    }

    public static int[] reversedCopy(int[] data){
        int[] result = new int[data.length];

        for (int i = 0; i < data.length; i++){
            result[i] = data[data.length - 1 - i];
        }

        return result;
    }
}

