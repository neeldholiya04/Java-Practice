package ProductConsumer;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ProductConsumer {
    public static void main(String[] args) {
        Store store = new Store(10);
        ExecutorService es = Executors.newCachedThreadPool();
        Semaphore pSem = new Semaphore(10);
        Semaphore cSem = new Semaphore(10);
        for(int i = 0; i < 100; i++) {
            Producer producer = new Producer(pSem, cSem, store, i);
            es.submit(producer);
        }
        for(int i = 0; i < 100; i++) {
            Consumer consumer = new Consumer(pSem, cSem, store, i);
            es.submit(consumer);
        }

        es.shutdown();
    }

}

class Store {
    int currSize;
    int capacity;

    public Store(int capacity) {
        this.currSize = 0;
        this.capacity = capacity;
    }
}

class Consumer implements Callable<Void> {
    Store store;
    int id;
    Semaphore pSem;
    Semaphore cSem;
    public Consumer(Semaphore pSem, Semaphore cSem, Store store, int id) {
        this.store = store;
        this.id = id;
        this.pSem = pSem;
        this.cSem = cSem;
    }

    @Override
    public Void call() throws InterruptedException {
        while (true) {
            cSem.acquire();
            if (this.store.currSize > 0) {
                System.out.println("Consumer " + this.id + " consumed " + this.store.currSize);
                this.store.currSize--;
                System.out.println("Current itemCount in store is " + this.store.currSize);
            }
            pSem.release();
        }
    }
}

class Producer implements Callable<Void> {
    Store store;
    int id;
    Semaphore pSem;
    Semaphore cSem;
    public Producer(Semaphore pSem, Semaphore cSem, Store store, int id) {
        this.store = store;
        this.id = id;
        this.pSem = pSem;
        this.cSem = cSem;
    }

    @Override
    public Void call() throws InterruptedException {
        while (true) {
            pSem.acquire();
                if (this.store.currSize < this.store.capacity) {
                    System.out.println("Producer " + this.id + " produced " + this.store.currSize);
                    this.store.currSize++;
                    System.out.println("Current itemCount in store is " + this.store.currSize);
                }
            cSem.release();
        }
    }
}