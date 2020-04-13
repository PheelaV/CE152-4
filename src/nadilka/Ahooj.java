package nadilka;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;


public class Ahooj extends Plot {
    public static double x, y;
    public double[] vrcholyX, vrcholyY;
    public static Path2D path;
    public static int[] pismenaX,pismenaY;
    public String answer = "answer";
    public static final int POCET_VRCHOLU =15; //zvol si pocet vrcholu


    public void computeVrcholy() {
        vrcholyX = new double[2*POCET_VRCHOLU+1];
        vrcholyY = new double[2*POCET_VRCHOLU+1];
        int numOfV = 0;
        double rozestup = 360.0/(2*POCET_VRCHOLU);
        double i =0;
        while(i<=360.00000001){     //kvuli omezeni presnosti Javy vychazi spatny vysledek pro vyssi POCET_VRCHOLU pokud je podminka (i<=360)
            double r = Math.toRadians(i);
            if(numOfV%2 == 0){
                vrcholyX[numOfV] = Math.cos(r);
                vrcholyY[numOfV] = Math.sin(r);
            }else{
                vrcholyX[numOfV] = 1.7*Math.cos(r);
                vrcholyY[numOfV] = 1.7*Math.sin(r);
            }
            numOfV++;
            i += rozestup;
        }
    }

    public void drawKrouzek(JFrame f) throws InterruptedException {
        setScaleX(-2, 2);
        setScaleY(-2, 2);
        x = scaleX(1);
        y = scaleY(0);
        path = new Path2D.Double();
        path.moveTo(x, y);
        for (int i = 0; i <= 360; i++) {
            double r = Math.toRadians(i);
            x = Math.cos(r);
            y = Math.sin(r);
            path.lineTo(scaleX(x), scaleY(y));
            f.repaint();
            Thread.sleep(5);
        }
    }

    public void drawPaprsky(JFrame f) throws InterruptedException {
        computeVrcholy();
        for (int i = 1; i < 2 * POCET_VRCHOLU + 1; i++) {
            double x1 = vrcholyX[i - 1];
            double y1 = vrcholyY[i - 1];
            double x2 = vrcholyX[i];
            double y2 = vrcholyY[i];
            double k = (y2 - y1) / (x2 - x1); //smernice hrany paprsku
            double j = x1;
            while (Math.abs(j - x1) < Math.abs(x2 - x1)) { //dokud se z x1 nedostanu na x2
                x = scaleX(j);
                y = scaleY(k * (j - x1) + y1);
                path.lineTo(x, y);
                Thread.sleep(10);
                f.repaint();
                if (x2 > x1)
                    j += Math.abs(x2 - x1) / 10;
                else
                    j -= Math.abs(x2 - x1) / 10;
            }
        }
    }

    public void psani(JFrame f) throws InterruptedException {
        setScaleX(-24,66);
        setScaleY(-17,49);
        pismenaX = new int[]{0,6,6,4,8,0,8,10,12,11,9,0,14,14,14,16,14,16,0,18,20,22,20,20,0,30,30,28,32,0,36,34,34,36,34,34,36,0,34,35,36,0,12,12,14,16,16,0,18,20,22,21,19,0,21,22,0,24,24,26,28,28,0,12,12,14,14,12,14,0,16,18,20,19,17,0,19,20,0,22,22,24,24,22,0,26,28,30,29,27};
        pismenaY = new int[]{0,18,22,22,22,0,18,22,18,20,20,0,22,18,20,22,20,18,0,22,20,22,20,18,0,18,22,22,22,0,18,18,20,20,20,22,22,0,24,23,24,0,12,16,14,16,12,0,12,16,12,14,14,0,17,18,0,12,16,14,16,12,0,6,10,10,8,8,6,0,6,10,6,8,8,0,11,12,0,6,10,10,6,6,0,6,10,6,8,8};
        for (int i = 0; i < pismenaX.length; i++) {
            if (pismenaX[i] == 0) {
                path.moveTo(scaleX(pismenaX[i + 1]), scaleY(pismenaY[i + 1]));
            } else {
                path.lineTo(scaleX(pismenaX[i]), scaleY(pismenaY[i]));
            }
            Thread.sleep(70);
            f.repaint();
        }
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(5));
        g.setColor(Color.ORANGE);
        g.draw(path);
    }



    public static void main(String[] args) throws InterruptedException {
        JFrame f = new JFrame("Kralicek");
        f.getContentPane().setPreferredSize(new Dimension(600, 600));
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.pack();
        Ahooj cau = new Ahooj();

        JPanel heslo = new JPanel();
        heslo.setLayout(new FlowLayout());
        JTextArea text = new JTextArea();
        text.setColumns(16);
        JLabel jl = new JLabel();
        JButton jb = new JButton("Poslat");
        jb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(text.getText().equals("THEMANINTHEARENA"))
                    synchronized (f){
                        f.notifyAll();
                    }
                else
                    jl.setText("Zkus to znovu. Heslo napiš velkým písmem a bez mezer.");
            }
        });


        heslo.add(text);
        heslo.add(jb);
        heslo.add(jl);
        f.add(heslo);


//        synchronized (f) {
//            f.wait();
//        }


        heslo.setVisible(false);
        f.repaint();
        f.add(cau);
        try {
            cau.drawKrouzek(f);
            cau.drawPaprsky(f);
            cau.psani(f);
        }catch(InterruptedException e){
            System.out.println("Mistake: " + e.getMessage());
        }
    }
}
