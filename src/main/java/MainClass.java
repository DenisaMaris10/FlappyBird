import javax.swing.*;

public class MainClass {
    public static void main(String[] args){
        FlappyBird game = new FlappyBird();
        game.execute();
//        SwingUtilities.invokeLater((new Runnable() {
//            @Override
//            public void run() {
//                new View();
//            }
//        }));
    }
}
