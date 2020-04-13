package nadilka;

import javax.swing.*;

//transformation of user coordinates to screen coordinates
public class Plot extends JComponent {
    //dimensions of plotting area with default values
    int width = 600, height = 600;

    //dimensions of user-space coordinates with default values
    double xmin=0, xmax=1, ymin=0, ymax = 1;

    //transformation of coordinates
    public int scaleX(double x){
        return (int) (width*(x-xmin)/(xmax - xmin));
    }

    public int scaleY (double y){
        return (int) (height*(ymin-y)/(ymax - ymin) + height);
    }

    public void setScaleX(double min, double max){
        xmin = min; xmax = max;
    }

    public void setScaleY(double min, double max){
        ymin = min; ymax = max;
    }

    public void setWidth(int width){
        this.width = width;
        this.height = (int) (this.width*(ymax - ymin)/(xmax - xmin));
    }
}
