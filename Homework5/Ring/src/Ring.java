import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Ring {
    private int n;
    private int id;

    private int R;
    private int nextId;
    private int v;
    private int max;
    private int min;

    private PrintWriter out;
    private BufferedReader in;



    public Ring(int id, int n, int R) throws IOException {
        this.id = id;
        this.n = n;
        this.R = R;
        this.nextId = (id+1)%n;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        Ring process = new Ring(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Integer.valueOf(args[2]));
        long start = System.currentTimeMillis();
        process.start();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        Thread.sleep(50000);

    }



    public void firstRound() throws IOException{
        String ins;
        if(id == 0){


            out.println(min+","+max+","+id);
            out.flush();
            ins = in.readLine();
            compare(ins);


        }else{
            ins = in.readLine();
            compare(ins);
            out.println(min+","+max+","+id);
            out.flush();

        }


    }
    public void secondRound() throws IOException, ClassNotFoundException {
        if(id == 0){
            out.println(min+","+max);
            out.flush();


        }else{
            if(id != n-1){
                String[] arr = in.readLine().split(",");
                min = Integer.valueOf(arr[0]);
                max = Integer.valueOf(arr[1]);
                if(id < n-2) {
                    out.println(min + "," + max);
                    out.flush();
                }
            }


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
    private void compare(String result){
        String[] arr = result.split(",");
        checkInteger(Integer.valueOf(arr[0]));
        checkInteger(Integer.valueOf(arr[1]));
    }
    public void start() throws IOException, ClassNotFoundException, InterruptedException {
        Random rand = new Random();
        ServerSocket ss = new ServerSocket(800 + id);
        Socket next = trySocket();
        Socket prev = ss.accept();
        out = new PrintWriter(next.getOutputStream());
        in = new BufferedReader(new InputStreamReader(prev.getInputStream()));
        for(int i = 0; i < R; i++) {
            v = rand.nextInt(100);
            System.out.println(v);
            if(i == 0) {
                min = v;
                max = v;
            }else {
                checkInteger(v);
            }
            firstRound();
            secondRound();
            System.out.println("p" + id + " min:" + min + " max:" + max+" R: "+i);
        }

        next.close();
        prev.close();
        ss.close();
        System.out.println("p" + id + " min:" + min + " max:" + max);
    }
     public Socket trySocket() throws InterruptedException {
        Socket s;
        try {
             s = new Socket("localhost", 800 + nextId);
        }catch (ConnectException e){
              s = trySocket();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         return s;
     }
}




