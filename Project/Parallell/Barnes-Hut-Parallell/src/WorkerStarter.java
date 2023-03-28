import java.util.concurrent.CyclicBarrier;

public class WorkerStarter {








    public static Body[] initBodies(int gnumBodies, int bodyPerRow, double mass){
        Body[] bodies = new Body[gnumBodies];
        int j = -1;
        double x;
        double y = 0;
        for(int i = 0; i<gnumBodies;i++){
            j++;
            x = i%bodyPerRow;
            if(j == (bodyPerRow-1)){
                j = 0;
                y++;
            }
            bodies[i] = new Body(new Point(x,y),new Point(0,0),new Point(0,0),mass,i);
            //bodies[i].broadCastPos();

        }
        return bodies;

    }
    public static void main(String[] args) {
       int gnumBodies = Integer.valueOf(args[0]);
       int numSteps = Integer.valueOf(args[1]);
       double mass = Double.valueOf(args[2]);
       int bodyPerRow = Integer.valueOf(args[3]);
       int numWorkers = Integer.valueOf(args[4]);
       double far = Double.valueOf(args[5]);
       double side = Double.valueOf(args[6]);
       Body[] bodies = initBodies(gnumBodies,bodyPerRow,mass);
      CyclicBarrier barrier = new CyclicBarrier(numWorkers);

        for(int i = 0; i < numWorkers;i++){
            new Worker(i,bodies,barrier,numSteps,far,side).start();
        }


    }
}
