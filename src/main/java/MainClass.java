public class MainClass {
    public static void main(String[] args){
        FlappyBird game = new FlappyBird();
        Thread thGame = new Thread(game);
        thGame.start();
        //game.execute();
    }
}
