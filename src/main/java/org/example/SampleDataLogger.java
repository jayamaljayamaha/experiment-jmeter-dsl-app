package org.example;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.SimpleDataWriter;

import javax.swing.event.ChangeEvent;
import java.util.concurrent.atomic.AtomicLong;

public class SampleDataLogger extends SimpleDataWriter {

    private AtomicLong sum;

    public SampleDataLogger(){
        super();
        this.sum = new AtomicLong(0);
    }

    @Override
    public void clearData() {
        System.out.println("clear data");
        // NOOP
    }

    @Override
    public synchronized void add(SampleResult sample) {
//        System.out.println("Results came");
//        System.out.println("Latency: "+sample.getLatency());
        System.out.println("Full Time: " +sample.getTime());
//        System.out.println("Process Time: "+ (sample.getTime()- sample.getLatency()));
        this.sum.set(this.sum.get() + (sample.getTime()));
        System.out.println("sum: "+ this.sum.get());
    }

    @Override
    public void clearGui(){
        System.out.println("here in clear gui");
        super.clearGui();
    }
}
