import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class QuicksortThreaded{
    private int threads = 64;
    final ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {
        int[] arr = randomArr(10000000,0,10000000);
        int[] arr2 = arr.clone();
        QuicksortThreaded object = new QuicksortThreaded();

        QThread q = object.new QThread(arr,0,arr.length-1);
        Thread t = new Thread(q);

        //time quicksort with threads
        long start = System.currentTimeMillis();
        t.start();
        t.join();
        long end = System.currentTimeMillis();
        System.out.println(end-start);


        QThread b = object.new QThread(arr2,0,arr2.length-1);

        //time normal quicksort
        long start2 = System.currentTimeMillis();
        b.quicksort(arr2,0,arr2.length-1);
        long end2 = System.currentTimeMillis();
        System.out.println(end2-start2);

        //check if the arrays are the same
        if(Arrays.equals(arr,arr2)){
            System.out.println("passed");
        }

    }
    //static that prints input array
    static void printArr(int[] arr){
        StringBuilder s = new StringBuilder();
        for(int i:arr){
            s.append(i).append(",");
        }
        System.out.println(s);
    }
    //static method that creates a random array of a certain length
    //min and max determine the range of numbers
    static int[] randomArr(int length, int min, int max){
        Random rand = new Random();
        int[] arr = new int[length];
        for(int i=0;i<arr.length;i++){
            arr[i] = rand.nextInt(max-min) + min;

        }
        return arr;
    }



//thread object
 class QThread implements Runnable{
    private final int[] arr;
    private final int low;
    private final int high;

    private boolean flag = false;


    //thread constructor
    public QThread(int[] arr, int low, int high){
        this.arr = arr;
        this.low = low;
        this.high = high;


    }
    //method that swaps the element i and j in the array
   public void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

   }
   //partition method

   public int partition(int[] arr, int low,int high){
    //last element in subarray as pivot
    int pivot = arr[high];

    //i indicate part of subarray where elements are lower than pivot
    int i = low-1;
    //loop through whole subarray
    for(int j = low; j<= high-1;j++){
        //if element is less than pivot
        //i increases and i and j swap
        if(arr[j]<=pivot) {
            i++;
            swap(arr, i, j);
        }

    }
    //lastly pivot and i+1 swap
    swap(arr,i+1,high);
    return i+1;
   }
   @Override
   public void run(){

            //locking part where threads check for thread count
            lock.lock();
            try {
                if(low<high && threads != 0) {
                    threads -= 2;
                    //flag for indicating that more threads can be created
                    flag = true;
                }
            }finally {
                lock.unlock();
            }


            if(flag){
                int pi = partition(arr, low, high);
                flag = false;
                //instead of calling the quicksort method recursively
                //a new thread is created
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
        //if no more threads are available
        //the quicksort is done recursively
        else if(low<high){
            int pi = partition(arr, low, high);
            quicksort(arr,low,pi-1);
            quicksort(arr,pi+1,high);

        }
   }
    //normal quicksort
     public void quicksort(int[] arr,int low, int high){
         if(low<high) {
             int pi = partition(arr, low, high);
             quicksort(arr,low,pi-1);
             quicksort(arr,pi+1,high);
         }

     }

}
}
