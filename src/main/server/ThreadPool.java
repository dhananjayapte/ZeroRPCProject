package main.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {

	private final BlockingQueue<Runnable> workerQueue;
    private final Thread[] workerThreads;
 
    public ThreadPool(int numThreads) {
        workerQueue = new LinkedBlockingQueue<Runnable>();
        workerThreads = new Thread[numThreads];
 
        int i = 0;
        for (Thread t : workerThreads) {
            i++;
            t = new Worker("Pool Thread "+i);
            t.start();
        }
    }
    
    public void addTask(Runnable r){
        try {
            workerQueue.put(r);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
 
    private class Worker extends Thread {
        
        public Worker(String name){
            super(name);
        }
        
        public void run() {
            while (true) {
                try {
                    Runnable r = workerQueue.take();
                    r.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
}
