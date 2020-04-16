package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class TutorialFrame extends JFrame {

    public TutorialFrame(){
        new JFrame();
        this.setResizable(false);
        this.getContentPane().setPreferredSize(new Dimension(400,350));
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));


        this.add(new Label("CONTROLS:", Label.CENTER));
        for (var text: new String[]{
                "Leftclick          to select points",
                "Rightclick         to do what the spec says",
                ""
        }){
            this.add(new Label(text, Label.LEFT));
        }
        this.add(new Label("FUN BONUS:", Label.CENTER));
        for (var text: new String[]{
                "Scroll Up&Down     to affect the seaLevel",
                "Click&Drag         to pan around the map",
                "Press key \"o\"\t  to zoom Out",
                "Press key \"i\"\t  to zoom In",
                "Press key ESC\t    to reset the view",
                "",
        }){
            this.add(new Label(text, Label.LEFT));
        }
        this.add(new Label("Hope you enjoy!", Label.CENTER));
        var okButton = new Button("Ay ay captain");

        ///AAAA super lazy welcome to Java(Script)
        var self = this;
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                self.dispatchEvent(new WindowEvent(self, WindowEvent.WINDOW_CLOSING));
            }
        });
        this.add(okButton);
        this.pack();
        this.setTitle("IMORTANT - HOW TO");
        this.setVisible(true);
        this.repaint();
    }
}
