import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker extends Thread {
    int w;
    Body[] bodies;
    CyclicBarrier barrier;

    int numSteps;

    double far;
    double side;



    //Thread class
    public Worker(int w, Body[] bodies, CyclicBarrier barrier,int numSteps,double far, double side) {
        this.w = w;
        this.bodies = bodies;
        this.barrier = barrier;
        this.numSteps = numSteps;
        this.far = far;
        this.side = side;

    }

    @Override
    public void run() {
       QuadNode root;
        for(int j = 0; j < numSteps;j++){
            root = new QuadNode(new Point(0,0), side,far);
            for (int i = 0; i < bodies.length; i++) {
                root.insert(bodies[i]);


            }
            for (int i = w; i < bodies.length; i += barrier.getParties()) {

                root.calculateForce(bodies[i]);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                bodies[i].movePoint(1);
                bodies[i].force.x = bodies[i].force.y = 0.0;
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        if(this.w == 0){
           broadcast();
            }
        }
    public void broadcast(){
        for(int i=0;i< bodies.length;i++){
            bodies[i].broadCastPos();
        }


    }
}