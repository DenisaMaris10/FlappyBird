import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class FlappyBird extends SwingWorker<Void, Integer> {
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private AtomicInteger score = new AtomicInteger(0);
    private AtomicBoolean gameOver;
    private int width, heigth;
    private View view;
    private PipesController pipesController;
    private JTextField scoreText;

    public FlappyBird() {
        width = 850;
        heigth = 638;
        pipesController = new PipesController(width);
        pipes = pipesController.getPipes();
        gameOver = new AtomicBoolean(false);
        bird = new Bird(30, 320);
        scoreText = new JTextField(10);
        view = new View(pipes, bird, scoreText);

        bird.execute();

        for (Pipe p : pipes) {
            p.execute();
        }
        pipesController.execute();
    }


    @Override
    protected Void doInBackground() throws Exception {
        while(!gameOver.get()){
            for(Pipe p : pipes){
                if(p.isInsideWindow()) {
                    p.move();
                }
            }
            Collections.sort(pipes);
            increaseScore();
            if(gameOver()) {
                System.out.println("Game over");
                new GameOverFrame(score.get(), score.get());
                bird.cancel(true);
                for(Pipe p : pipes){
                    p.cancel(true);
                }
                pipesController.cancel(true);
                this.cancel(true);
            }
            publish(score.get());
            sleep(100);
        }
        return null;
    }

    @Override
    protected void process(List<Integer> chunks){
        Integer score = chunks.get(chunks.size()-1);
        for(Pipe p : pipes){
            p.setPipeLocation();
        }
        scoreText.setSize(60, 20);
        scoreText.setLocation(30, 30);
        scoreText.setText("Score: " + score);
    }

    public void increaseScore(){
        Pipe p = pipes.get(0);
        if(p.hasPassed(bird.getCoordX()))
            if(p.getPassed()) {
                score.getAndIncrement();
            }
    }

    public boolean gameOver(){
        Pipe firstPipe = pipes.get(0);
        //daca pasarea ajunge sa "treaca prin" obstacol, verificam daca nu cumva atinge obstacolul
        if(bird.getCoordX()+bird.getBirdWidth()>=firstPipe.getCoordX()){
            System.out.println("Pipe hit");
            //verificam daca atinge sus
            if(bird.getCoordY() <= firstPipe.getLowestCoordYTopPipe())
                return true;
            System.out.println("Bird: " + (bird.getCoordY()+bird.getBirdHeigth()) + " vs Pipe: Y " + firstPipe.getHighestCoordYButtomPipe() + " X " + firstPipe.getCoordX());
            if(bird.getCoordY()+bird.getBirdHeigth() >= firstPipe.getHighestCoordYButtomPipe())
                return true;
        }
        //return false;
        //daca pasarea nu atinge obstacolul, verificam daca a iesit din fereastra sau nu
        System.out.println("Bird inside window");
        return !bird.insideWindow(heigth);

      }

}
