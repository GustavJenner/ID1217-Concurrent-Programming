import java.util.ArrayList;
import java.util.Random;

public class Bridge {
    //true = north, false = south
    private boolean dir;
    private int count;

    private long start;

    public Bridge() {
        this.count = 0;
        this.start = System.currentTimeMillis();


    }
    //synchronized method for wanting to cross the bridge
    //if cars in opposite direction are currently crossing then the thread will wait
    public synchronized void crossBridge(Boolean dir) throws InterruptedException {
        while (count > 0 && this.dir != dir) {
            this.wait();
        }
        addCar();
        this.notify();
        this.dir = dir;

    }

    //synchronized method for leaving bridge
    public synchronized void overBridge(int id, String dir) throws InterruptedException {
        System.out.println("Car" + id + " crossed bridge " + "at the time " + getTimestamp() + "towards " + dir);
        removeCar();


    }
    //new car is at the bridge
    public void addCar() {

        this.count++;
    }
    //car is leaving bridge
    public void removeCar() {
        this.count--;
    }

    //gets timestamp in seconds
    public long getTimestamp() {
        return (System.currentTimeMillis() - this.start)/1000;
    }
}
