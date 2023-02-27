import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        //inputs
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("trips");
        int trips = Integer.valueOf(in.readLine());
        System.out.println("northCars");
        int northCars = Integer.valueOf(in.readLine());
        System.out.println("southCars");
        int southCars = Integer.valueOf(in.readLine());
        //monitor
        Bridge b = new Bridge();
        //create car threads
        for(int i=0;i<(northCars+southCars);i++){
            if(i<northCars){
                new Cars(true,i,trips,b).start();
            }
            else{new Cars(false,i,trips,b).start();
            }
        }

    }
}
