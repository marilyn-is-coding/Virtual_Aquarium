/*    
******** About the Aquarium.java file ********
* 
* This file contains the Aquarium class which utilizes various Swing (GUI toolkit) components
* There is one JButton that allows the user to add another fish to the tank when it is pressed.
* There are 3 JRadioButtons. These give the user the ability to pick which color the
* next fish added will be. They can be red, yellow, or purple.
* There are 2 JLabels. One which displays the count of fish in the tank.
* The other JLabel informs the user about the Night Mode functionality.
* 
* The class Tank is a JPanel in the Aquarium.java file
* An instance of Tank is made in the Aquarium class and added to a JFrame.
* This JPanel includes all of the stationary background elements of the tank (water, gravel, plants) and
* an ArrayList of Fish objects. Fish is a nested class of the Tank.
* The water, gravel and plants are all made with Rectangle objects, whose draw method uses a simple
* Graphics2D fillRect() method. The Fish class has a more complicated draw method that combines rectangles and ovals
* that change with fish directionality.
* The Tank JPanel has an overridden paintComponent method that draws the elements in the right order. 
* Finally the Tank Jpanel has an animate method that repaints the Tank periodically.
   
*/

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.util.Random;

public class Aquarium {

    JFrame window;
    JButton addFishButton;
    JLabel counter;
    JLabel nightText;
    Tank t1;
    ButtonListener fishButtonListener;
    JRadioButton optionRed;
    JRadioButton optionYellow;
    JRadioButton optionPurple;
    ButtonGroup bg1;
    int fishcount = 1;
    MouseRightClicker mrc1;
    int threadms = 10;  //for tank light is on
    int threadmsfast = 3; //for Night Mode
    
    //Constructor for an Aquarium includes the frame, all the components, listeners, and Tank JPanel
    public Aquarium(){
        
        JFrame window = new JFrame();
        window.setSize(1000,1000);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addFishButton = new JButton();
        addFishButton.setText("Add fish!");
        addFishButton.setBounds (200,40, 150, 20);
        fishButtonListener = new ButtonListener();
        addFishButton.addActionListener(fishButtonListener);

        //RadioButton Listeners
        RadioListenerRed red1 = new RadioListenerRed();
        RadioListenerYellow yellow1 = new RadioListenerYellow();
        RadioListenerPurple purple1 = new RadioListenerPurple();
        //RadioButtons for new fish color optiions (Red, Yellow, or Purple)
        optionRed = new JRadioButton("Red fish");
        optionRed.addActionListener(red1);
        optionRed.setBounds(400,40,100,20);
        optionYellow = new JRadioButton("Yellow fish");
        optionYellow.setBounds(500,40,120,20);
        optionYellow.addActionListener(yellow1);
        optionPurple = new JRadioButton("Purple fish");
        optionPurple.setBounds(630,40,100,20);
        optionPurple.addActionListener(purple1);
        
        bg1 = new ButtonGroup();
        bg1.add(optionRed);
        bg1.add(optionYellow);
        bg1.add(optionPurple);

        //an instance of the right clicker, will turn on Night Mode
        mrc1 = new MouseRightClicker();

        //JLabel to keep track of how many fish are in the tank
        counter = new JLabel();
        counter.setText("Number of fish:" + fishcount);
        counter.setBounds(200,100,200,50);
        
        nightText = new JLabel();
        nightText.setText("Right click to toggle Night Mode");
        nightText.setBounds(200, 200, 300, 50);

        //making a tank object which is a JPanel, adding to the window frame
        Tank t1 = new Tank();
        window.add(t1); 
        
        //adding the components to the JPanel
        
        t1.setLayout(null);
        t1.add(addFishButton);
        t1.add(optionRed);
        t1.add(optionYellow);
        t1.add(optionPurple);
        t1.add(counter);
        t1.add(nightText);
        t1.addMouseListener(mrc1); 
        t1.revalidate();
        t1.repaint();
        t1.animate();
    }

    public class ButtonListener implements ActionListener
    //an actionlistener for when the Add Fish! button is pressed
    {
        public void actionPerformed(ActionEvent e)
        {
            //when the button is pressed, a fish is spawned at a random location in the tank, and the fish count is increased
            Random random = new Random();
            int x_random = random.nextInt(200,740);
            int y_random = random.nextInt(300, 580);
            Tank.fishes.add(new Tank.Fish(x_random, y_random));
            fishcount ++;
            counter.setText("Number of fish: " + fishcount);
        }
    }
    public class RadioListenerRed implements ActionListener
    {   //RadioButton Red will change the static default color to red for new Fish
        public void actionPerformed(ActionEvent e)
        {
            Tank.Fish.fishColour = Color.RED;
        }
    }
    public class RadioListenerYellow implements ActionListener
    {   //RadioButton Yellow will change the static default color to a custom yellow for new Fish
        public void actionPerformed(ActionEvent e)
        {
            Tank.Fish.fishColour = new Color (218, 165, 32);
        }
    }
    public class RadioListenerPurple implements ActionListener
    {   //RadioButton Purple will change the static default color to a custom purple for new Fish
        public void actionPerformed(ActionEvent e)
        {
            Tank.Fish.fishColour = new Color(160, 32, 240);
        }
    }

    public class MouseRightClicker implements MouseListener
    {   
        @Override
        public void mouseClicked(MouseEvent m)
        {   //Right clicking in the panel will toggle the tankLight variable
            if (m.getButton() == MouseEvent.BUTTON3)
            {
                Tank.tankLight = !Tank.tankLight;
            }
        }
        public void mouseEntered(MouseEvent m){}
        public void mouseExited(MouseEvent m){}
        public void mousePressed(MouseEvent m){}
        public void mouseReleased(MouseEvent m){}
    }

