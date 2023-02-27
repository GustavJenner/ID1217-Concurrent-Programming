public class HungryBirds {
    private int W;
    private boolean flag = true;
    public synchronized void consume() throws InterruptedException {

           System.out.println("worm consumed");
           wait();

    }
    public synchronized void produce() throws InterruptedException {


            System.out.println("dish filled");
            wait();

    }

    public static void main(String[] args) {
        HungryBirds h = new HungryBirds();
         h.new thread1().start();
         h.new thread2().start();


    }
    public class thread1 extends Thread{
        private HungryBirds h;


        @Override
        public void run() {
            try {
                consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public class thread2 extends Thread{
        @Override
        public void run() {
            try {
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
