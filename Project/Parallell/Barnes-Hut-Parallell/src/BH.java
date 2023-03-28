import java.util.concurrent.CyclicBarrier;

public class BH {
    QuadNode root;
    double mass;
    int gnumBodies;
    int numSteps;
    int bodyPerRow;
    double far;
    double side;
    Body[] bodies;
    int DT = 1;


    int numWorkers;


    public BH(int gnumBodies, int numSteps, double mass, int bodyPerRow, Body[] bodies, double far, double side){
        this.gnumBodies = gnumBodies;
        this.numSteps = numSteps;
        this.mass = mass;
        this.bodyPerRow = bodyPerRow;
        this.far = far;
        this.side = side;
        this.bodies = bodies;

    }

    public void broadcast(){
        for(int i=0;i<gnumBodies;i++){
            bodies[i].broadCastPos();
        }
    }




}

