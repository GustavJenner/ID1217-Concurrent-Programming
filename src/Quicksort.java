

public class Quicksort {
    public static void main(String[] args) {
        QuicksortThreaded q2 = new QuicksortThreaded();
        Quicksort q = new Quicksort();
        int[] arr = q2.randomArr(1000000,0,1000000);
        long start = System.currentTimeMillis();
        q.quicksort(arr,0,arr.length-1);
        long end = System.currentTimeMillis();
        System.out.println(end-start);


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
    public void quicksort(int[] arr,int low, int high){
        if(low<high) {
            int pi = partition(arr, low, high);
            quicksort(arr,low,pi-1);
            quicksort(arr,pi+1,high);
        }

    }
    public void printArr(int[] arr){
        String s = "";
        for(int i:arr){
            s += String.valueOf(i)+",";
        }
        System.out.println(s);
    }

}