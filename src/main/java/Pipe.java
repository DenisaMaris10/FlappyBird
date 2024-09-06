import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Pipe extends SwingWorker<Void, ArrayList<Integer>>{
    private AtomicInteger coordX, topCoordY, buttomCoordY;
    private ImageIcon topPipeImage, buttomPipeImage;
    private JLabel topPipeLabel, buttomPipeLabel;
    private int pipeWidth = 31, pipeHeigth = 250;
    private boolean gameOver = false;
    private int velocityX = -10;

    public Pipe(int width){
        topPipeImage = new ImageIcon("src/toppipe.png");
        buttomPipeImage = new ImageIcon("src/bottompipe.png");
        topPipeLabel = new JLabel("", topPipeImage, JLabel.CENTER);
        buttomPipeLabel = new JLabel("", buttomPipeImage, JLabel.CENTER);
        this.topCoordY = new AtomicInteger(0);
        this.coordX = new AtomicInteger(width);
        this.buttomCoordY = new AtomicInteger(388); //638-250
        topPipeLabel.setBounds(this.coordX.get(), this.topCoordY.get(), pipeWidth, pipeHeigth);
        buttomPipeLabel.setBounds(this.coordX.get(), this.buttomCoordY.get(), pipeWidth, pipeHeigth);
        topPipeLabel.setLocation(this.coordX.get(), this.topCoordY.get());
        buttomPipeLabel.setLocation(this.buttomCoordY.get(), this.coordX.get());
    }

    public void movePipes() {
        coordX.addAndGet(100);
        buttomPipeLabel.setLocation(this.coordX.get(), this.buttomCoordY.get());
        topPipeLabel.setLocation(this.coordX.get(), this.topCoordY.get());
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!gameOver){
            coordX.addAndGet(velocityX);
            publish(new ArrayList<Integer>(Arrays.asList(coordX.get(), buttomCoordY.get(), topCoordY.get())));
            try {
                sleep(100);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void process(List<ArrayList<Integer>> chunks){
        ArrayList<Integer> coord = chunks.get(chunks.size() - 1);
        buttomPipeLabel.setLocation(coord.get(0), coord.get(1));
        topPipeLabel.setLocation(coord.get(0), coord.get(2));
        //System.out.println("CoordX: " + coord.get(0)+ " CoordY: " + coord.get(1));
    }

    public JLabel getTopPipeLabel() {
        return topPipeLabel;
    }

    public JLabel getButtomPipeLabel() {
        return buttomPipeLabel;
    }
}
