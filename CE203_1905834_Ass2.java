import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.sql.*;

/*
    Main class that initialize the new frame.
*/
public class CE203_1905834_Ass2 {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Frame frame = new Frame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

/*
    Main frame class.
*/
class Frame extends JFrame {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // Get screen size.
    public int WIDTH = screenSize.width;    // Define width of the frame.
    public int HEIGHT = screenSize.height;  // Define height of the frame.

    Frame() {
        setResizable(false);
        setTitle("Reg.no.1905834");
        setFocusable(true);
        requestFocus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        getContentPane().add(new TitlePanel(this));
    }

}

/*
    Three panels of the game.

    TitlePanel is the first panel that appears when the program starts, it displays the title,
    a brief introduction, instruction and buttons start and exit.

    GamePanel is the panel on which the game takes place.

    GameOver panel appears when the game round is over, it shows the ending text,
    short instruction, player's scores and two buttons: MENU and EXIT.
*/
class TitlePanel extends JPanel {
    public Frame frame;  // Linker with the main frame.

    private int posX;     // Two variables that define white border rectangle position on the frame.
    private int posY;

/*
    Set of strings printed on the panel.
*/
    private String title = "CREATIVE NAME FOR COVID GAME";

    private String s1 = "During the pandemic it is important";
    private String s2 = "to maintain social distance";
    private String s3 = "and personal hygiene to reduce";
    private String s4 = "the risk of virus transmission.";

    private String s5 = "Keep distance from     ,";
    private String s6 = "collect    to sanitize";
    private String s7 = "and reduce the risk of infection.";

/*
    New custom buttons.
*/
    public StartButton startButton;
    public ExitButton exitButton;

/*
    New Passer and Sanitizer instances printed on the panel in instruction text.
*/
    private Passer p = new Passer(793,345,30);
    private Sanitizer s = new Sanitizer(690,380,20);

/*
    Constructor that sets the color of the panel's background,
    adds start snd exit buttons at specific places and sizes to the frame.
    Also adds MouseListeners to buttons.
*/
    TitlePanel(Frame appF) {
        this.frame = appF;

        posX = (int)(frame.WIDTH * 0.362);
        posY = (int)(frame.HEIGHT * 0.1);

        setBackground(Color.BLACK);

        startButton = new StartButton("START", posX + 100, posY + 370,200,30);
        startButton.addMouseListener(new MouseHandler(frame, startButton));
        frame.add(startButton);

        exitButton = new ExitButton("EXIT", posX + 100,posY + 410,200,30);
        exitButton.addMouseListener(new MouseHandler(frame, exitButton));
        frame.add(exitButton);

    }

/*
    Draw a custom graphic of the panel.
*/
    public void draw(Graphics g) {

        // Draw white border rectangle.
        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY,  400, 500);

        // Set font for title.
        Font fontTitle = new Font("Symbol", Font.BOLD, 37);
        FontMetrics metrTitle = this.getFontMetrics(fontTitle);
        g.setFont(fontTitle);

        // Draw white title background.
        g.fillRect((int)((frame.WIDTH - metrTitle.stringWidth(title)) * 0.5), (int)(frame.HEIGHT * 0.179 - metrTitle.getHeight()),  654, 50);

        // Draw title shade.
        g.setColor(new Color(36, 36, 36, 196));
        g.drawString(title, (int)((frame.WIDTH - metrTitle.stringWidth(title)) * 0.508), (int)(frame.HEIGHT * 0.18));

        // Draw green title.
        g.setColor(new Color(54, 191, 0));
        g.drawString(title, (int)((frame.WIDTH - metrTitle.stringWidth(title)) * 0.51), (int)(frame.HEIGHT * 0.179));

        // Draw white text introduction.
        g.setColor(new Color(245, 243, 240));
        Font fontIn = new Font("Courier", Font.PLAIN, 16);
        FontMetrics metrInfo = this.getFontMetrics(fontIn);
        g.setFont(fontIn);
        g.drawString(s1, (int)((frame.WIDTH - metrInfo.stringWidth(s1)) * 0.5), (int)(frame.HEIGHT * 0.27));
        g.drawString(s2, (int)((frame.WIDTH - metrInfo.stringWidth(s2)) * 0.5), (int)(frame.HEIGHT * 0.27) + metrInfo.getHeight() + 5);
        g.drawString(s3, (int)((frame.WIDTH - metrInfo.stringWidth(s3)) * 0.5), (int)(frame.HEIGHT * 0.27) + (metrInfo.getHeight() + 5) * 2);
        g.drawString(s4, (int)((frame.WIDTH - metrInfo.stringWidth(s4)) * 0.5), (int)(frame.HEIGHT * 0.27) + (metrInfo.getHeight() + 5) * 3);

