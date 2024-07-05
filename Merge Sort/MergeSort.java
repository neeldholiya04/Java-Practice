import java.util.concurrent.*;
import java.util.*;

class Sorter implements Callable<ArrayList<Integer>> {
    private ArrayList<Integer> list;

    public Sorter(ArrayList<Integer> list) {
        this.list = list;
    }

    public ArrayList<Integer> call() throws Exception {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        ArrayList<Integer> left = new ArrayList<>(list.subList(0, mid));
        ArrayList<Integer> right = new ArrayList<>(list.subList(mid, list.size()));

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Sorter leftSorter = new Sorter(left);
        Sorter rightSorter = new Sorter(right);

        Future<ArrayList<Integer>> leftFuture = executor.submit(leftSorter);
        Future<ArrayList<Integer>> rightFuture = executor.submit(rightSorter);

        // ArrayList<Integer> sortedList = new ArrayList<>();

        try {
            left = leftFuture.get();
            right = rightFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return merge(left, right);
    }

    private ArrayList<Integer> merge(ArrayList<Integer> left, ArrayList<Integer> right) {
        ArrayList<Integer> merged = new ArrayList<>();
        int leftPointer = 0, rightPointer = 0;

        while (leftPointer < left.size() && rightPointer < right.size()) {
            if (left.get(leftPointer) <= right.get(rightPointer)) {
                merged.add(left.get(leftPointer));
                leftPointer++;
            } else {
                merged.add(right.get(rightPointer));
                rightPointer++;
            }
        }

        while (leftPointer < left.size()) {
            merged.add(left.get(leftPointer));
            leftPointer++;
        }

        while (rightPointer < right.size()) {
            merged.add(right.get(rightPointer));
            rightPointer++;
        }

        return merged;
    }
}

public class MergeSort {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(38, 27, 43, 3, 9, 82, 10, 1));
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Sorter sorter = new Sorter(list);

        Future<ArrayList<Integer>> future = executor.submit(sorter);

        try {
            ArrayList<Integer> sortedList = future.get();
            System.out.println("Sorted list: " + sortedList);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
