public class Body {
    Point position;
    Point velocity;
    Point[] force;
    double mass;
    public Body(Point position, Point velocity,int numWorkers, double mass){
        this.position = position;
        this.velocity = velocity;
        this.force = new Point[numWorkers];
        this.mass = mass;
        for(int i = 0; i<numWorkers;i++){
            this.force[i] = new Point(0.0,0.0);
        }
    }
    public void broadCastPos(int i){
        System.out.println("body:"+i+" x:"+position.x+" y:"+position.y);
    }

}