        // Draw red text instruction.
        g.setColor(new Color(179, 24, 24));
        g.drawString(s5, (int)((frame.WIDTH - metrInfo.stringWidth(s5)) * 0.5), (int)(frame.HEIGHT * 0.29) + (metrInfo.getHeight() + 5) * 5);
        g.drawString(s6, (int)((frame.WIDTH - metrInfo.stringWidth(s6)) * 0.5), (int)(frame.HEIGHT * 0.29) + (metrInfo.getHeight() + 5) * 6);
        g.drawString(s7, (int)((frame.WIDTH - metrInfo.stringWidth(s7)) * 0.5), (int)(frame.HEIGHT * 0.29) + (metrInfo.getHeight() + 5) * 7);

        g.dispose();

    }

/*
    Paint the panel with declared graphics:
    Passer, Sanitizer, startButton, exitButton and graphic of the panel.
*/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        p.draw(g);
        s.draw(g);

        startButton.draw(g);
        exitButton.draw(g);

        draw(g);

        repaint();
        revalidate();

    }

}
class GamePanel extends JPanel  {
    public Frame frame;             // Linker with the main frame.

    public int borderWidth = 350;   // Game boundaries.
    public int borderHeight = 100;

/*
    New custom interactive buttons.
*/
    public MenuButton menuButton;
    public PauseButton pauseButton;
    public ResumeButton resumeButton;

    public int score = 0;             // Initial value of the score. Stores current score of the round. Used and changed by the scoreCounter attribute.
    public final int maxHealth = 400; // Maximum size of the health bar.
    public Random r = new Random();   // Random value for random text printed by Sign.
    public int threshold = 50;        // Initial score threshold.

    public Hero hero;                 // Initiates Hero at initial position.
    public InfectionBar infectionBar; // Initiates InfectionBar at initial position with score value.
    public Sign sign;                 // Initiates Sign at initial position.

    public ArrayList<Passer> listP = new ArrayList<Passer>();           // Stores new Passer instances.
    public ArrayList<Sanitizer> listS = new ArrayList<Sanitizer>();     // Stores new Sanitizer instances.

    public Timer scoreCounter = new Timer(100, new TimerHandler(this,1));   // Responsible for counting player's score.
    public Timer speed = new Timer(100, new TimerHandler(this,2));          // Responsible for the speed at which instances are printed.

/*
    Constructor that sets the color of the panel background,
    adds KeyHandler which allows Hero to move around the panel,
    starts both timers, adds three custom buttons at specific places and sizes to the frame.
    Also adds MouseListeners to the buttons.
*/
    GamePanel (Frame appF) {
        this.frame = appF;

        setBackground(Color.black);
        frame.addKeyListener(new KeyboardHandler(this));

        hero = new Hero((frame.WIDTH / 2) - 30,((2 * frame.HEIGHT) / 3) - 30,40);
        infectionBar = new InfectionBar(0,0, maxHealth, score, frame.WIDTH);
        sign = new Sign(1140,400,40, r.nextInt(7));

        scoreCounter.start();
        speed.start();

        pauseButton = new PauseButton("PAUSE", 935,20,30,30);
        pauseButton.addMouseListener(new MouseHandler(this, pauseButton));
        frame.add(pauseButton);

        resumeButton = new ResumeButton("RESUME", 980,20,30,30);
        resumeButton.addMouseListener(new MouseHandler(this, resumeButton));
        frame.add(resumeButton);

        menuButton = new MenuButton("MENU", 1020,20,100,30);
        menuButton.addMouseListener(new MouseHandler(frame, menuButton));
        frame.add(menuButton);

    }

/*
    Paint the panel with declared graphics:
    Hero, instances of Passer, instances of Sanitizer, InfectionBar, Sign,
    pauseButton, menuButton and resumeButton.
*/
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        hero.draw(g);
        for (Passer passer : listP) {
            passer.draw(g);
        }
        for (Sanitizer sanitizer : listS) {
            sanitizer.draw(g);
        }
        infectionBar.draw(g);

