import java.util.concurrent.locks.ReentrantLock;

public class QuicksortThreaded{
    public static void main(String[] args) throws InterruptedException {
        int[] arr = {10, 7, 8, 9, 1, 5};

        QThread q = new QThread(arr,0,arr.length-1);
        Thread t = new Thread(q);
        t.start();
        t.join();
        printArr(arr);

    }
    static void printArr(int[] arr){
        String s = "";
        for(int i:arr){
            s += String.valueOf(i)+",";
        }
        System.out.println(s);
    }
}

 class QThread implements Runnable{
    private int[] arr;
    private int low;
    private int high;

    private final ReentrantLock lock = new ReentrantLock();

    private boolean exit;
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
   public void run(){
        if(low<high) {
            int pi = 0;
            lock.lock();
            try {
                 pi = partition(arr, low, high);

            }finally {
                System.out.println(Thread.currentThread().getName());
                lock.unlock();

            }
            QThread q1 = new QThread(arr,low,pi-1);
            QThread q2 = new QThread(arr,pi+1,high);
            Thread t1 = new Thread(q1);
            Thread t2 = new Thread(q2);
            t1.start();
            t2.start();

        }
        else{
            Thread.currentThread().interrupt();

        }



   }

}
