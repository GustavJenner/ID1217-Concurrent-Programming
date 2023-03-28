
import java.util.Random;

public class Cars extends Thread {
    private Bridge bridge;
    private boolean dir;
    private int id;



    private Random rand = new Random();

    private int trips;
    public Cars(boolean dir, int id, int trips, Bridge bridge){
        this.dir = dir;
        this.id = id;
        this.trips = trips;
        this.bridge = bridge;

    }
    public String getDir(){
        if(dir){
            return "North";
        }
        else{
            return "South";
        }
    }
    public int getCarID(){
        return this.id;
    }
    public void completedTrip(){
        this.trips--;
        this.dir = !this.dir;
    }



    @Override
    public void run() {
        while(this.trips != 0){

            System.out.println("Car"+getCarID()+" waiting to cross bridge at "+ bridge.getTimestamp() +" towards "+getDir());
            try {
                bridge.crossBridge(dir);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                    System.out.println("Car"+getCarID()+" started crossing bridge at "+ bridge.getTimestamp()+" towards "+getDir());
                    Thread.sleep((rand.nextInt(5)+1)*2000);
                    bridge.overBridge(this.id,getDir());
                    completedTrip();
                    Thread.sleep((rand.nextInt(5)+1)*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }




        }
    }
}
