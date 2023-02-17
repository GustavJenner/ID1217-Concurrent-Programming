import java.io.*;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;

public class QuicksortThreaded{
    private int threads;
    final ReentrantLock lock = new ReentrantLock();

    private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private int n;
    public static void main(String[] args) throws InterruptedException, IOException {
        String PATH = "C:\\Users\\Gustav Jenner\\Desktop\\array.txt";
        QuicksortThreaded object = new QuicksortThreaded();
        object.cInput();

        //show that the array is sorted
        int[] arr = randomArr(object.n,0,100);
        printArr(arr);

        //int[] arr = object.readFromFile(PATH);


        QThread q = object.new QThread(arr,0,arr.length-1);
        Thread t = new Thread(q);

        //time quicksort with threads
        long start = System.currentTimeMillis();
        t.start();
        t.join();
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        //after sorted
        printArr(arr);






    }
    static void writeToFile(int[] arr, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (int j : arr) {
            writer.write(j + ",");
        }
        writer.close();
    }

    public int[] readFromFile(String path) throws IOException {

        int[] arr = new int[n];
        File file = new File(path);
        Scanner sc = new Scanner(file).useDelimiter(",");
        int i = 0;
        while(sc.hasNextInt()){
            arr[i] = sc.nextInt();
            i++;
        }
        sc.close();
        return arr;
    }
    //static method that prints input array
    static void printArr(int[] arr){
        StringBuilder s = new StringBuilder();
        for(int i:arr){
            s.append(i).append(",");
        }
        System.out.println(s);
    }
    //consol input
    public void cInput() throws IOException {
        System.out.println("Threads:");
        String threads = in.readLine();
        System.out.println("array length:");
        String n = in.readLine();
        this.threads = Integer.parseInt(threads);
        this.n = Integer.parseInt(n);
        in.close();
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
    i++;
    swap(arr,i,high);
    return i;
   }
   @Override
   public void run(){

            //locking part where threads check for thread count
            lock.lock();
            try {
                if(low<high && threads >= 2) {
                    threads -= 2;
                    //flag for indicating that more threads can be created
                    flag = true;
                }
            }finally {
                lock.unlock();
            }


            if(flag){
                int part = partition(arr, low, high);
                flag = false;
                //instead of calling the quicksort method recursively
                //a new thread is created
                QThread q1 = new QThread(arr,low,part-1);
                QThread q2 = new QThread(arr,part+1,high);
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
            int part = partition(arr, low, high);
            quicksort(arr,low,part-1);
            quicksort(arr,part+1,high);

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
