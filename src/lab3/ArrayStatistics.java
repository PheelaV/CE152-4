package lab3;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class ArrayStatistics implements Statistics {
    public double[] x;

    public ArrayStatistics(double[] x) {
        this.x = x;
    }

    @Override
    public double sum() {
        return this.getStream().sum();
    }

    @Override
    public double mean() {
        return this.sum() / x.length;
    }

    @Override
    public double max() {
        return this.getStream().max().orElseThrow(IllegalAccessError::new);
    }

    @Override
    public double min() {
        return this.getStream().min().orElseThrow(IllegalAccessError::new);
    }

    private DoubleStream getStream(){
        return Arrays.stream(x);
    }
}
