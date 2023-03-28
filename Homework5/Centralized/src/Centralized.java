import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Centralized {
    private int v;
    private int id;
    private int max;
    private int min;
    private int n;
    private int R;
    public Centralized(int id, int n, int R){
        this.id = id;
        this.n = n;
        this.R = R;

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("hello");
        Centralized process = new Centralized(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Integer.valueOf(args[2]));
        Long start = System.currentTimeMillis();
        process.start();
        Long end = System.currentTimeMillis();
        System.out.println(end-start);

        Thread.sleep(5000);
    }
    public void start() throws IOException {
        Random rand = new Random();
        if(id == 0){
            ArrayList peers = new ArrayList<>();
            try {
                ServerSocket ss = new ServerSocket(8080);
                for (int j = 0; j < n - 1; j++) {

                    Socket s = ss.accept();
                    peers.add(s);

                }
                for (int i = 0; i < R; i++) {
                    v = rand.nextInt( 100);
                    if(i == 0){
                        max = v;
                        min = v;
                    }else {
                        checkInteger(v);
                    }


                    receive(peers);
                    broadcast(peers);


                }
                //System.out.println("p" + id + " min:" + min + " max:" + max);
                ss.close();
            } catch(IOException e){
                throw new RuntimeException(e);

            }

        }else {
            Socket s = new Socket("localhost", 8080);
            PrintWriter out = new PrintWriter(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            for (int i = 0; i < R; i++) {
                v = rand.nextInt( 100);
                out.println(v);
                out.flush();
                String[] arr = in.readLine().split(",");
                min = Integer.valueOf(arr[0]);
                max = Integer.valueOf(arr[1]);
                System.out.println("p" + id + " min:" + min + " max:" + max);

            }

            out.close();
            in.close();
            s.close();
        }
    }
    private void checkInteger(int v){
        if(v < this.min){
            this.min = v;
        }
        if(v > this.max){
            this.max = v;
        }
    }
    private void receive(ArrayList<Socket> peers) throws IOException {
        for(Socket s:peers){
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            checkInteger(Integer.valueOf(in.readLine()));

        }
    }
    private void broadcast(ArrayList<Socket> peers) throws IOException {

        for(Socket s:peers){
            PrintWriter out = new PrintWriter(s.getOutputStream());
            out.println(min+","+max);
            out.flush();


        }
    }
}
