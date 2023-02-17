import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

//Hungry Birds
public class HungryBirds {
    private ArrayList<Integer> dish = new ArrayList<>();
    private Thread[] bbirds;

    private Semaphore empty = new Semaphore(1);

    private Semaphore full = new Semaphore(0);

    private Thread pbird;

    private int W;

    public HungryBirds(int birds, int worms) {
        this.bbirds = new Thread[birds];
        this.pbird = new Thread(new parentBird());
        this.W = worms;
    }
//Main
    public static void main(String[] args) throws IOException {

        int[] arr = cInput();
        HungryBirds object = new HungryBirds(arr[0], arr[1]);
        object.pbird.start();
        for (int i = 0; i < arr[0]; i++) {

            object.bbirds[i] = new Thread(object.new babyBird());
            object.bbirds[i].start();

        }


    }
    //consol input
    static int[] cInput() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Threads:");
        String threads = in.readLine();
        System.out.println("Worms");
        String strW = in.readLine();
        int birds = Integer.parseInt(threads);
        int W = Integer.parseInt(strW);
        in.close();
        int[] arr = {birds,W};
        return arr;

    }
    //fill function (fills the dish with worms)
    public void fill(){
        for(int i = 0;i<this.W ;i++){
            dish.add(1);
        }
        System.out.println(dish.size());
    }
    //babyBird
    class babyBird implements Runnable{

        @Override
        public void run() {
        while(true){

            try {
                full.acquire();
                System.out.println("size:"+dish.size());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dish.remove(0);
            System.out.println("Worm consumed");
            if(dish.isEmpty()){
                System.out.println("try to awake mother");
                empty.release();

            }else {
                full.release();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        }
    }
    //parentBird
    class parentBird implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {
                    empty.acquire();
                    System.out.println("parent bird is now awake");
                    fill();
                    full.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}

