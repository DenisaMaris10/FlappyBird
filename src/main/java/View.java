import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class View extends JFrame {
    Image bgImage = Toolkit.getDefaultToolkit().getImage("C:\\temp\\Facultate\\Proiecte\\Flappy_Bird\\FlappyBird\\src\\flappy-bird-game-background.jpg");
    JLabel bgLabel;
    private Bird bird;
    private ArrayList<Pipe> pipes;

    public View(ArrayList<Pipe> pipes, Bird b){

        this.bird = b;
        this.pipes = pipes;

        setLayout(null);
        this.setSize(850, 638);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //overlaylayout pentru a putea afisa imagini deasupra altor imagini
        Container container = this.getContentPane();
        this.setLayout(new OverlayLayout(container));

        //background
        ImageIcon image = new ImageIcon("src/flappy-bird-game-background.jpg");
        bgLabel = new JLabel("", image, JLabel.CENTER);
        bgLabel.setBounds(0, 0, 850, 638);

        //adaug elementele in fereastra(pasarea, pipe-urile si imaginea de background)
        add(bird.getBirdLabel());
        JPanel pipesPanel = new JPanel();
        for(Pipe p : pipes) {
            add(p.getButtomPipeLabel());
            add(p.getTopPipeLabel());
        }
        add(bgLabel);
        
        addWindowActionListener();
        addPressedEnterListener();
        this.setVisible(true);
    }

    //varianta pentru un singur pipe, nu il folosesc. Sa il sterg cand e gata
//    public View(){
//        setLayout(null);
//        //pack();
//        this.setSize(850, 638);
//        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
//        this.setLocationRelativeTo(null);
//
//        //overlaylayout pentru a putea afisa imagini deasupra altor imagini
//        Container container = this.getContentPane();
//        this.setLayout(new OverlayLayout(container));
//
//        //background
//        ImageIcon image = new ImageIcon("src/flappy-bird-game-background.jpg");
//        bgLabel = new JLabel("", image, JLabel.CENTER);
//        bgLabel.setBounds(0, 0, 850, 638);
//
//
//        //bird
//        Bird bird = new Bird(30, 320);
//        bird.execute();
//        //birdLabel = new JLabel("", bird.getBirdImage(), JLabel.CENTER);
//        //birdLabel.setBounds(bird.getCoordX(), bird.getCoordY(), 100, 100);
//
//        //pipes
//        //Semaphore s = new Semaphore(1);
//        ReentrantLock lock = new ReentrantLock();
//        Pipe pipe = new Pipe(850, lock, lock, lock.newCondition(), lock.newCondition());
//        pipe.execute();
//
//        //to display the bird in front of the background, it is necessarily to add the bird first and then the background (in the frame)
//        add(bird.getBirdLabel());
//        add(pipe.getButtomPipeLabel());
//        add(pipe.getTopPipeLabel());
//        add(bgLabel);
//
//        addWindowActionListener();
//        this.setVisible(true);
//
//    }

    public void addWindowActionListener(){
        bgLabel.addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                bird.birdJump();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void addPressedEnterListener(){
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    bird.birdJump();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public void addPipe(Pipe p){
        this.add(p.getButtomPipeLabel());
        this.add(p.getTopPipeLabel());
    }

//    public static void main(String[] args){
//        View view = new View();
//    }
}
