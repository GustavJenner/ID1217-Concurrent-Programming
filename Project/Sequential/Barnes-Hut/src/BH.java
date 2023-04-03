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

    double percentage = 0.0;


    public BH(int gnumBodies, int numSteps, double mass, int bodyPerRow, double far, double side){
        this.gnumBodies = gnumBodies;
        this.numSteps = numSteps;
        this.mass = mass;
        this.bodyPerRow = bodyPerRow;
        this.far = far;
        this.side = side;
        this.bodies = new Body[gnumBodies];
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
            bodies[i] = new Body(new Point(x,y),new Point(0,0),new Point(0,0),mass,i);
            //bodies[i].broadCastPos();

        }

    }
    public void broadcast(){
        for(int i=0;i<gnumBodies;i++){
            bodies[i].broadCastPos();
        }
    }

    public static void main(String[] args) {
        BH tree = new BH(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Double.valueOf(args[2]),
                Integer.valueOf(args[3]),Double.valueOf(args[4]),Double.valueOf(args[5]));
        tree.initBodies();
        long start = System.nanoTime();
        for(int j = 0; j< tree.numSteps;j++) {
            tree.root = new QuadNode(new Point(0,0), tree.side, tree.far);
            for (int i = 0; i < tree.gnumBodies; i++) {
                tree.root.insert(tree.bodies[i]);

            }
            for (int i = 0; i < tree.gnumBodies; i++) {
                tree.root.calculateForce(tree.bodies[i]);
                tree.percentage += tree.bodies[i].count;

        }   //for finding percentage of bodies being aprox
            //System.out.println(tree.percentage/ tree.gnumBodies);

            for(int i = 0; i < tree.gnumBodies;i++){
                tree.bodies[i].movePoint(tree.DT);
                tree.bodies[i].force.x = tree.bodies[i].force.y = 0.0;
            }
            }
        System.out.println((System.nanoTime()-start)/1e9);
        //tree.broadcast();

    }



}
