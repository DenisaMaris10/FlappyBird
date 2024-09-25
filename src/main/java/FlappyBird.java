import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class FlappyBird extends SwingWorker<Integer, Integer> {
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private AtomicInteger score;
    private AtomicBoolean gameOver;
    //private Semaphore sem;
    private int width, heigth;
    private static AtomicInteger nrPipesWindow = new AtomicInteger(0);
    private ReentrantLock maxPipesLock = new ReentrantLock();
    private ReentrantLock newPipeLock = new ReentrantLock();
    private Condition maxPipesCond = maxPipesLock.newCondition();
    private Condition newPipeCond = newPipeLock.newCondition();
    private View view;
    private PipesController pipesController;

    public FlappyBird() {
        width = 850;
        heigth = 638;
        gameOver = new AtomicBoolean(false);
        bird = new Bird(30, 320);
        pipes = new ArrayList<Pipe>();
        for (int i = 0; i < 8; i++) {
            pipes.add(new Pipe(width, maxPipesLock, newPipeLock, maxPipesCond, newPipeCond, i));
        }
        view = new View(pipes, bird);
        bird.execute();
        for (Pipe p : pipes)
            p.execute();
        pipesController = new PipesController(pipes);
        pipesController.execute();
    }


    @Override
    protected Integer doInBackground() throws Exception {
        int minTime = 1500, maxTime = 4000, currentTime;
        //System.out.println("Game: Am inceput");
        while (!gameOver.get()) {
            //System.out.println("Game: Am inceput");
            maxPipesLock.lock();
            System.out.println("Nr. Pipe-uri:" + nrPipesWindow.get());
            while (nrPipesWindow.get() >= 8) {
                maxPipesCond.await();
            }
            //System.out.println("Game: Am iesit din primul lock");
            maxPipesLock.unlock();

            currentTime = (int)(Math.random() * (maxTime - minTime) + minTime);
            sleep(currentTime);

            newPipeLock.lock();
            Pipe.setAvailable(true); // trezesc un pipe si reintra prin stanga ferestrei
            newPipeCond.signal();
            //System.out.println("Game: Am trezit");
            newPipeLock.unlock();
        }
        return null;
    }

    public void movePipes() {
        for (Pipe p : pipes) {
            p.move();
        }
    }

    public void process(List<Integer> chunks) {

    }

    public static void setNrPipesWindow(AtomicInteger nrPipesWindow) {
        FlappyBird.nrPipesWindow = nrPipesWindow;
    }
}
