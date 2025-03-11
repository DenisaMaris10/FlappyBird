import javax.swing.*;

public class GameOverFrame extends JFrame {
    private JLabel gameOverLabel = new JLabel("Game Over!");
    private JLabel scoreLabel;
    private JButton restartButton = new JButton("Restart");
    private JButton exitGameButton = new JButton("Exit Game");
    private JLabel highestScoreLabel;

    public GameOverFrame(int score, int highScore){
        scoreLabel = new JLabel("Your score Was: " + score);
        highestScoreLabel = new JLabel ("High Score: " + highScore);

        setSize(600, 600);
        setLocationRelativeTo(null);

        add(gameOverLabel);
        gameOverLabel.setLocation(80, 10);

        add(scoreLabel);
        scoreLabel.setLocation(40, 50);

        add(highestScoreLabel);
        highestScoreLabel.setLocation(40, 80);

        add(restartButton);
        restartButton.setLocation(40, 120);
        restartButton.setSize(100, 100);

        add(exitGameButton);
        exitGameButton.setLocation(80, 120);
        exitGameButton.setSize(100, 120);

        setVisible(true);
    }
}
