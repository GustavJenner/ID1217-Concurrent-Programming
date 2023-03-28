//main class
public class nBody {
    Body[] bodies;
    static double G = 6.67e-11;
    double mass;
    int gnumBodies;
    int numSteps;
    int bodyPerRow;

    int DT = 1;
    //Constructor
    public nBody(int gnumBodies, int numSteps, double mass, int bodyPerRow){
        this.gnumBodies = gnumBodies;
        this.numSteps = numSteps;
        this.mass = mass;
        this.bodyPerRow = bodyPerRow;
        this.bodies = new Body[gnumBodies];

    }
    //initilized bodies
    // example: if gnumbodies = 8 and bodyPerRow = 4
    //output . . . .
    //       . . . .
    //where each space has a length of 1 length unit
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
            bodies[i] = new Body(new Point(x,y),new Point(0,0),new Point(0,0),mass);
            //bodies[i].broadCastPos(i);

        }
    }
    //calculateForces method described in lecture 18
    public void calculateForces(){
        double distance, magnitude;
        Point direction;
        for(int i = 0; i < gnumBodies; i++){
            for(int j = i+1; j<gnumBodies;j++){
                Body a = bodies[i];
                Body b = bodies[j];
                distance = Math.sqrt(Math.pow((a.position.x-b.position.x),2)+Math.pow((a.position.y-b.position.y),2));
                magnitude = (G * a.mass * b.mass)/(Math.pow(distance,2));
                direction = new Point(b.position.x-a.position.x,b.position.y-a.position.y);
                a.force.x = a.force.x + magnitude*direction.x/distance;
                b.force.x = b.force.x - magnitude*direction.x/distance;
                a.force.y = a.force.y + magnitude*direction.y/distance;
                b.force.y = b.force.y - magnitude*direction.y/distance;

            }
        }
    }
    //moveBodies method described in lecture 18
    public void moveBodies(){
        Point deltav;
        Point deltap;
        for(int i = 0; i < gnumBodies; i++){
            deltav = new Point((bodies[i].force.x/bodies[i].mass)*DT,(bodies[i].force.y/bodies[i].mass)*DT);
            deltap = new Point((bodies[i].velocity.x+deltav.x/2)*DT,(bodies[i].velocity.y+deltav.y/2)*DT);

            bodies[i].velocity.x = bodies[i].velocity.x + deltav.x;
            bodies[i].velocity.y = bodies[i].velocity.y + deltav.y;
            bodies[i].position.x = bodies[i].position.x + deltap.x;
            bodies[i].position.y = bodies[i].position.y + deltap.y;
            bodies[i].force.x = bodies[i].force.y = 0.0;
            bodies[i].broadCastPos(i);
        }
    }
    //main
    public static void main(String[] args) {
        //nBody <gnumBodies> <numSteps> <mass> <bodyPerRow>
        nBody b = new nBody(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Double.valueOf(args[2])
                ,Integer.valueOf(args[3]));
        b.initBodies();
        long start = System.nanoTime();
        for(int i = 0;i< b.numSteps;i=i+b.DT){
            b.calculateForces();
            b.moveBodies();
        }
        System.out.println((System.nanoTime()-start)*1e-9);
    }
}