        pauseButton.draw(g);
        menuButton.draw(g);
        resumeButton.draw(g);

        sign.draw(g);

        repaint();
        revalidate();
    }

/*
    Check whether Hero keeps the distance from Passers. If Passer is in Hero's
    distance area the health bar is decreasing. If the health bar's size drop to
    zero, both timers stop, KeyListener and current panel are removed
    and new GameOver panel is added to the frame.
*/
    public void checkSocialDistance(int distance) {

        int heroX1 = hero.getPosX() - distance;
        int heroX2 = heroX1 + hero.getSize() + (2 * distance);
        int heroY1 = hero.getPosY() - distance;
        int heroY2 = heroY1 + hero.getSize() + (2 * distance);

        for (Passer passer : listP) {
            int passerX1 = passer.getPosX();
            int passerX2 = passerX1 + passer.getSize();
            int passerY1 = passer.getPosY();
            int passerY2 = passerY1 + passer.getSize();

            // Check if Hero and Passer overlap.
            if (((passerX1 >= heroX1 && passerX1 <= heroX2) || (passerX2 >= heroX1 && passerX2 <= heroX2))
                    && ((passerY1 >= heroY1 && passerY1 <= heroY2) || (passerY2 >= heroY1 && passerY2 <= heroY2))) {

                infectionBar.setSize(infectionBar.getSize()-1);
            }
        }
        // Check if the health bar is empty.
        if (infectionBar.getSize() <= 0){
            scoreCounter.stop();
            speed.stop();

            removeKeyListener(new KeyboardHandler(this));

            frame.getContentPane().removeAll();
            frame.getContentPane().add(new GameOver(this, frame));
        }

    }

/*
    Check whether Hero caught the Sanitizer. If Hero was in the field of Sanitizer,
    Sanitizer is moved outside of the frame and health bar recharges by a certain amount.
*/
    public void checkCatch() {

        int heroX1 = hero.getPosX();
        int heroX2 = heroX1 + hero.getSize();
        int heroY1 = hero.getPosY();
        int heroY2 = heroY1 + hero.getSize();

        for (Sanitizer sanitizer : listS) {
            int sanitizerX1 = sanitizer.getPosX();
            int sanitizerX2 = sanitizerX1 + sanitizer.getSize();
            int sanitizerY1 = sanitizer.getPosY();
            int sanitizerY2 = sanitizerY1 + sanitizer.getSize();

            // Check if Hero and Sanitizer overlap.
            if (((sanitizerX1 >= heroX1 && sanitizerX1 <= heroX2) || (sanitizerX2 >= heroX1 && sanitizerX2 <= heroX2))
                    && ((sanitizerY1 >= heroY1 && sanitizerY1 <= heroY2) || (sanitizerY2 >= heroY1 && sanitizerY2 <= heroY2))) {

                sanitizer.setPosX(frame.WIDTH);
                sanitizer.setPosY(frame.HEIGHT);

                infectionBar.setSize(infectionBar.getSize() + 80);
            }
        }
        // Prevent the health bar from overfilling.
        if (infectionBar.getSize() > maxHealth){
            infectionBar.setSize(infectionBar.getSize() - (infectionBar.getSize() - maxHealth));
        }

    }

/*
    Check whether the Hero has exceeded the boundary.
*/
    public void checkHeroBounds() {

        if (hero.posX < borderWidth){
            hero.setPosX(borderWidth);
        }
        if (hero.posX > frame.WIDTH - borderWidth){
            hero.setPosX(frame.WIDTH - borderWidth);
        }
        if (hero.posY < borderHeight){
            hero.setPosY(borderHeight);
        }
        if (hero.posY > frame.HEIGHT - 2 * borderHeight){
            hero.setPosY(frame.HEIGHT - 2 * borderHeight);
        }

    }

/*
    Check whether the current score has exceeded a given threshold. If so, the delay is reduced (the game speeds up).
*/
    public void controlSpeed() {

        if (speed.getDelay() > 20) {
            if (score == threshold) {
                speed.setDelay(speed.getDelay() - 5);
                threshold = threshold + 50;
            }
        }
        if (speed.getDelay() <= 20) {
            if (speed.getDelay() == 5) {
                speed.setDelay(5);
            }
            if (score == threshold) {
                speed.setDelay(speed.getDelay() - 1);
                threshold = threshold + 175;
            }
        }

    }

}
class GameOver extends JPanel {
    public Frame frame;         // Linker to the main frame.
    public GamePanel game;      // Linker to the GamePlay panel.

