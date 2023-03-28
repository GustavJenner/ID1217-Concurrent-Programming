public class QuadNode {
    Body body = null;
    Point center;
    Double side;
    QuadNode NW,NE,SE,SW = null;

    double G = 6.67e-11;


    double theta;
    public QuadNode(Point center, Double side, double theta){
        this.center = center;
        this.side = side;
        this.theta = theta;


    }
    public void insert(Body body) {
        if (this.body == null) {
            this.body = body;
            return;

        }
        if (NW == null && NE == null && SE == null && SW == null) {

            createQuad();
            add(this.body);
            add(body);
            updateBody(body);


        }
        else{
            updateBody(body);
            add(body);


        }
    }
    public Boolean checkRegion(Point p){
        return (this.center.x-this.side)<=p.x && p.x <=(this.center.x+this.side) &&
                (this.center.y-this.side)<=p.y && p.y <=(this.center.y+this.side);

        }
        public void createQuad(){
        Double small = side/2;
        this.NW = new QuadNode(new Point(center.x-small,center.y+small),small,theta);
        this.NE = new QuadNode(new Point(center.x+small,center.y+small),small,theta);
        this.SE = new QuadNode(new Point(center.x+small,center.y-small),small,theta);
        this.SW = new QuadNode(new Point(center.x-small,center.y-small),small,theta);

        }
        public void add(Body body){
            if(NW.checkRegion(body.position)){
                NW.insert(body);
                return;
            }
            if(NE.checkRegion(body.position)){
                NE.insert(body);
                return;
            }
            if(SE.checkRegion(body.position)){
                SE.insert(body);
                return;
            }
            if(SW.checkRegion(body.position)){
                SW.insert(body);
            }
        }
        public void updateBody(Body body){
        double totMass = body.mass+this.body.mass;
        double x = (body.position.x*body.mass+this.body.position.x*this.body.mass)/totMass;
        double y = (body.position.y*body.mass+this.body.position.y*this.body.mass)/totMass;
        this.body =  new Body(new Point(x,y),new Point(0,0),new Point(0,0),totMass,-1);
        }
        public void calculateForce(Body body){

        if(this.body == null || this.body.equals(body)){
            return;
        }
        if(NW == null && NE == null && SE == null && SW == null || checkFar(body)){

            addForce(body);

        }else{
            NE.calculateForce(body);
            SE.calculateForce(body);
            SW.calculateForce(body);
            NW.calculateForce(body);
        }
        }
        public void addForce(Body body){
            double distance, magnitude;
            Point direction;
            distance = calculateDistance(body);
            magnitude = (G * this.body.mass * body.mass)/(Math.pow(distance,2));
            direction = new Point(this.body.position.x-body.position.x,this.body.position.y-body.position.y);
            body.force.x += magnitude*direction.x/distance;
            body.force.y += magnitude*direction.y/distance;

        }

        public Boolean checkFar(Body body){
        double d = calculateDistance(body);
        return (side/d < theta);


        }
        public double calculateDistance(Body body){
        return Math.sqrt(Math.pow((this.body.position.x-body.position.x),2)+Math.pow((this.body.position.y-body.position.y),2));
        }

    }

