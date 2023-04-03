import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    int w;
    Body[] bodies;
    CyclicBarrier barrier;

    int numSteps;

    double far;
    double side;

    int numWorkers;

    int gnumbodies;

    //Thread class
    public Worker(int w, Body[] bodies,int gnumbodies,CyclicBarrier barrier,int numSteps,double far, double side, int numWorkers) {
        this.w = w;
        this.bodies = bodies;
        this.barrier = barrier;
        this.numSteps = numSteps;
        this.far = far;
        this.side = side;
        this.numWorkers = numWorkers;
        this.gnumbodies = gnumbodies;

    }

    @Override
    public void run() {

        long start = System.nanoTime();
        QuadNode root;
        for(int j = 0; j < numSteps;j++){
            root = new QuadNode(new Point(0,0), side,far);
            for (int i = 0; i < gnumbodies; i++) {
                root.insert(bodies[i]);


            }
            for (int i = w; i < gnumbodies; i += numWorkers) {
                root.calculateForce(bodies[i]);


            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
            for (int i = w; i < bodies.length; i += numWorkers) {
                bodies[i].movePoint(1);
                bodies[i].force.x = bodies[i].force.y = 0;
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

        }
        if(this.w == 0){
           //broadcast();
           System.out.println((System.nanoTime()-start)/1e9);
            }
        }
    public void broadcast(){
        for(int i=0;i< bodies.length;i++){
            bodies[i].broadCastPos();
        }


    }
}