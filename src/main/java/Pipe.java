import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

public class Pipe extends SwingWorker<Void, ArrayList<Integer>> implements Comparable<Pipe> {
    public AtomicInteger coordX, topCoordY, buttomCoordY;
    private ImageIcon topPipeImage, buttomPipeImage;
    private JLabel topPipeLabel, buttomPipeLabel;
    private int pipeWidth = 31, pipeHeigth = 250;
    private boolean gameOver = false;
    private int velocityX = -10;
    private AtomicBoolean insideWindow = new AtomicBoolean(false);
    private Semaphore sem;
    private static AtomicInteger nrPipesWindow = new AtomicInteger(0);
    private ReentrantLock maxPipesLock, newPipeLock;
    private Condition maxPipesCond;
    private Condition newPipeCond;
    private AtomicBoolean passed = new AtomicBoolean(false);
    private static boolean available;
    private static int nrThreaduri;
    private int indice;

    public Pipe(int width, ReentrantLock maxLock, ReentrantLock newLock, Condition cMaxPipes, Condition cNewPipe, int indice){
        this.indice = indice;
        maxPipesLock = maxLock;
        newPipeLock = newLock;
        maxPipesCond = cMaxPipes;
        newPipeCond = cNewPipe;
        topPipeImage = new ImageIcon("src/toppipe.png");
        buttomPipeImage = new ImageIcon("src/bottompipe.png");
        topPipeLabel = new JLabel("", topPipeImage, JLabel.CENTER);
        buttomPipeLabel = new JLabel("", buttomPipeImage, JLabel.CENTER);
        this.topCoordY = new AtomicInteger(0);
        this.coordX = new AtomicInteger(-pipeWidth);
        this.buttomCoordY = new AtomicInteger(388); //638-250
        topPipeLabel.setBounds(this.coordX.get(), this.topCoordY.get(), pipeWidth, pipeHeigth);
        buttomPipeLabel.setBounds(this.coordX.get(), this.buttomCoordY.get(), pipeWidth, pipeHeigth);
        //resetPipe();
        //topPipeLabel.setLocation(this.coordX.get(), this.topCoordY.get());
        //buttomPipeLabel.setLocation(this.buttomCoordY.get(), this.coordX.get());
    }