    public int score;     // Hold the current score.

    private int posX;     // Two variables that define white border position on the frame.
    private int posY;

/*
    New custom buttons.
 */
    public RestartButton restartButton;
    public MenuButton menuButton;
    public ExitButton exitButton;

/*
    Set of strings printed on the panel.
*/
    private String gameOverText = "GAME OVER";
    private String infectedText = "YOU ARE INFECTED";
    private String infoText = "STAY AT HOME";

    private ArrayList scoresList;       // Hold all scores from text file.
    private ArrayList bestScores;       // Hold five best scores of all time.

/*
    Constructor that sets the color of the panel background,
    calls methods that write to and receive scores from text file,
    adds custom buttons in specific places and sizes to the frame.
    Also adds MouseListener to buttons.
*/
    GameOver(GamePanel appG, Frame appF) {
        this.game = appG;
        this.frame = appF;
        this.score = game.infectionBar.score;

        setBackground(Color.BLACK);

        posX = (int)(frame.WIDTH * 0.362);
        posY = (int)(frame.HEIGHT * 0.1);

        try {
            printScoreToFile(score, "scores.txt");
            getBestScores("scores.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        restartButton = new RestartButton("TRY AGAIN", posX + 100, posY + 340, 200, 30);
        restartButton.addMouseListener(new MouseHandler(frame,restartButton));
        frame.add(restartButton);

        menuButton = new MenuButton("MENU", posX + 100,posY + 380,200,30);
        menuButton.addMouseListener(new MouseHandler(frame, menuButton));
        frame.add(menuButton);

        exitButton = new ExitButton("EXIT", posX + 100,posY + 420,200,30);
        exitButton.addMouseListener(new MouseHandler(frame, exitButton));
        frame.add(exitButton);

        // databaseScoreStorage(score);
    }

/*
    printScoreToFile gets the score and writes it to the specified text file.
*/
    public void printScoreToFile(int score, String fileName) throws IOException {
            FileWriter fileOutput = new FileWriter(fileName, true);
            BufferedWriter b = new BufferedWriter(fileOutput);
            String scoreString = String.valueOf(score);
            b.write(scoreString);
            b.newLine();
            b.close();
            fileOutput.close();

    }

/*
    getBestScores creates two new lists, one to hold scores from a text file, second to hold five best scores.
    It uses Scanner to retrieve the scores stored in a text file and puts them into a scoresList,
    sorts the list, reverses its order and puts five first items from scoresList into bestScores.
 */
    public void getBestScores(String fileName) throws FileNotFoundException {
        scoresList = new ArrayList();
        bestScores = new ArrayList();

        Scanner input = new Scanner(new File(fileName));
        String currentLine;
        int point;
        while(input.hasNextLine()){
            currentLine = input.nextLine();
            String curData = currentLine;
            point = Integer.parseInt(curData);
            scoresList.add(point);
        }
        input.close();

        Collections.sort(scoresList);
        Collections.reverse(scoresList);

        for(int i = 1; i <= 5 ; i++){
            if (scoresList.size() <= i){
                bestScores.add(scoresList.get(i-1));
                break;
            }
            bestScores.add(scoresList.get(i-1));
        }

    }

/*
    Draw a custom graphic of the panel.
*/
    public void draw(Graphics g) {

        // Draw white border rectangle.
        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY,  400, 500);

        // Draw grey title shadow.
        g.setColor(Color.DARK_GRAY);
        Font fontTitle = new Font("Symbol", Font.BOLD, 50);
        FontMetrics metrTitle = this.getFontMetrics(fontTitle);
        g.setFont(fontTitle);
        g.drawString(gameOverText, (int)((frame.WIDTH - metrTitle.stringWidth(gameOverText)) * 0.508), (int)(frame.HEIGHT * 0.22));

        // Draw red title text.
        g.setColor(new Color(227, 0, 0));
        g.drawString(gameOverText, (int)((frame.WIDTH - metrTitle.stringWidth(gameOverText)) * 0.51), (int)(frame.HEIGHT * 0.219));

        // Draw red information texts.
        g.setColor(new Color(179, 24, 24));
        Font fontInfo = new Font("Courier", Font.PLAIN, 20);
        FontMetrics metrInfo = this.getFontMetrics(fontInfo);
        g.setFont(fontInfo);
        g.drawString(infectedText, (int)((frame.WIDTH - metrInfo.stringWidth(infectedText)) * 0.5), (int)(frame.HEIGHT * 0.27));
        g.drawString(infoText, (int)((frame.WIDTH - metrInfo.stringWidth(infoText)) * 0.5), (int)(frame.HEIGHT * 0.29));

        // Draw white scores display.
        g.setColor(new Color(245, 243, 240));
        Font fontScores = new Font("Courier", Font.PLAIN, 17);
        g.setFont(fontScores);
        FontMetrics metrScores = this.getFontMetrics(fontScores);
        String scoreText = "TOTAL SCORE:    ";
        g.drawString(scoreText, (int)((frame.WIDTH - metrScores.stringWidth(scoreText)) * 0.49), (int)(frame.HEIGHT * 0.33));

        // Draw red current score.
        g.setColor(Color.RED);
        String currentScore = String.valueOf(score);
        g.drawString(currentScore, (int)((frame.WIDTH - metrScores.stringWidth(scoreText)) * 0.5) + metrScores.stringWidth(scoreText) - metrScores.stringWidth(currentScore), (int)(frame.HEIGHT * 0.33));

        // Draw white best scores.
        g.setColor(new Color(245, 243, 240));
        String topScores = "TOP SCORES:";
        g.drawString(topScores, (int)((frame.WIDTH - metrScores.stringWidth(scoreText)) * 0.49), (int)(frame.HEIGHT * 0.33) + (metrScores.getHeight()));
        String value;
        for (int i = 0; i < bestScores.size(); i++) {
            value = String.valueOf(bestScores.get(i));
            g.drawString(value, (int)((frame.WIDTH - metrScores.stringWidth(scoreText)) * 0.5) - metrScores.stringWidth(currentScore) + metrScores.stringWidth(scoreText), (int)(frame.HEIGHT * 0.33) + (metrScores.getHeight()) * (i+1));
        }

        g.dispose();

    }

/*
    Paint the panel with declared graphics:
    restartButton, menuButton, exitButton and graphic of the panel.
*/
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        restartButton.draw(g);
        menuButton.draw(g);
        exitButton.draw(g);

        draw(g);

        repaint();
        revalidate();
    }

/*
    Player score storage method for a database extension.
*/
/*
    static void databaseScoreStorage (int score){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scores?autoReconnect=true&useSSL=false", "root", "password");
        } catch (ClassNotFoundException e) {
            System.exit(1);
        } catch (SQLException e) {
            System.exit(1);
        }

        PreparedStatement preparedStatement = null;
        int playerID = 1;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO scores VALUES (?,?)");
            preparedStatement.setInt(1, playerID);
            preparedStatement.setInt(2, score);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.exit(1);
        }

        try {
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.exit(1);
        }
    }
*/

}

