import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class PipesController extends SwingWorker<Void, Void> {
    private ArrayList<Pipe> pipes;
    private AtomicBoolean gameOver = new AtomicBoolean(false);
    private ReentrantLock maxPipesLock = new ReentrantLock();
    private ReentrantLock newPipeLock = new ReentrantLock();
    private Condition maxPipesCond = maxPipesLock.newCondition();
    private Condition newPipeCond = newPipeLock.newCondition();
    private static AtomicInteger nrPipesWindow = new AtomicInteger(0);
    public PipesController(int width){
        pipes = new ArrayList<Pipe>();
        for (int i = 0; i < 6; i++) {
            pipes.add(new Pipe(width, maxPipesLock, newPipeLock, maxPipesCond, newPipeCond, i));
        }
    }
    @Override
    protected Void doInBackground() throws Exception {
        int minTime = 1500, maxTime = 4000, currentTime;
        while (!gameOver.get()) {
            maxPipesLock.lock();
            while (nrPipesWindow.get() >= 8) {
                maxPipesCond.await();
            }
            maxPipesLock.unlock();

            currentTime = (int)(Math.random() * (maxTime - minTime) + minTime);
            //System.out.println(currentTime);
            sleep(currentTime);

            newPipeLock.lock();
            Pipe.setAvailable(true); // trezesc un pipe si reintra prin stanga ferestrei
            newPipeCond.signal();
            newPipeLock.unlock();
        }
        return null;
    }

    public void setGameOver(AtomicBoolean gameOver) {
        this.gameOver = gameOver;
    }

    public ArrayList<Pipe> getPipes() {
        return pipes;
    }
}
