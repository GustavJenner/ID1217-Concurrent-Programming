import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class QuicksortThreaded{
    private int threads = 64;
    final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        int[] arr = randomArr(1000000,0,100000);
        QuicksortThreaded object = new QuicksortThreaded();

        QThread q = object.new QThread(arr,0,arr.length-1);
        Thread t = new Thread(q);
        long start = System.currentTimeMillis();
        t.start();
        t.join();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        printArr(arr);

    }
    static void printArr(int[] arr){
        StringBuilder s = new StringBuilder();
        for(int i:arr){
            s.append(i).append(",");
        }
        System.out.println(s);
    }
    static int[] randomArr(int length, int min, int max){
        Random rand = new Random();
        int[] arr = new int[length];
        for(int i=0;i<arr.length;i++){
            arr[i] = rand.nextInt(max-min) + min;

        }
        return arr;
    }


 class QThread implements Runnable{
    private final int[] arr;
    private final int low;
    private final int high;

    private boolean flag = false;



    public QThread(int[] arr, int low, int high){
        this.arr = arr;
        this.low = low;
        this.high = high;


    }

   public void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

   }
   public int partition(int[] arr, int low,int high){
    int pivot = arr[high];
    int i = low-1;
    for(int j = low; j<= high-1;j++){
        if(arr[j]<=pivot) {
            i++;
            swap(arr, i, j);
        }

    }
    swap(arr,i+1,high);
    return i+1;
   }
   @Override
   public void run(){


            lock.lock();
            try {
                if(low<high && threads != 0) {
                    threads -= 2;
                    flag = true;
                }
            }finally {
                lock.unlock();
            }

            int pi = partition(arr, low, high);
            if(flag){
                flag = false;
                QThread q1 = new QThread(arr,low,pi-1);
                QThread q2 = new QThread(arr,pi+1,high);
                Thread t1 = new Thread(q1);
                Thread t2 = new Thread(q2);
                t1.start();
                t2.start();
                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


        }

        else if(low<high){
            quicksort(arr,low,pi-1);
            quicksort(arr,pi+1,high);

        }
   }
     public void quicksort(int[] arr,int low, int high){
         if(low<high) {
             int pi = partition(arr, low, high);
             quicksort(arr,low,pi-1);
             quicksort(arr,pi+1,high);
         }

     }

}
}
