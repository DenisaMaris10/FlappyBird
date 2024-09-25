import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class PipesController extends SwingWorker<Void, Void> {
    private ArrayList<Pipe> pipes;
    private AtomicBoolean gameOver = new AtomicBoolean(false);
    public PipesController(ArrayList<Pipe> pipes){
        this.pipes = pipes;
    }

    @Override
    protected Void doInBackground() throws Exception {
        //System.out.println("PipesController");
        while(!gameOver.get()){
            for(Pipe p : pipes){
                if(p.isInsideWindow()) {
                    p.move();
                }
            }
            publish();
            sleep(100);
        }
        return null;
    }

    @Override
    protected void process(List<Void> chunks){
        //System.out.println("Pipe-uri inauntru:");
        for(Pipe p : pipes){
            if(p.isInsideWindow()) {
                p.setPipeLocation();
          //      System.out.println(p.getIndice());
            }
        }
    }

    public void setGameOver(AtomicBoolean gameOver) {
        this.gameOver = gameOver;
    }
}
