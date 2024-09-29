import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class View extends JFrame {//implements ActionListener{
    private Image bgImage = Toolkit.getDefaultToolkit().getImage("C:\\temp\\Facultate\\Proiecte\\Flappy_Bird\\FlappyBird\\src\\flappy-bird-game-background.jpg");
    private JLabel bgLabel;
    private Bird bird;
    private ArrayList<Pipe> pipes;
    private JTextField scoreText;
    private JButton startGameButton;
    private FlappyBird flappyBird;

    public View(){

        setLayout(null);
        this.setSize(850, 638);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        startGameButton = new JButton("Start Game");
        startGameButton.setActionCommand("Start Game");
        //startGameButton.addActionListener(this);
        add(startGameButton);
        startGameButton.setLocation(420, 315);
        View copyView = this;
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameButton.setVisible(false);
                startGameButton.setEnabled(false);
                flappyBird = new  FlappyBird();
                flappyBird.execute();
            }
        });

        //overlaylayout pentru a putea afisa imagini deasupra altor imagini
        Container container = this.getContentPane();
        this.setLayout(new OverlayLayout(container));

        //background
        ImageIcon image = new ImageIcon("src/flappy-bird-game-background.jpg");
        bgLabel = new JLabel("", image, JLabel.CENTER);
        bgLabel.setBounds(0, 0, 850, 638);
        add(bgLabel);

        addWindowActionListener();
        addPressedEnterListener();

        this.setVisible(true);
    }

    public View(ArrayList<Pipe> pipe, Bird b, JTextField score){

        this.bird = b;
        this.pipes = pipe;
        scoreText = score;
        scoreText.setSize(50, 20);
        scoreText.setEditable(false);
        scoreText.setEnabled(false);

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

        //scoreLabel = new JLabel("Score: ");
        //scoreLabel.setBounds(0, 0, 20, 20);
        //scoreLabel.setLocation(420, 50);

        //adaug elementele in fereastra(pasarea, pipe-urile si imaginea de background)
        add(bird.getBirdLabel());
        add(scoreText);
        for(Pipe p : pipe) {
            add(p.getButtomPipeLabel());
            add(p.getTopPipeLabel());
        }
        add(bgLabel);
        addWindowActionListener();
        addPressedEnterListener();
        this.setVisible(true);
    }

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
                    System.out.println("Space");
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

//    public void setScore(int score){
//        scoreText.setText("Score: " + score);
//    }

    public JTextField getScoreText() {
        return scoreText;
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if("Start Game".equals(e.getActionCommand())){
//            startGameButton.setVisible(false);
//            startGameButton.setEnabled(false);
//            flappyBird = new  FlappyBird(this);
//            flappyBird.execute();
//        }
//    }

    public void startGame(Bird bird, ArrayList<Pipe> pipes, JTextField score){
        this.bird = bird;
        this.pipes = pipes;

        scoreText = score;
        scoreText.setSize(50, 20);
        scoreText.setEditable(false);
        scoreText.setEnabled(false);


        add(bird.getBirdLabel());
        add(scoreText);
        for(Pipe p : pipes) {
            add(p.getButtomPipeLabel());
            add(p.getTopPipeLabel());
        }
        add(bgLabel);

    }

//    public static void main(String[] args){
//        SwingUtilities.invokeLater(new Runnable(){
//            public void run(){
//                new View();
//            }
//        });
//    }
}
