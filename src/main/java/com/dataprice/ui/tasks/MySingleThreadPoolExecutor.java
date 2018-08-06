package com.dataprice.ui.tasks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MySingleThreadPoolExecutor {

    private ExecutorService executor;
    private static MySingleThreadPoolExecutor instance = null;
    private Future<?> job = null;
    
    private MySingleThreadPoolExecutor() {
        executor = Executors.newSingleThreadExecutor();
      }
    
    public static MySingleThreadPoolExecutor getInstance() {
    	if (instance==null) {
    		instance = new MySingleThreadPoolExecutor();
    	}	
        return instance;
    }

    public Future<?> runTask(Runnable run) {
    	job = executor.submit(run);
    	return job;
    }
    
    
	public Future<?> getCurrentJob() {
		return job;
	}
    
	public void reStart() {
		this.job = null;
	}

}
