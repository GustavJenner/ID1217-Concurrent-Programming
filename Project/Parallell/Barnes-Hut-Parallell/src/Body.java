public class Body {
    Point position;
    Point velocity;
    Point force;
    double mass;

    int id;
    public Body(Point position, Point velocity,Point force, double mass, int id){
        this.position = position;
        this.velocity = velocity;
        this.force = force;
        this.mass = mass;
        this.id = id;

    }
    public void broadCastPos(){
        System.out.println("body:"+id+" x:"+position.x+" y:"+position.y);
    }
    public void movePoint(int DT){
        Point deltav = new Point(this.force.x/this.mass*DT,this.force.y* this.mass*DT);
        Point deltap = new Point((this.velocity.x+deltav.x/2)*DT,(this.velocity.y+deltav.y/2)*DT);
        this.velocity.x += deltav.x;
        this.velocity.y += deltav.y;
        this.position.x += deltap.x;
        this.position.y += deltap.y;


    }


}
