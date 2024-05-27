import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // TODO: Seed your randomizer
        Random rand = new Random();

        // TODO: Get array size and thread count from user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the array size: ");
        int arraySize = scanner.nextInt();
        System.out.print("Enter the number of threads: ");
        int threadCount = scanner.nextInt();
        scanner.close();

        // TODO: Generate a random array of given size
        int[] array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = rand.nextInt(arraySize); // generate random integers between 0 and 99
        }

        Integer[] concurrentArray = Arrays.stream(array).boxed().toArray( Integer[]::new );

        // TODO: Call the generate_intervals method to generate the merge sequence
        long time = System.currentTimeMillis();
        List<Interval> intervals = generate_intervals(0, array.length - 1);

        // TODO: Call merge on each interval in sequence
        
        for (Interval interval : intervals) {
            merge(array, interval.getStart(), interval.getEnd());
        }
        long currentTime = System.currentTimeMillis() - time;
        System.out.println("Single Threaded Time: " + currentTime);

        // Output the sorted array
        // System.out.println("Sorted array:");
        // for (int i : array) {
        //     System.out.print(i + " ");
        // }
        // System.out.println();

        // Once you get the single-threaded version to work, it's time to 
        // implement the concurrent version. Good luck :)

        // Concurrent version || assuming we want to use a fixed number of threads
        // int[] concurrentArray = array.clone();
        // mergeSortConcurrent(concurrentArray, 0, concurrentArray.length - 1, threadCount);
        MergeSort mergeSort = new MergeSort(threadCount); // instantiation of MergeSort Class (takes20)
        mergeSort.threadedSort(concurrentArray); // the actuall threaded merge sort

        // Output the sorted array from concurrent merge sort
        // System.out.println("Sorted array (concurrent):");
        // for (int i : concurrentArray) {
        //     System.out.print(i + " ");
        // }
        // System.out.println();
    }

    private static Thread mergeSortThread(int[] array, int start, int end, int threadCount) {
        return new Thread(() -> {
            // mergeSortConcurrent(array, start, end, threadCount);
        });
    }

    // Merge Sort method
    public static void mergeSort(int[] array, int start, int end) {
        if (start < end) {
            int mid = start + (end - start) / 2;
            mergeSort(array, start, mid);
            mergeSort(array, mid + 1, end);
            merge(array, start, end);
        }
    }

    /*
    This function generates all the intervals for merge sort iteratively, given 
    the range of indices to sort. Algorithm runs in O(n).

    Parameters:
    start : int - start of range
    end : int - end of range (inclusive)

    Returns a list of Interval objects indicating the ranges for merge sort.
    */
    public static List<Interval> generate_intervals(int start, int end) {
        List<Interval> frontier = new ArrayList<>();
        frontier.add(new Interval(start,end));

        int i = 0;
        while(i < frontier.size()){
            int s = frontier.get(i).getStart();
            int e = frontier.get(i).getEnd();

            i++;

            // if base case
            if(s == e){
                continue;
            }

            // compute midpoint
            int m = s + (e - s) / 2;

            // add prerequisite intervals
            frontier.add(new Interval(m + 1,e));
            frontier.add(new Interval(s,m));
        }

        List<Interval> retval = new ArrayList<>();
        for(i = frontier.size() - 1; i >= 0; i--) {
            retval.add(frontier.get(i));
        }

        return retval;
    }

    /*
    This function performs the merge operation of merge sort.

    Parameters:
    array : vector<int> - array to sort
    s     : int         - start index of merge
    e     : int         - end index (inclusive) of merge
    */
    public static void merge(int[] array, int s, int e) {
        int m = s + (e - s) / 2;
        int[] left = new int[m - s + 1];
        int[] right = new int[e - m];
        int l_ptr = 0, r_ptr = 0;
        for(int i = s; i <= e; i++) {
            if(i <= m) {
                left[l_ptr++] = array[i];
            } else {
                right[r_ptr++] = array[i];
            }
        }
        l_ptr = r_ptr = 0;

        for(int i = s; i <= e; i++) {
            // no more elements on left half
            if(l_ptr == m - s + 1) {
                array[i] = right[r_ptr];
                r_ptr++;

            // no more elements on right half or left element comes first
            } else if(r_ptr == e - m || left[l_ptr] <= right[r_ptr]) {
                array[i] = left[l_ptr];
                l_ptr++;
            } else {
                array[i] = right[r_ptr];
                r_ptr++;
            }
        }
    }
}

class Interval {
    private int start;
    private int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}