/*
    Abstract class for game objects.
*/
abstract class GameObject {
    protected int posX, posY, size;

    GameObject(int posX, int posY, int size){
        this.posX = posX;
        this.posY = posY;
        this.size = size;
    }

    public abstract void setPosX(int posX);
    public abstract void setPosY(int posY);
    public abstract void setSize(int size);

    public abstract int getPosX();
    public abstract int getPosY();
    public abstract int getSize();

    public abstract void draw(Graphics g);

}

class Sanitizer extends GameObject{

    Sanitizer(int posX, int posY, int size){
        super(posX, posY, size);
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(int posY) {
        this.posY = posY;
    }
    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }
    @Override
    public int getSize() {
        return size;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        // Draw pink round rectangle.
        g.setColor(new Color(224, 125, 208));
        g.fillRoundRect(posX, posY, size, size, size/2,size/3);

        // Draw black bottle cork shape.
        g.setColor(Color.black);
        g.drawRect(posX + size/2 - size/6, posY - size/6, size/3, (int)(size/2.5));

        // Draw white bottle cork.
        g.setColor(Color.white);
        g.fillRect(posX + size/2 - size/6, posY - size/6, size/3, (int)(size/2.5));

    }

    // Change object's position in y-axis. Allow object to "move" down on the screen.
    public void moveDown() {
        posY = posY + size/5;
    }

}
class Hero extends GameObject {

