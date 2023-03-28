import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static java.net.StandardSocketOptions.IP_MULTICAST_LOOP;

public class Symmetric {
    private int id;
    private int n;

    private int R;

    private int v;
    private int min;
    private int max;
    public Symmetric(int id, int n, int R){
        this.id = id;
        this.n = n;
        this.R = R;

    }
    public static void main(String[] args) throws IOException, InterruptedException {

        Symmetric process = new Symmetric(Integer.valueOf(args[0]),Integer.valueOf(args[1]),Integer.valueOf(args[2]));
        Long start = System.currentTimeMillis();
        process.start();
        Long end = System.currentTimeMillis();
        System.out.println(end-start);
        Thread.sleep(50000);

    }
    public void start(){
        Random rand = new Random();
        InetAddress mcastaddr;

        try {
            mcastaddr = InetAddress.getByName("228.5.6.7");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        InetSocketAddress group = new InetSocketAddress(mcastaddr, 8080);

        try {
            NetworkInterface netIf = NetworkInterface.getByInetAddress(new InetSocketAddress("127.0.0."+id,8080).getAddress());
            MulticastSocket s = new MulticastSocket(8080);
            s.joinGroup(group,netIf);



            for(int i = 0; i < R; i++) {
                v = rand.nextInt(100);
                if(i == 0) {
                    max = v;
                    min = v;
                }
                String msg = String.valueOf(v);
                byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
                DatagramPacket h = new DatagramPacket(msgBytes,msgBytes.length, group.getAddress(),8080);
                if(id == n-1 && i == 0) {
                    System.out.println("hello");
                    s.send(h);
                } else if (i >= 1) {
                    s.send(h);
                }
                int temp;
                byte[] buf = new byte[1000];
                System.out.println("p"+id+":"+v);
                for (int j = 0; j < n; j++) {
                    DatagramPacket rec = new DatagramPacket(buf, buf.length);
                    s.receive(rec);
                    temp = Integer.valueOf(new String(rec.getData(), 0, rec.getLength()));
                    System.out.println(temp);
                    checkInteger(temp);
                    if(i == 0 && id != n-1 && j == 0){
                        s.send(h);
                    }


                }

            }

            System.out.println("p"+id+" min:"+min+" max:"+max);
            if(id == 0){
                Thread.sleep(100);
            }
            s.close();

        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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




}

