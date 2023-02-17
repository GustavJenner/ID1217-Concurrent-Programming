import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
//Bear and Bees class
public class BearnBees {
    private int H;
    private ArrayList<Integer> honeypot = new ArrayList<>();
    private Semaphore full = new Semaphore(0);
    private Semaphore empty = new Semaphore(1);
    private Thread[] honeybees;
    //constructor
    public BearnBees(int n,int H){
        this.honeybees = new Thread[n];
        this.H = H;
    }
    //consol input
    static int[] cInput() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Threads:");
        String threads = in.readLine();
        System.out.println("Honey:");
        String strW = in.readLine();
        int honeybees = Integer.parseInt(threads);
        int H = Integer.parseInt(strW);
        in.close();
        int[] arr = {honeybees,H};
        return arr;
    }
    //main
    public static void main(String[] args) throws IOException{
        int[] arr = cInput();
        BearnBees object = new BearnBees(arr[0],arr[1]);
        for (int i = 0; i < arr[0]; i++) {
            object.honeybees[i] = new Thread(object.new HoneyBee());
            object.honeybees[i].start();
        }
        Thread c = new Thread(object.new Bear());
        c.start();


    }
    //produce function
    public void produce(){
        honeypot.add(1);
    }
    //consume function
    public void consume(){
        honeypot.clear();
    }

    public class HoneyBee implements Runnable{
        @Override
        public void run() {
           while(true){

               try {
                   empty.acquire();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               if(honeypot.size()<H){
                   System.out.println("Bee produces honey");
                   produce();
                   empty.release();
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }

               }else{
                   System.out.println("Honeypot full");
                   full.release();
               }
           }




        }

    }
    //Bear class
    public class Bear implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    full.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Bear eating");
                consume();
                System.out.println("Honeypot empty");
                empty.release();
            }

        }
    }

}