    Hero(int posX, int posY, int size){
        super(posX,posY,size);
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(int posY) {
        this.posY = posY;
    }
    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }
    @Override
    public int getSize() {
        return size;
    }


    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        // Draw yellow circle.
        g.setColor(new Color(255, 252, 20));
        g.fillOval(posX, posY, size, size);

        // Draw black eye.
        g.setColor(Color.black);
        g.fillOval((int) (posX + size/2.5 - size/5), (int)(posY + size/2.5 - size/5), size/5, size/5);

        // Draw blue mask.
        g.setColor(new Color(13, 192, 224));
        g.fillArc(posX, posY, size, size, 170, 80);
        g.fillArc(posX, posY, size, size, 330, 5);
        g.fillArc(posX, posY, size, size, 30, 5);

        // Draw black mask features.
        g.setColor(Color.black);
        g.fillArc(posX, posY, size, size, 170, 2);
        g.fillArc(posX, posY, size, size, 190, 2);
        g.fillArc(posX, posY, size, size, 210, 2);
        g.fillArc(posX, posY, size, size, 230, 2);
        g.fillArc(posX, posY, size, size, 250, 2);

    }

    //Four methods that enables object to move in specified direction.
    public void moveRight() {
        posX = posX + (3 * size)/4;
    }
    public void moveLeft() {
        posX = posX - (3 * size)/4;
    }
    public void moveDown() {
        posY = posY + (3 * size)/4;
    }
    public void moveUp() {
        posY = posY - (3 * size)/4;
    }

}
class Passer extends GameObject {

    Passer(int posX, int posY, int size) {
        super(posX,posY,size);
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(int posY) {
        this.posY = posY;
    }
    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }
    @Override
    public int getSize() {
        return size;
    }


    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        // Draw green circle shape.
        g.setColor(new Color(53, 198, 53));
        g.fillOval(posX, posY, size, size);

        // Draw black eyes.
        g.setColor(Color.black);
        g.fillOval((int) (posX + size - size/2.5), (int)(posY + size/2.5 - size/5), size/5, size/5);
        g.fillOval((int) (posX + size/2.5 - size/5), (int)(posY + size/2.5 - size/5), size/5, size/5);

        // Draw white teeth.
        g.setColor(Color.WHITE);
        g.fillRect(posX + (int)(size/4.27), posY + size/2, (int)(size/1.8), size/4);

        // Draw red mouth.
        g.setColor(new Color(205, 69, 69));
        g.fillRect(posX + (int)(size/4.27), posY + (int)(size/1.7), (int)(size/1.8), size/9);

    }

    // Change object's position in y-axis. Allows object to "move" down on the screen.
    public void moveDown() {
        posY = posY + size/5;
    }

}
class InfectionBar extends GameObject {
    public final int maxHealth = 400;   // Maximum size of the health bar.
    public int score;                   // Store current score.
    private int width;                  // Width of the infection bar.

    InfectionBar(int posX, int posY, int size, int score, int width){
        super(posX, posY, size);
        this.score = score;
        this.width = width;
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(int posY) {
        this.posY = posY;
    }
    @Override
    public void setSize(int size) {
        this.size = size;
    }
    public void setScore(int score){
        this.score = score;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }
    @Override
    public int getSize() {
        return size;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        // Draw black background rectangle.
        g.setColor(Color.BLACK);
        g.fillRect(posX,posY,width,70);

        // Draw red infection bar fill.
        g.setColor(Color.RED);
        g.fillRoundRect(500, posY+30, size,15,5,5);

        // Draw white infection bar border.
        g.setColor(new Color(245, 243, 240));
        g.drawRect(500, posY+30, maxHealth ,15);

        // Draw white current score display.
        Font font = new Font("Courier", Font.PLAIN, 23);
        g.setFont(font);
        String scoreString = "SCORE:" + score;
        g.drawString(scoreString, 350, font.getSize() + 22);

    }

}
class Sign extends GameObject {
    String[] texts = new String[7]; // Array that stores texts on the sign.
    int textNumber;                 // Number of the text to be printed on sign.

