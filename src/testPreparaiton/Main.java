package testPreparaiton;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static int threads = 12;

    public interface IPerson{
        void getMoney(int money);
    }

    public abstract class Person implements  IPerson{

        @Override
        public void getMoney(int money) {

        }
    }

    public class Teacher extends Person {
        @Override
        public void getMoney(int money) {
            super.getMoney(money);
        }
    }


    public static double getWholeNumberLeftOver(double value){
        var d = String.valueOf(value);
        var part = d.split("\\.");

        var decimalPart = Double.parseDouble("0." + part[1]);

        return decimalPart;
    }

    public static void main(String[] args) throws InterruptedException {
//        // What does the parameter do?
//        JFrame f = new JFrame("Drawing 1");
//        // What do these methods do?
//        f.setSize(600, 600);
//
////        f.add( new Drawing1());
//        f.add(new Drawing2());
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);

//        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
//
//        BouncingBalls animation = new BouncingBalls();
//        JFrame f = new JFrame("Bouncing Ball Animation");
//        f.getContentPane().setPreferredSize(
//                new Dimension(Ball.BOX_WIDTH + 2 * animation.BOX_MARGIN, Ball.BOX_HEIGHT + 2 * animation.BOX_MARGIN));
//        f.add(animation);
//        f.setResizable(false);
//        f.pack();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);
//
//        int batchCount = animation.ballArray.length / threads;
//
//        while (true) {
//            Thread.sleep(animation.REPAINT_INTERVAL);
//
//            for (int t = 0; t < threads; t++){
//                int finalT = t;
//                executor.execute(() -> {
//
//                    for (int i = finalT * batchCount; i < finalT * batchCount + batchCount; i ++){
//                       Main.MoveBall(animation.ballArray[i]); ;
//                    }
//                });
//            }
//
//            f.repaint();
//        }
        System.out.println(Double.parseDouble("0"));



    }

    public static void MoveBall(Ball ball){
        ball.x += ball.vx;
        ball.y += ball.vy;
        if (ball.x < ball.radius || ball.x + ball.radius > Ball.BOX_WIDTH)
            ball.vx *= -1;
        if (ball.y < ball.radius || ball.y + ball.radius > ball.BOX_HEIGHT)
            ball.vy *= -1;
    }
}