    public void movePipes() {
        coordX.addAndGet(100);
        buttomPipeLabel.setLocation(this.coordX.get(), this.buttomCoordY.get());
        topPipeLabel.setLocation(this.coordX.get(), this.topCoordY.get());
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!gameOver){
            //System.out.println("Pipe: coordX" + coordX);
            //coordX.addAndGet(velocityX);
            if(coordX.get()<=-pipeWidth){
                resetPipe();
                insideWindow.set(false);
                // in momentul in care un pipe ajunge la final, adica trece de pasare si "iese" din fereastra, anunta threadul "FlappyBird" (jocul in sine)
                // ca este disponibil sa fie din nou introdus in fereastra (sa fie un nou obstacol pentru pasare)
                maxPipesLock.lock();
//                if(nrPipesWindow.get()!=0) {
//                    nrPipesWindow.getAndDecrement(); // nu cred ca mai era nevoie sa folosesc AtomicInteger, avand in vedere ca folosesc lacatul
//                    FlappyBird.setNrPipesWindow(nrPipesWindow);
//                }
                maxPipesCond.signal();
                maxPipesLock.unlock();

                // thread-ul pipe-ului care tocmai a ajuns la final va astepta pana in momentul in care primeste semnal de la threadul "FlappyBird"
                newPipeLock.lock();
                while(!available) {
                    newPipeCond.await();
                }
                insideWindow.set(true);
                available = false;
//                nrPipesWindow.getAndIncrement();
//                FlappyBird.setNrPipesWindow(nrPipesWindow);
                newPipeLock.unlock();

            }


            try {
                sleep(200);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void process(List<ArrayList<Integer>> chunks){
//        ArrayList<Integer> coord = chunks.get(chunks.size() - 1);
//        buttomPipeLabel.setLocation(coord.get(0), coord.get(1));
//        topPipeLabel.setLocation(coord.get(0), coord.get(2));
        //System.out.println("CoordX: " + coord.get(0)+ " CoordY: " + coord.get(1));
    }

    public void move(){
        coordX.addAndGet(velocityX);
    }



    public void setPipeLocation(){
        topPipeLabel.setLocation(coordX.get(), topCoordY.get());
        buttomPipeLabel.setLocation(coordX.get(), buttomCoordY.get());
    }
    public void resetPipe(){
        coordX.set(850);
        passed.set(false);
    }

    public void gameOver(){
        gameOver = true;
    }

    public JLabel getTopPipeLabel() {
        return topPipeLabel;
    }

    public JLabel getButtomPipeLabel() {
        return buttomPipeLabel;
    }

    public int getCoordX() {
        return coordX.get();
    }

    public boolean isInsideWindow() {
        return insideWindow.get();
    }

    public void setInsideWindow(boolean insideWindow) {
        this.insideWindow.set(insideWindow);
    }

    public static void setAvailable(boolean b){
        available = b;
    }

    public static void setNrPipesWindow(AtomicInteger pipes){
        nrPipesWindow.set(pipes.get());
    }

    @Override
    public int compareTo(Pipe o) {
        if(this.getPassed() && !o.getPassed())
            return 1;
        else if(!this.getPassed() && o.getPassed())
            return -1;
        else{
            if (this.isInsideWindow() && !o.isInsideWindow())
                return -1;
            else {
                if (!this.isInsideWindow() && o.isInsideWindow())
                    return 1;
                else
                    return this.getCoordX() - o.getCoordX();
            }
        }
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
    public int getIndice(){
        return indice;
    }
    public String toString(){
        return indice + " " + coordX + " " + passed ;
    }

    public boolean getPassed(){
        return passed.get();
    }
    public boolean hasPassed(int coord){
        if(coord > coordX.get()/2){
            passed.set(true);
            return true;
        }
        return false;
    }
    public void setPassed(boolean b){
        passed.set(b);
    }

    public int getLowestCoordYTopPipe(){
        return topCoordY.get()+pipeHeigth;
    }

    public int getHighestCoordYButtomPipe(){
        return buttomCoordY.get();
    }


//    @Override
//    public void run() {
//        try {
//            while (!gameOver) {
//                //System.out.println("Pipe: coordX" + coordX);
//                //coordX.addAndGet(velocityX);
//                if (coordX.get() <= -pipeWidth) {
//                    resetPipe();
//                    insideWindow.set(false);
//                    // in momentul in care un pipe ajunge la final, adica trece de pasare si "iese" din fereastra, anunta threadul "FlappyBird" (jocul in sine)
//                    // ca este disponibil sa fie din nou introdus in fereastra (sa fie un nou obstacol pentru pasare)
//                    maxPipesLock.lock();
////                if(nrPipesWindow.get()!=0) {
////                    nrPipesWindow.getAndDecrement(); // nu cred ca mai era nevoie sa folosesc AtomicInteger, avand in vedere ca folosesc lacatul
////                    FlappyBird.setNrPipesWindow(nrPipesWindow);
////                }
//                    maxPipesCond.signal();
//                    maxPipesLock.unlock();
//
//                    // thread-ul pipe-ului care tocmai a ajuns la final va astepta pana in momentul in care primeste semnal de la threadul "FlappyBird"
//                    newPipeLock.lock();
//                    while (!available) {
//                        newPipeCond.await();
//                    }
//                    insideWindow.set(true);
//                    available = false;
////                nrPipesWindow.getAndIncrement();
////                FlappyBird.setNrPipesWindow(nrPipesWindow);
//                    newPipeLock.unlock();
//
//                }
//
//
//                try {
//                    sleep(200);
//                } catch (Exception e) {
//                    System.out.println(e.getMessage());
//                }
//            }
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
//    }
}