    Sign(int posX, int posY, int size, int textNumber) {
        super(posX, posY, size);
        this.textNumber = textNumber;

        texts[0] = "   SHOP THIS WAY";
        texts[1] = "     HAIRDRESSER";
        texts[2] = "    TOILET PAPER";
        texts[3] = "    GET VACCINES";
        texts[4] = "   YOU GOT THIS!";
        texts[5] = "        PHARMACY";
        texts[6] = "      SANITIZERS";
    }

    @Override
    public void setPosX(int posX) {
        this.posX = posX;
    }
    @Override
    public void setPosY(int posY) {
        this.posY = posY;
    }
    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getPosX() {
        return posX;
    }
    @Override
    public int getPosY() {
        return posY;
    }
    @Override
    public int getSize() {
        return size;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        // Draw white road sign shape.
        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY,size * 5, size);
        g.drawRect(posX + 90, posY + size, size / 2,size * 2);

        // Draw string on the sign.
        Font font = new Font("Courier", Font.PLAIN, 18);
        g.setFont(font);
        g.drawString(texts[textNumber], posX, posY + 25);

        // Draw white arrow.
        int[] xPoints = {posX + 13, posX + 21, posX + 29};
        int[] yPoints = {posY + 20, posY + 8, posY + 20};
        g.drawPolygon(xPoints, yPoints,3);
        g.drawRect(posX + 19, posY + 20, 4,10);

    }

}

/*
    Abstract class for customized interactive buttons.
*/
abstract class InteractiveButton extends JComponent {
    protected int posX, posY, width, height;
    protected String name;

    InteractiveButton(String name, int posX, int posY, int width, int height){
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        setLocation(posX,posY);
        setSize(width,height);
    }

    public abstract String getName();

    public abstract void draw(Graphics g);

}

class StartButton extends InteractiveButton {

    StartButton(String name, int posX, int posY, int width, int height) {
        super(name, posX, posY, width, height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY, width, height);

        Font fontStart = new Font("Courier", Font.PLAIN, 23);
        FontMetrics metr = this.getFontMetrics(fontStart);
        g.setFont(fontStart);
        g.drawString(name, (int)((posX + width) - metr.stringWidth(name) * 1.88), posY + metr.getHeight()-2);
    }

}
class PauseButton extends InteractiveButton {

    PauseButton(String name, int posX, int posY, int width, int height) {
        super(name, posX, posY, width, height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));

        g.fillRect(posX,posY,width/3,height);
        g.fillRect(posX + width - width/3, posY,width/3, height);

    }

}
class ResumeButton extends InteractiveButton {

    ResumeButton(String name, int posX, int posY, int width, int height) {
        super(name, posX, posY, width, height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));

        int[] xPoints = {posX,posX,posX+width};
        int[] yPoints = {posY,posY+height,posY+height/2};
        g.fillPolygon(xPoints,yPoints,3);

    }

}
class MenuButton extends InteractiveButton {

    MenuButton(String name, int posX, int posY, int width, int height) {
        super(name, posX, posY, width, height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY, width, height);

        Font fontMenu = new Font("Courier", Font.PLAIN, 23);
        FontMetrics metr = this.getFontMetrics(fontMenu);
        g.setFont(fontMenu);
        g.drawString(name, posX + (width - metr.stringWidth(name))/2, posY + metr.getHeight()-3);

    }

}
class ExitButton extends InteractiveButton {

    ExitButton(String name, int posX, int posY, int width, int height) {
        super(name, posX, posY, width, height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY, width, height);

        Font fontExit = new Font("Courier", Font.PLAIN, 23);
        FontMetrics metr = this.getFontMetrics(fontExit);
        g.setFont(fontExit);
        g.drawString(name, posX + (width - metr.stringWidth(name))/2, posY + metr.getHeight()-3);

    }

}
class RestartButton extends InteractiveButton {

    RestartButton(String name, int posX, int posY, int width, int height){
        super(name,posX,posY,width,height);
    }

    // Return name of the object.
    @Override
    public String getName(){
        return name;
    }

    // Draw a custom graphic of the object.
    @Override
    public void draw(Graphics g) {

        g.setColor(new Color(245, 243, 240));
        g.drawRect(posX, posY, width, height);

        Font fontRestart = new Font("Courier", Font.PLAIN, 23);
        FontMetrics metr = this.getFontMetrics(fontRestart);
        g.setFont(fontRestart);
        g.drawString(name, posX + (width - metr.stringWidth(name))/2, posY + metr.getHeight()-3);

    }

}

