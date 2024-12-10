import java.util.*;
import java.util.Queue;
import java.io.*;
import java.lang.Math;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameStop {
    public static ArrayList<Game> titleOnHand = new ArrayList<>();
    public static HashMap<Game, Integer> inventory = new HashMap<>();
    public static ArrayList<Event> possibleEvents = new ArrayList<>();

    public static int currentEnergy = 50;
    public static double Popularity = 1.0;
    public static int resetEnergy = 50;
    public static double chanceOfEvent = 0;
    public static double profit = 2000.0;

    static class Tick{
        public Tick(){}
    }
    
    static class Game{
        String title;
        double price;
        ArrayList<Game> relatedGames = new ArrayList<>();
    
        public Game(String title, double price){
            this.title = title;
            this.price = price;
        }

        public String toString(){
            return title;
        }
    }

    static class Customer extends Tick{
        Stack<Game> shoppingCart = new Stack<>();
    
        public Customer(){

            int amountOfGames = (int)(Math.random() * 9) + 1;
    
            for(int i = 0; i<amountOfGames; i++){
                int randomGameIndex = (int)(Math.random() * titleOnHand.size());
                Game newGameInCart = titleOnHand.get(randomGameIndex);
                if(inventory.get(newGameInCart) > 0){
                    shoppingCart.push(newGameInCart);
                    inventory.replace(newGameInCart, inventory.get(newGameInCart) - 1);
                }
            }
        }
    }

    static class Event extends Tick{
        int[] changes = new int[2];
        String description;

        public Event(int[] changes, String description){
            this.changes = changes;
            this.description = description;
        }
    }

    static class Day{
        static Queue<Tick> dailyTicks = new LinkedList<Tick>();
        double sales = 0;

        public Day(){
            for(int i = 0; i < 100; i++){
                double chance = (double)(Math.random()*1);
                if(chance>chanceOfEvent){
                    dailyTicks.offer(new Customer());
                }else{
                    int pickEvent = (int)(Math.random() * possibleEvents.size());
                    dailyTicks.offer(possibleEvents.get(pickEvent));
                }
            }
        }

        public static Tick getTick(){
            return dailyTicks.poll();
        }
    }

    static class GreetingGUI implements ActionListener{

        JFrame frame = new JFrame();
        JLabel greetingLabel = new JLabel();
        JButton start = new JButton();
        public GreetingGUI(){
            start.setBounds(900,600,100,50);
            start.addActionListener(this);
            start.setText("Start");
            start.setFocusable(false);
            greetingLabel.add(start);

            ImageIcon logo = new ImageIcon("gamestop-logo.png");
            Image image = logo.getImage();
            Image newimg = image.getScaledInstance(400,120, java.awt.Image.SCALE_SMOOTH);
            logo = new ImageIcon(newimg);

            greetingLabel.setText("Welcome to the GameStop Simulator! Are you ready to start?");
            greetingLabel.setIcon(logo);
            greetingLabel.setHorizontalTextPosition(JLabel.CENTER);
            greetingLabel.setVerticalTextPosition(JLabel.BOTTOM);
            greetingLabel.setForeground(Color.black);
            greetingLabel.setBackground(Color.white);
            greetingLabel.setOpaque(true);
            greetingLabel.setVerticalAlignment(JLabel.CENTER);
            greetingLabel.setHorizontalAlignment(JLabel.CENTER);
            greetingLabel.setBounds(0,0,100,100);
            frame.add(greetingLabel);

            frame.setTitle("GameStop Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1920,1080);
            frame.setVisible(true);

            
            frame.setIconImage(logo.getImage());
            frame.getContentPane().setBackground(Color.white);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==start){
                frame.dispose();
                Day newDay = new Day();
                new HomeScreenGUI(newDay);
            }
        }
    }

    static class HomeScreenGUI implements ActionListener{

        JFrame frame = new JFrame();
        JLabel stats = new JLabel();
        JPanel action = new JPanel();
        JLabel actionLabel = new JLabel();
        JButton helpCustomer = new JButton();
        JButton addInventory = new JButton();
        JButton upgradeStore = new JButton();
        JPanel inventoryPanel = new JPanel();
        JLabel inventoryLabel = new JLabel();
        Day currentDay;

        
        public HomeScreenGUI(Day currentDay){
            this.currentDay = currentDay;
            stats.setPreferredSize(new Dimension(1920, 50));
            stats.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            stats.setText("Energy: " + currentEnergy + "        Popularity: " + Popularity + "      Profit: " + profit);
            stats.setLayout(new FlowLayout());
            frame.add(stats,BorderLayout.NORTH);

            ImageIcon logo = new ImageIcon("gamestop-logo.png");
            Image image = logo.getImage();
            Image newimg = image.getScaledInstance(400,120, java.awt.Image.SCALE_SMOOTH);
            logo = new ImageIcon(newimg);
            Tick currentTick = Day.getTick();

            if(currentTick instanceof Event){
                //frame.dispose();
                //EventGUI(currentDay, currentTick);
            }else{
                actionLabel.setText("What would you like to do?");
                actionLabel.setPreferredSize(new Dimension(1920, 500));
                actionLabel.setVerticalTextPosition(JLabel.TOP);
                actionLabel.setHorizontalTextPosition(JLabel.CENTER);
                actionLabel.setVerticalAlignment(JLabel.CENTER);
                actionLabel.setHorizontalAlignment(JLabel.CENTER);
                //actionLabel.setBackground(Color.green);
                actionLabel.setOpaque(true);

                action.setLayout(new FlowLayout());

                helpCustomer.setPreferredSize(new Dimension(200,100));
                helpCustomer.setText("Help A Customer");
                helpCustomer.addActionListener(this);
                addInventory.setPreferredSize(new Dimension(200,100));
                addInventory.setText("Receive New Inventory");
                addInventory.addActionListener(this);
                upgradeStore.setPreferredSize(new Dimension(200,100));
                upgradeStore.setText("Upgrade Store; Cost: 200");
                if(profit < 200){
                    upgradeStore.setEnabled(false);
                }
                upgradeStore.addActionListener(this);

                inventoryLabel.setText("<HTML>" + "Inventory: <br>" + inventory.toString() + "<br> <HTML>");
                inventoryLabel.setPreferredSize(new Dimension(100, 1080));
                inventoryPanel.add(inventoryLabel);
                frame.add(inventoryPanel, BorderLayout.WEST);

                action.add(actionLabel);
                action.add(helpCustomer);
                action.add(addInventory);
                action.add(upgradeStore);

                frame.add(action, BorderLayout.CENTER);
            }
        

            frame.setTitle("GameStop Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1920,1080);
            frame.setVisible(true);

            
            frame.setIconImage(logo.getImage());
            frame.getContentPane().setBackground(Color.white);
        }
           
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == helpCustomer){
                frame.dispose();
                new TransactionGUI(currentDay);
            }else if(e.getSource() == addInventory){
                currentEnergy-= 3;
                stats.setText("Energy: " + currentEnergy + "        Popularity: " + Popularity + "      Profit: " + profit);
                int amountToAdd = (int)((Math.random()*20) + 1);
                for(int i = 0; i<amountToAdd; i++){
                    int gameToAddIndex = (int)(Math.random() * titleOnHand.size());
                    Game gameToAdd = titleOnHand.get(gameToAddIndex);
                    inventory.put(gameToAdd, inventory.get(gameToAdd) + 1);
                    inventoryLabel.setText("<HTML>" + inventory.toString() + "<HTML>");
                }
                if(currentEnergy < 0){
                    Day newDay = new Day();
                    currentEnergy = resetEnergy;
                    frame.dispose();
                    new HomeScreenGUI(newDay);
                }
            }else if(e.getSource() == upgradeStore){
                profit-= 200;
                resetEnergy += 10;
                Popularity += .2;
                stats.setText("Energy: " + currentEnergy + "        Popularity: " + Popularity + "      Profit: " + profit);
                if(profit < 200){
                    upgradeStore.setEnabled(false);
                }
            }
        }
    }

    static class TransactionGUI implements ActionListener{
        Day currentDay;
        Customer currentCustomer;
        double changeNeeded = round((Math.random()*50),2);
        double currentChange = 0.0;
        double total = 0;

        JFrame frame = new JFrame();
        JLabel stats = new JLabel();
        JPanel transaction = new JPanel();
        JLabel transactionLabel = new JLabel();
        JButton finalize = new JButton();
        JPanel dollars = new JPanel();
        JPanel coins = new JPanel();
        JButton pennies = new JButton();
        JButton nickels = new JButton();
        JButton dimes = new JButton();
        JButton quarters = new JButton();
        JButton ones = new JButton();
        JButton fives = new JButton();
        JButton tens = new JButton();
        JButton twenties = new JButton();
        JLabel bottomTransactionLabel = new JLabel();

        public TransactionGUI(Day currentDay){
            String transactionString = "<HTML>";
            currentCustomer = (Customer)currentDay.getTick();
            stats.setPreferredSize(new Dimension(1920, 50));
            stats.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            stats.setText("Energy: " + currentEnergy + "        Popularity: " + Popularity + "      Profit: " + profit);
            stats.setLayout(new FlowLayout());
            frame.add(stats,BorderLayout.NORTH);

            ImageIcon logo = new ImageIcon("gamestop-logo.png");
            Image image = logo.getImage();
            Image newimg = image.getScaledInstance(400,120, java.awt.Image.SCALE_SMOOTH);
            logo = new ImageIcon(newimg);

            transactionLabel.setPreferredSize(new Dimension(150, 600));

            
            for(int i = 0; i<currentCustomer.shoppingCart.size() + 1; i++){
                Game currentGame = currentCustomer.shoppingCart.pop();
                transactionString = transactionString + currentGame.title + ": " + currentGame.price + "<br>";
                total += currentGame.price;
            }
            total = round(total, 2);
            transaction.setPreferredSize(new Dimension(150, 1080));
            transactionString = transactionString + "<HTML>";
            transactionLabel.setText(transactionString);
            //transactionLabel.setBackground(Color.green);
            transactionLabel.setOpaque(true);

            dollars.setPreferredSize(new Dimension(800, 600));
            ones.setPreferredSize(new Dimension(200, 600));
            fives.setPreferredSize(new Dimension(200, 600));
            tens.setPreferredSize(new Dimension(200, 600));
            twenties.setPreferredSize(new Dimension(200, 600));

            ones.setBackground(new Color(0x85bb65));
            fives.setBackground(new Color(0x85bb65));
            tens.setBackground(new Color(0x85bb65));
            twenties.setBackground(new Color(0x85bb65));

            ones.setText("$1");
            fives.setText("$5");
            tens.setText("$10");
            twenties.setText("$20");
            
            twenties.addActionListener(this);
            tens.addActionListener(this);
            fives.addActionListener(this);
            ones.addActionListener(this);
            

            dollars.add(twenties);
            dollars.add(tens);
            dollars.add(fives);
            dollars.add(ones);

            pennies.setPreferredSize(new Dimension(200, 200));
            nickels.setPreferredSize(new Dimension(200, 200));
            dimes.setPreferredSize(new Dimension(200, 200));
            quarters.setPreferredSize(new Dimension(200, 200));

            pennies.setBackground(new Color(0xCD7F32));
            nickels.setBackground(new Color(0xC0C0C0));
            dimes.setBackground(new Color(0xC0C0C0));
            quarters.setBackground(new Color(0xC0C0C0));

            pennies.setText("¢1");
            nickels.setText("¢5");
            dimes.setText("¢10");
            quarters.setText("¢25");

            quarters.addActionListener(this);
            dimes.addActionListener(this);
            nickels.addActionListener(this);
            pennies.addActionListener(this);
            
            coins.add(quarters);
            coins.add(dimes);
            coins.add(nickels);
            coins.add(pennies);

            coins.setOpaque(true);

            frame.add(coins, BorderLayout.SOUTH);
            frame.add(dollars,BorderLayout.CENTER);

            finalize.setPreferredSize(new Dimension(200,200));

            //bottomTransactionLabel.setBackground(Color.red);
            bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            //bottomTransactionLabel.add(finalize);
            bottomTransactionLabel.setOpaque(true);
            finalize.setText("Finalize");
            if(currentChange != changeNeeded){
                finalize.setEnabled(false);
            }

            finalize.addActionListener(this);
            
            coins.add(finalize);

            bottomTransactionLabel.setPreferredSize(new Dimension(150, 150));

            transaction.add(transactionLabel);

            transaction.add(bottomTransactionLabel,BorderLayout.SOUTH);
            frame.add(transaction,BorderLayout.EAST);

            frame.setTitle("GameStop Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1920,1080);
            frame.setVisible(true);

            frame.setIconImage(logo.getImage());
            frame.getContentPane().setBackground(Color.white);

        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == twenties){
                currentChange += 20.00;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == tens){
                currentChange += 10.00;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == fives){
                currentChange += 5.00;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                } 
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == ones){
                currentChange += 1.00;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == quarters){
                currentChange += 0.25;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == dimes){
                currentChange += 0.10;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == nickels){
                currentChange += 0.05;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else if(e.getSource() == pennies){
                currentChange += 0.01;
                currentChange = round(currentChange,2);
                if(currentChange > changeNeeded){
                    currentChange = 0.0;
                }else if(currentChange == changeNeeded){
                    finalize.setEnabled(true);
                }
                bottomTransactionLabel.setText("<HTML>Total: " + total + "<br> Change needed: " + changeNeeded + "<br> Current Change: " + currentChange + "<HTML>");
            }else{
                profit += total;
                currentEnergy -= 5;
                if(currentEnergy < 0){
                    Day newDay = new Day();
                    currentEnergy = resetEnergy;
                    frame.dispose();
                    new HomeScreenGUI(newDay);
                }else{
                    frame.dispose();
                new HomeScreenGUI(currentDay);
                }  
            }
        }
    }

    static class EventGUI implements ActionListener{

        JFrame frame = new JFrame();
        JLabel eventLabel = new JLabel();
        JButton continueButton = new JButton();
        Event currentEvent;
        Day currentDay;
        JLabel stats = new JLabel();
        
        public EventGUI(Day currentDay, Event currentEvent){
            currentEnergy += currentEvent.changes[0];
            Popularity += currentEvent.changes[1];
            this.currentDay = currentDay;
            this.currentEvent = currentEvent;

            ImageIcon logo = new ImageIcon("gamestop-logo.png");
            Image image = logo.getImage();
            Image newimg = image.getScaledInstance(400,120, java.awt.Image.SCALE_SMOOTH);
            logo = new ImageIcon(newimg);

            frame.setIconImage(logo.getImage());
            frame.getContentPane().setBackground(Color.white);

            stats.setPreferredSize(new Dimension(1920, 50));
            stats.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            stats.setText("Energy: " + currentEnergy + "        Popularity: " + Popularity + "      Profit: " + profit);
            stats.setLayout(new FlowLayout());
            frame.add(stats,BorderLayout.NORTH);

            eventLabel.setPreferredSize(new Dimension(500, 500));
            eventLabel.setText(currentEvent.description + " " + currentEvent.changes[0] + " energy; " + currentEvent.changes[1] + " popularity");
            eventLabel.setVerticalTextPosition(JLabel.CENTER);
            eventLabel.setBackground(Color.green);
            eventLabel.setLayout(new FlowLayout());
            continueButton.setText("Continue");
            continueButton.setPreferredSize(new Dimension(200, 200));
            continueButton.addActionListener(this);
            eventLabel.setOpaque(true);
            frame.add(eventLabel, BorderLayout.CENTER);
            frame.add(continueButton, BorderLayout.SOUTH);

            frame.setTitle("GameStop Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(1920,1080);
            frame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == continueButton){
                frame.dispose();
                new HomeScreenGUI(currentDay);
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException{
        Scanner gameReader = new Scanner(new File("C:\\Users\\Nathan Elliott\\Desktop\\CS 210\\Games.csv"));
        gameReader.useDelimiter(",");
        Game newGame;
        while(gameReader.hasNext()){
            newGame = new Game(gameReader.next(), Double.parseDouble(gameReader.next()));
            titleOnHand.add(newGame);
            inventory.put(newGame, 100);
        }
        gameReader.close();

        Scanner eventReader = new Scanner(new File("C:\\Users\\Nathan Elliott\\Desktop\\CS 210\\Events.csv"));
        eventReader.useDelimiter(",");
        Event newEvent;
        while(eventReader.hasNext()){
            int[] arr = new int[2];
            arr[0] = Integer.parseInt(eventReader.next());
            arr[1] = Integer.parseInt(eventReader.next());
            newEvent = new Event(arr, eventReader.next());
            possibleEvents.add(newEvent);
        }
        eventReader.close();
        new GreetingGUI();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}