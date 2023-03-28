//body class
//contains the position, velocity, force and mass of the body
public class Body {
    Point position;
    Point velocity;
    Point force;
    double mass;
   //Constructor
    public Body(Point position, Point velocity, Point force, double mass){
        this.position = position;
        this.velocity = velocity;
        this.force = force;
        this.mass = mass;
    }
    //prints out the current position of the body
    public void broadCastPos(int i){
        System.out.println("body:"+i+" x:"+position.x+" y:"+position.y);
    }
}
