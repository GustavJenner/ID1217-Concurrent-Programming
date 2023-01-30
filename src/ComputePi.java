import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.io.BufferedReader;
import java.io.InputStreamReader;
public class ComputePi {
    private int threads;
    private double e;
    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException, IOException {
        ComputePi n = new ComputePi();
        n.cInput();
        PiThread p = n.new PiThread(0, 1, f(0), f(1), 0);
        Thread t = new Thread(p);
        t.start();
        t.join();
        System.out.println(p.getAns() * 4);
    }

    static double f(double x) {
        return Math.pow((1 - Math.pow(x, 2)), 0.5);
    }
    public void cInput() throws IOException {
        System.out.println("Threads:");
        String threads = in.readLine();
        System.out.println("e:");
        String e = in.readLine();
        this.threads = Integer.parseInt(threads);
        this.e = Double.parseDouble(e);

    }


    class PiThread implements Runnable {
        private final double l;
        private final double r;
        private final double fl;
        private final double fr;
        private final double area;

        private boolean flag = false;
        private double ans;

        public PiThread(double l, double r, double fl, double fr, double area) {
            this.l = l;
            this.r = r;
            this.fl = fl;
            this.fr = fr;
            this.area = area;



        }

        @Override
        public void run() {
            double m = (l + r) / 2;
            double fm = f(m);
            double larea = (fl + fm) * (m - l) / 2;
            double rarea = (fm + fr) * (r - m) / 2;
            if (Math.abs((larea + rarea) - area) > e) {
                lock.lock();
                try {
                    if (threads >= 2) {
                        threads -= 2;
                        flag = true;
                    }
                }finally {
                    lock.unlock();
                }
                if(flag){
                    flag = false;
                    PiThread p1 = new PiThread(l, m, fl, fm, larea);
                    PiThread p2 = new PiThread(m, r, fm, fr, rarea);
                    Thread t1 = new Thread(p1);
                    Thread t2 = new Thread(p2);
                    t1.start();
                    t2.start();

                    try {
                        t1.join();
                        t2.join();
                        ans = p1.getAns() + p2.getAns();


                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }else{
                    larea = quad(l,m,fl,fm,larea);
                    rarea = quad(m,r,fm,fr,rarea);
                    ans = larea + rarea;
                }


            }  else {
                ans = area;
            }


        }

        public double getAns() {
            return ans;

        }

        public double quad(double l, double r, double fl, double fr, double area){
            double m = (l + r) / 2;
            double fm = f(m);
            double larea = (fl + fm) * (m - l) / 2;
            double rarea = (fm + fr) * (r - m) / 2;
            if (Math.abs((larea + rarea) - area) > e) {
                larea = quad(l,m,fl,fm,larea);
                rarea = quad(m,r,fm,fr,rarea);

            }
            return larea + rarea;
            }

        }

    }


