package lab4;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        String[] learningGoals1 = new String[] {"inheritance", "abstraction", "encapsulation", "polymorphism"};
        String[] learningGoals2 = {"inheritance", "abstraction", "encapsulation", "polymorphism"};
        String[] learningGoals3 = new String[3];
        learningGoals3[0] = "inheritance";
        learningGoals3[1] = "abstraction";
        learningGoals3[2] = "encapsulation";
        learningGoals3[3] = "polymorphism";

        ArrayList<String> hashes = new ArrayList<>(Arrays.asList(
                "ebe4bab9796d8591e73101a9b2cf6296",
                "876e8108f87eb61877c6263228b67256",
                "738c8372fab9160336f3daad7fcc7e2a",
                "e746640a93970b89c3aa02a0e0119b72",
                "4c686eae1cc02cb974473fea2b93f0ba",
                "361334be6de412b0f57d7c745815a1b5"));

        Map<String, String> passwords = new HashMap<>();

        //First 0000-9999
//        for (int i = 0; i < 10000; i++){
//            passwords.put(Password.getMD5(String.format("%04d",i)), String.format("%04d",i));
//            String hash = Password.getMD5(password);
//            if (hashes.contains(hash)){
//                System.out.println("The password for{" + hash + "} is " + password);
//            }
//        }

        //Another00-zz
        List<Character> characters = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            characters.add(Character.forDigit(i, 10));
        }
        for (int i = 97; i < 123; i++){
            characters.add((char)i);
        }

        characters.forEach(c1 -> {
            characters.forEach(c2 -> {
//                System.out.println(c1.toString() + c2.toString());
//                passwords.put(Password.getMD5(c1.toString() + c2.toString()), c1.toString() + c2.toString());

                StringBuilder sb = new StringBuilder();
                sb.append(new Character[]{c1, c2});
                String password = sb.toString();
                String hash = Password.getMD5(password);
                if (hashes.contains(hash)){
                    System.out.println("The password for{" + hash + "} is " + password);
                }
            });
        });

        characters.forEach(c1 -> {
            characters.forEach(c2 -> {
                characters.forEach(c3 -> {
                    characters.forEach(c4 -> {
                        characters.forEach(c5 -> {
                            StringBuilder sb = new StringBuilder();
                            sb.append(new Character[]{c1, c2, c3, c4});
                            String password = sb.toString();
                            String hash = Password.getMD5(password);
                            if (hashes.contains(hash)){
                                System.out.println("The password for{" + hash + "} is " + password);
                            }
                        });
                    });
                });
            });
        });

        System.out.println(characters);

        hashes.forEach(h -> {
            if(passwords.containsKey(h)){
                System.out.println(passwords.get(h));
            }
        });
    }
}