class KeyboardHandler implements KeyListener {
    private GamePanel game;     // Linker to the GamePanel.

    KeyboardHandler(GamePanel appG){
        this.game = appG;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Depending on the pressed key, a different method is called that allows to change the Hero's position on the panel.
        if (key == KeyEvent.VK_LEFT) {
            game.hero.moveLeft();
            game.checkHeroBounds();
        }
        if (key == KeyEvent.VK_RIGHT) {
            game.hero.moveRight();
            game.checkHeroBounds();
        }
        if (key == KeyEvent.VK_UP) {
            game.hero.moveUp();
            game.checkHeroBounds();
        }
        if (key == KeyEvent.VK_DOWN) {
            game.hero.moveDown();
            game.checkHeroBounds();
        }

    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}
class MouseHandler implements MouseListener {
    private Frame frame;                // Linker to the main frame.
    private GamePanel game;             // Linker to the GamePanel.
    private InteractiveButton button;   // Linker to the InteractiveButton.

    MouseHandler(Frame appF, InteractiveButton appB) {
        this.frame = appF;
        this.button = appB;
    }
    MouseHandler(GamePanel appG, InteractiveButton appB) {
        this.game = appG;
        this.button = appB;
    }

    // Different actions are triggered depending on which custom button was pressed by the mouse.
    @Override
    public void mousePressed(MouseEvent e) {

        // Corresponds to StartButton. "Start the game", remove everything from the frame and add new GamePanel panel to the frame.
        if (button.getName().equals("START")) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            frame.getContentPane().add(new GamePanel(frame));
        }

        // Corresponds to MenuButton. Remove everything from the frame and add new TitlePanel panel to the frame.
        if (button.getName().equals("MENU")) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            frame.getContentPane().add(new TitlePanel(frame));
        }

        // Corresponds to ExitButton. Close application.
        if (button.getName().equals("EXIT")) {
            java.lang.System.exit(0);
        }

        // Corresponds to PauseButton. Stop timers.
        if (button.getName().equals("PAUSE")) {
            game.scoreCounter.stop();
            game.speed.stop();
        }

        // Corresponds to ResumeButton. Restart timers.
        if (button.getName().equals("RESUME")) {
            game.scoreCounter.restart();
            game.speed.restart();
        }

        // Corresponds to RestartButton. Restart the game.
        if (button.getName().equals("TRY AGAIN")) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            frame.getContentPane().add(new GamePanel(frame));
        }

    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

}
class TimerHandler implements ActionListener {
    private GamePanel g;             // Linker to the GamePanel.
    private int number;              // Number of action to perform.
    private Random r = new Random(); // Used in the speed attribute so that instances can be printed at random x-axis on the panel.

    TimerHandler(GamePanel g, int number) {
        this.g = g;
        this.number = number;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        g.controlSpeed();

    /*
        Count player's score. Pass score value to the infectionBar so it can be printed on the panel.
    */
        if (number == 1) {
            g.score++;
            g.infectionBar.setScore(g.score);
        }
    /*
        Create new instances of Passer and Sanitizer with random value of x-axis position and add these instances to lists.
        The condition checks if random fits within the width of the frame. If not, the instance is not created.
        It is done to avoid the overflow of lists with instances that will not be visible and keep the speed with which instances are printed.
        For very instance from lists call methods moveDown (that changes y-axis position of the instance), checkSocialDistance and checkCatch.
    */
        if (number == 2) {
            int p = r.nextInt(5000);
            if ((p < (g.frame.WIDTH - g.borderWidth)) && (p > g.borderWidth)){
                g.listP.add(new Passer(p,0,30));
            }
            for (Passer passer : g.listP) {
                passer.moveDown();
            }

            int s = r.nextInt(150000);
            if ((s < (g.frame.WIDTH - g.borderWidth)) && (s > g.borderWidth)){
                g.listS.add(new Sanitizer(s, 0, 25));
            }
            for (Sanitizer sanitizer : g.listS) {
                sanitizer.moveDown();
            }
            g.checkSocialDistance(30);
            g.checkCatch();
        }

    }

}