import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread{
    int w;
    nBody b;
    CyclicBarrier barrier;


    //Thread class
    public Worker(int w, nBody b, CyclicBarrier barrier){
        this.w = w;
        this.b = b;
        this.barrier = barrier;

    }

    @Override
    public void run() {
        //for time measurement

        long start = System.nanoTime();
        for(int i = 0; i < b.numSteps;i++) {
            b.calculateForce(w);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            b.moveBodies(w);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }
        if(w==0){
            //b.broadcast();
            System.out.println((System.nanoTime()-start)/1e9);
        }

    }
}