    public class Tank extends JPanel
    {   //the Tank is a JPanel that we can draw things on
        private Rectangle water;
        private Rectangle gravel;
        private Rectangle blackScreen;
        private ArrayList<Rectangle> plants = new ArrayList<Rectangle>();
        static boolean tankLight = true;
        static ArrayList<Fish> fishes = new ArrayList<Fish>();

        public Tank()
        {   //Constructor for a tank includes water, gravel, plants, an initial first fish, and a blackScreen for Night Mode
            water = new Rectangle(200,300, 600,400, new Color(153,204,255));
            gravel = new Rectangle (200,700, 600,100, new Color (245,222,179));
            Rectangle plant1 = new Rectangle(250,500, 30, 200, new Color(6, 64, 43));
            Rectangle plant2 = new Rectangle(300,520, 15, 180, new Color(6, 64, 43));
            Rectangle plant3 = new Rectangle(550,580, 20, 120, new Color(6, 64, 43));
            Rectangle plant4 = new Rectangle(600,520, 18, 180, new Color(6, 64, 43));
            Rectangle plant5 = new Rectangle(750,400, 10, 300, new Color(6, 64, 43));
            plants.add(plant1);
            plants.add(plant2);
            plants.add(plant3);
            plants.add(plant4);
            plants.add(plant5);
            Fish firstFish = new Fish(500,500);
            fishes.add(firstFish);
            blackScreen = new Rectangle(200,300,600,500, new Color(0,0,0, 210));
        }

        public class Rectangle 
        { //Reusuable rectangle class with customizable parameters (xy coordinates, dimensions, and colour)        
            int x, y;
            Color c1;
            int width;
            int height;
            public Rectangle (int x, int y, int width, int height, Color colour)
            {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                c1 = colour;
            }
            public void draw(Graphics g)
            {   //Rectangles are drawn with the custom parameters
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(c1);
                g2d.fillRect(x, y, width, height);
            }
        }

        public static class Fish
        {
            //fish objects have their default color as Red but instances are changed with JRadioButtons
            static Color fishColour = Color.RED;

            //Creating a green-ish (glow-in-the-dark) color. Each fish has a chance to express slightly different blue hues
            static Random r2  = new Random();
            Color glowColour = new Color(50,205,r2.nextInt(256));

            //instance variable for the fish color depends on what the current default static fishColour is.
            Color thisfishcolor;
            int x, y, dx, dy;
            int [] directions = {-1, 1};

            public Fish(int x, int y)
            { //Constructor for fish object
                this.x = x;
                this.y = y;
                thisfishcolor =fishColour;
                dx = directions [(int) (Math.random()*directions.length)];
                dy = directions [(int) (Math.random()*directions.length)]; //gets a random number from 0-1 
            }
            public void draw(Graphics g)
            {   //draw method for Fish
                // coordinates for fish objects are updated as they are repainted
                x+=dx;
                y+=dy;
                //keep fish inside the water area of the tank
                if (x<= 200) {dx *= -1;}
                if (x >= (800 - 60)) {dx*= -1;}
                if (y <= 300) {dy *= -1;}
                if (y>= (700-10)) {dy *=-1;}

                Graphics2D g2d = (Graphics2D) g;
                
                if (dx >=0)
                {   //shapes and colors of fish in the moving right position
                    if(!tankLight)
                    {g2d.setColor(Color.GREEN);}
                    else
                        {g2d.setColor(Color.ORANGE);}
                    g2d.fillRoundRect(x, y, 20, 20, 2,2);
                    if (!tankLight)
                    {g2d.setColor(glowColour);}
                    else
                    {g2d.setColor(thisfishcolor);}
                    g2d.fillOval(x+20, y+5, 40, 10);
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(x+50, y+7, 5, 5);
                }
                if (dx <0)
                {    //shapes and colors of fish in the moving left position
                if(!tankLight)
                    {g2d.setColor(Color.GREEN);}
                    else
                        {g2d.setColor(Color.ORANGE);}
                    g2d.fillRoundRect(x+40, y, 20, 20, 2,2);
                    if (!tankLight)
                    {g2d.setColor(glowColour);}
                    else
                    {g2d.setColor(thisfishcolor);}
                    g2d.fillOval(x, y+5, 40, 10);

                g2d.setColor(Color.WHITE);
                g2d.fillOval(x+10, y+7, 5, 5);
                }
            } //end of Fish draw method
        } //end of Fish class

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            water.draw(g);
            gravel.draw(g);
            for (Rectangle plant:plants)
            {
                plant.draw(g);
            }
            //the blackScreen is drawn only if the tankLight of the aquarium is false (not on)
            if (!tankLight)
            {
                blackScreen.draw(g);
            }
            //fish are drawn in front of the black screen because they are glow-in-the-dark
            for (Fish fish: fishes){
                fish.draw(g);
            }
        }

        public void animate()
        {   //continuing updating the tank as time passes
            while (true)
            {
                try {
                    //Night Mode is party mode so fish are fast
                    if (!tankLight)
                    Thread.sleep(threadmsfast);
                    else
                    {
                    //tankLight is on, so the fish are reasonably swimming
                    Thread.sleep(threadms);
                    }
                }
                catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                }
                this.repaint();
            }
        }
    }
}
