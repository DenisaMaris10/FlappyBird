import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Bird extends SwingWorker<Void, ArrayList<Integer>>{
    private AtomicInteger coordX, coordY;
    private ImageIcon birdImage;
    private JLabel birdLabel;
    private int birdWidth = 75, birdHeigth = 75;
    private int jumpVelocityY = -25, gravity = 4;
    private AtomicInteger velocity = new AtomicInteger(0);
    private boolean gameOver = false;

    public Bird(int coordX, int coordY){
        birdImage = new ImageIcon("src/transparent_flappy_bird.png");
        this.coordX = new AtomicInteger(coordX);
        this.coordY = new AtomicInteger(coordY);
        birdLabel = new JLabel("", birdImage, JLabel.CENTER);
        birdLabel.setBounds(coordX, coordY, 75, 75);
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!gameOver){
            try {
                if(coordY.get() + gravity > 638-birdHeigth) {
                    gameOver = true;
                    //coordY = 638-birdHeigth;
                }
                else {
                    velocity.addAndGet(gravity);
                    coordY.addAndGet(velocity.get());
                }
                publish(new ArrayList<Integer>(Arrays.asList(coordX.get(), coordY.get()))); //puteam sa trimit doar coordonata Y, deoarece coordX nu se modifica
                sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void process(List<ArrayList<Integer>> chunks) {
        ArrayList<Integer> coord = chunks.get(chunks.size() - 1);
        birdLabel.setLocation(coord.get(0), coord.get(1));
        //System.out.println("CoordX: " + coord.get(0)+ " CoordY: " + coord.get(1));
    }

    public void moveBird() throws InterruptedException {
        sleep(2000);
        coordY.addAndGet(100);
        birdLabel.setLocation(coordX.get(), coordY.get());
    }
    public void birdJump(){
        velocity.set(jumpVelocityY);
    }

    public void gameOver(){
        gameOver = true;
    }

    public ImageIcon getBirdImage(){
        return birdImage;
    }

    public int getCoordX() {
        return coordX.get();
    }

    public int getCoordY() {
        return coordY.get();
    }

    public JLabel getBirdLabel() {
        return birdLabel;
    }

}
