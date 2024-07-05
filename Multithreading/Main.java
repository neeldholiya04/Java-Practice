import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class ListConverter implements Callable<List<Integer>> {
    List<Integer> list1;

    public ListConverter(List<Integer> list1) {
        this.list1 = list1;
    }

    @Override
    public List<Integer> call() {
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            list2.add(list1.get(i) * 2);
        }
        return list2;
    }
}

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException{
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Integer> list1 = new ArrayList<>();
        for(int i=0; i<100; i++) {
            list1.add(i);
        }
        Callable<List<Integer>> worker = new ListConverter(list1);
        Future<List<Integer>> list2 = executor.submit(worker);

        System.out.println(list2.get());
    }
}
