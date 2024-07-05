import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Adder implements Callable<Void>
{
    Value v;

    public Adder(Value v)
    {
        this.v = v;
    }

    public Void call() throws Exception
    {
        for (int i = 0; i < 10000; i++)
        {
            v.value++;
        }
        return null;
    }
}

class Subtractor implements Callable<Void>
{
    Value v;

    public Subtractor(Value v)
    {
        this.v = v;
    }

    public Void call() throws Exception
    {
        for (int i = 0; i < 10000; i++)
        {
            v.value--;
        }

        return null;
    }
}

class Value {
    int value;

    public Value(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }

    public void subtract(int value) {
        this.value -= value;
    }

    public int getValue() {
        return this.value;
    }
}


public class AdderSubtractor {
    public static void main(String[] args) throws Exception{
        Value v = new Value(20);

        Adder adder = new Adder(v);
        Subtractor subtractor = new Subtractor(v);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Void> adderFuture = executor.submit(adder);
        Future<Void> subtractorFuture = executor.submit(subtractor);

        try {
            adderFuture.get();
            subtractorFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        System.out.println(v.getValue());
    }    
}
