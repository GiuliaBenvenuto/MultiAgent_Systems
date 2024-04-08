package city;

import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// Icon
import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import city.CityEnvironment;


public class CityView extends GridWorldView {

    CityEnvironment city_env = null;
    CityModel city_model = null;


    private JComboBox city_selection;
    private JRadioButton city1Button, city2Button, city3Button, city4Button;
    private ButtonGroup cityGroup;

    // Icons for agents
    ImageIcon icon;
    private Image policeImage;
    private Image civilianImage;
    private Image clueImage;
    private Image criminalImage;
    private Image jailImage;
    private Image obstacleImage;


    public CityView(CityModel model) {
        super(model, "City", 900);
        this.city_model = model;

        icon = new ImageIcon("images/police.png");
        policeImage = icon.getImage();
        icon = new ImageIcon("images/man.png");
        civilianImage = icon.getImage();
        icon = new ImageIcon("images/green_clue.png");
        clueImage = icon.getImage();
        icon = new ImageIcon("images/criminal.png");
        criminalImage = icon.getImage();
        icon = new ImageIcon("images/jail.png");
        jailImage = icon.getImage();
        icon = new ImageIcon("images/brick-wall.png");
        obstacleImage = icon.getImage();


        setVisible(true);
        repaint();
    }

    public void setEnv(CityEnvironment env) {
        this.city_env = env;
        if (city_env != null) {
            int currentCityType = city_env.getCityType();
            System.out.println("Current city type: " + currentCityType);
        }
    }

    public void updateView(CityModel model) {
        this.city_model = model;
        repaint();
    }


    // RadioButtons for city selection
    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("City:"));

        // ButtonGroup
        ButtonGroup cityGroup = new ButtonGroup();

        // RadioButton for each city
        JRadioButton city1Button = new JRadioButton("City 1");
        JRadioButton city2Button = new JRadioButton("City 2");
        JRadioButton city3Button = new JRadioButton("City 3");
        JRadioButton city4Button = new JRadioButton("City 4");

        cityGroup.add(city1Button);
        cityGroup.add(city2Button);
        cityGroup.add(city3Button);
        cityGroup.add(city4Button);

        // ActionListener to the radio buttons
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JRadioButton source = (JRadioButton) e.getSource();

                int index = -1;
                if (source == city1Button) {
                    index = 1;
                } else if (source == city2Button) {
                    index = 2;
                } else if (source == city3Button) {
                    index = 3;
                } else if (source == city4Button) {
                    index = 4;
                }
                if (index != -1 && city_env != null) {
                    city_env.initCity(index);
                    System.out.println("City selected CITYVIEW: " + index);
                }
            }
        };

        city1Button.addActionListener(actionListener);
        city2Button.addActionListener(actionListener);
        city3Button.addActionListener(actionListener);
        city4Button.addActionListener(actionListener);

        // Add RadioButtons to the panel
        p.add(city1Button);
        p.add(city2Button);
        p.add(city3Button);
        p.add(city4Button);

        city1Button.setSelected(true);
        getContentPane().add(BorderLayout.NORTH, p);
    }



    @Override
    public void draw(Graphics g, int x, int y, int object) {
        super.draw(g, x, y, object);

        //System.out.println("Drawing");
        switch (object) {
            case CityModel.OBSTACLE:
                drawObstacle(g, x, y);
                break;
            case CityModel.JAIL:
                drawJail(g, x, y);
                break;
        }
    }

    public void drawJail(Graphics g, int x, int y) {
        //g.setColor(Color.red);
        //g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        g.drawImage(jailImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
    }

    public void drawObstacle(Graphics g, int x, int y) {
        //g.setColor(Color.GRAY);
        //g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        g.drawImage(obstacleImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        // Remove default drawing of agents
        g.setColor(getBackground());
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);

        // Color of agents based on their type
        if (city_model.hasObject(city_model.CLUE_AGENT, x, y)) {
            // CLUE_AGENT
            //g.setColor(Color.GREEN);
            //g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.drawImage(clueImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        } else if (city_model.hasObject(city_model.POLICE_AGENT, x, y)) {
            // POLICE_AGENT
            //g.setColor(Color.ORANGE);
            //g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.drawImage(policeImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        } else if (city_model.hasObject(city_model.CIVILIAN_AGENT, x, y)) {
            // CIVILIANL_AGENT
            //g.setColor(Color.BLUE);
            //g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.drawImage(civilianImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        } else if (city_model.hasObject(city_model.CRIMINAL_AGENT, x, y)) {
            // CRIMINAL_AGENT
            //g.setColor(Color.CYAN);
            //g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            g.drawImage(criminalImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        else {
            // Default agent
            g.setColor(c);
            g.fillOval(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        }
    }


}
