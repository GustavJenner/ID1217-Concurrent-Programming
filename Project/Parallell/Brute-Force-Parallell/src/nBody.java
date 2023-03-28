import java.util.concurrent.CyclicBarrier;

public class nBody {
    CyclicBarrier barrier;
    Body[] bodies;
    double G = 6.67e-11;
    double mass;
    int gnumBodies;
    int numSteps;
    int bodyPerRow;

    int DT = 1;
    int numWorkers;

    public nBody(int gnumBodies, int numSteps, double mass, int bodyPerRow, int numWorkers){
        this.gnumBodies = gnumBodies;
        this.numSteps = numSteps;
        this.mass = mass;
        this.bodyPerRow = bodyPerRow;
        this.bodies = new Body[gnumBodies];
        this.numWorkers = numWorkers;
        this.barrier = new CyclicBarrier(numWorkers);

    }
    public void initBodies(){
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
            bodies[i] = new Body(new Point(x,y),new Point(0,0),numWorkers,mass);
            //bodies[i].broadCastPos(i);

        }
    }
    public void calculateForce(int w){
        double distance, magnitude;
        Point direction;
        for(int i = w; i < gnumBodies; i+=numWorkers){
            for(int j = i+1; j<gnumBodies;j++){
                Body a = bodies[i];
                Body b = bodies[j];
                distance = Math.sqrt(Math.pow((a.position.x-b.position.x),2)+Math.pow((a.position.y-b.position.y),2));
                magnitude = (G * a.mass * b.mass)/(Math.pow(distance,2));
                direction = new Point(b.position.x-a.position.x,b.position.y-a.position.y);
                a.force[w].x = a.force[w].x + magnitude*direction.x/distance;
                b.force[w].x = b.force[w].x - magnitude*direction.x/distance;
                a.force[w].y = a.force[w].y + magnitude*direction.y/distance;
                b.force[w].y = b.force[w].y - magnitude*direction.y/distance;

            }
        }
    }
    public void moveBodies(int w){
        Point deltav;
        Point deltap;
        Point force = new Point(0.0,0.0);
        for(int i = w; i < gnumBodies; i+=numWorkers){
            //sum the forces on body i and reset
            for(int k = 0; k<numWorkers;k++){
                force.x += bodies[i].force[k].x; bodies[i].force[k].x = 0.0;
                force.y += bodies[i].force[k].y; bodies[i].force[k].y = 0.0;
            }
            deltav = new Point((force.x/bodies[i].mass)*DT,(force.y/bodies[i].mass)*DT);
            deltap = new Point((bodies[i].velocity.x+deltav.x/2)*DT,(bodies[i].velocity.y+deltav.y/2)*DT);

            bodies[i].velocity.x = bodies[i].velocity.x + deltav.x;
            bodies[i].velocity.y = bodies[i].velocity.y + deltav.y;
            bodies[i].position.x = bodies[i].position.x + deltap.x;
            bodies[i].position.y = bodies[i].position.y + deltap.y;
            force.x = force.y = 0.0;
        }
    }
    public void broadcast(){
        for(int i=0;i<gnumBodies;i++){
            bodies[i].broadCastPos(i);
        }
    }

    public static void main(String[] args) {
        nBody b = new nBody(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Double.valueOf(args[2]),Integer.valueOf(args[3]),Integer.valueOf(args[4]));
        b.initBodies();
        for(int i=0;i< b.numWorkers;i++){
            new Worker(i,b,b.barrier).start();
        }
    }
}
