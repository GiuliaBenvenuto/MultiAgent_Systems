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
import java.util.Set;
import java.util.HashSet;


import city.CityEnvironment;



/**  ---------- CITY VIEW CLASS ----------
 * This class extends GridWorldView and provides the view for visualizing
 * the simulation of the city environment.
 * This class is responsible for rendering the elements of the city, such as agents with their icons
 * (police, civilians, criminals, clue agents), obstacles, and other structures like jails.
 *
 *
 * The class has some responsibilities, including:
 * - Drawing different types of agents with distinct icons to represent their roles within the simulation.
 *   -> If a police agent is simply exploring the city, it is represented by a police icon.
 *   -> If a police agent is escorting a criminal, it is represented by a different icon.
 *
 * - Representing environmental features like obstacles (wall icon) and jail, enhancing
 *   the visual distinction between different cell types on the grid.
 *
 * - Dynamically updating the view based on changes in the model, ensuring that the state of the simulation
 *   is reflected. This includes re-drawing the grid when agents move or the environment changes.
 *   (This is the reason why sometimes there is flickering in the GUI)
 */

public class CityView extends GridWorldView {

    CityEnvironment city_env = null;
    CityModel city_model = null;

    // Icons for agents, obstacles and jail
    ImageIcon icon;
    private Image policeImage;
    private Image policeEscortingImage;
    private Image civilianImage;
    private Image clueImage;
    private Image criminalImage;
    private Image jailImage;
    private Image jailImage_1;
    private Image jailImage_2;
    private Image obstacleImage;
    private Image houseImage;

    // Method to create the view
    public CityView(CityModel model) {
        super(model, "City Environment", 700);
        this.city_model = model;

        // Police agent icon
        icon = new ImageIcon("images/police.png");
        policeImage = icon.getImage();
        icon = new ImageIcon("images/police_escorting.png");
        policeEscortingImage = icon.getImage();

        // Civilian agent icon
        icon = new ImageIcon("images/man.png");
        civilianImage = icon.getImage();

        // Clue agent icon
        icon = new ImageIcon("images/green_clue.png");
        clueImage = icon.getImage();

        // Criminal agent icon
        icon = new ImageIcon("images/criminal.png");
        criminalImage = icon.getImage();

        // Jail icons
        icon = new ImageIcon("images/jail.png");
        jailImage = icon.getImage();
        icon = new ImageIcon("images/1_criminal_jail.png");
        jailImage_1 = icon.getImage();
        icon = new ImageIcon("images/2_criminal_jail.png");
        jailImage_2 = icon.getImage();

        // Obstacle icons
        icon = new ImageIcon("images/brick-wall.png");
        obstacleImage = icon.getImage();
        icon = new ImageIcon("images/house.png");
        houseImage = icon.getImage();

        setVisible(true);
        repaint();
    }


    // Method to set the environment
    public void setEnv(CityEnvironment env) {
        this.city_env = env;
        if (city_env != null) {
            int currentCityType = city_env.getCityType();
            // System.out.println("Current city type: " + currentCityType);
        }
    }


    // Method to update the view
    public void updateView(CityModel model) {
        this.city_model = model;
        repaint();
    }


    // Method to draw the grid and its elements
    @Override
    public void draw(Graphics g, int x, int y, int object) {
        super.draw(g, x, y, object);

        switch (object) {
            case CityModel.OBSTACLE:
                drawObstacle(g, x, y);
                break;
            case CityModel.JAIL:
                drawJail(g, x, y);
                break;
        }
    }


    // Method to draw the jail
    public void drawJail(Graphics g, int x, int y) {
        int arrested_criminals = city_model.getArrestedCriminals();
        // One criminal arrested
        if (arrested_criminals == 1) {
            g.drawImage(jailImage_1, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        // Two criminals arrested
        else if (arrested_criminals == 2) {
            g.drawImage(jailImage_2, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        // No criminals arrested
        else {
            g.drawImage(jailImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
    }


    // Method to draw the obstacles (walls and houses)
    public void drawObstacle(Graphics g, int x, int y) {
        // Define special coordinates in which to draw houses
        int[] xCoords = {25, 27, 29};
        int[] yCoords = {14, 12, 10};
        Set<String> specialCoords = new HashSet<>();

        for (int xCoord : xCoords) {
            for (int yCoord : yCoords) {
                specialCoords.add(xCoord + "," + yCoord);
            }
        }
        // Determine which image to use
        Image currentImage = specialCoords.contains(x + "," + y) ? houseImage : obstacleImage;
        // Draw
        g.drawImage(currentImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
    }


    // Method to draw the agents depending on their type and role
    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        g.setColor(getBackground());
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);

        // Draw clue agent
        if (city_model.hasObject(city_model.CLUE_AGENT, x, y)) {
            g.drawImage(clueImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        // Draw police agent
        else if (city_model.hasObject(city_model.POLICE_AGENT, x, y)) {
            boolean isEscorting = city_model.isEscorting(id);
            boolean atJail = city_model.isPoliceAtJail(id);
            // If the agent is at jail and not escorting, draw cell background
            if (atJail && !isEscorting) {
                g.setColor(getBackground());
                g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
            }
            // If the agent is escorting and not at jail, draw the escorting image
            else if (isEscorting && !atJail) {
                g.drawImage(policeEscortingImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
            }
            // If the agent is not at jail and not escorting, draw the normal police image
            else if (!atJail) {
                g.drawImage(policeImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
            }
        }
        // Draw civilian agent
        else if (city_model.hasObject(city_model.CIVILIAN_AGENT, x, y)) {
            g.drawImage(civilianImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        // Draw criminal agent
        else if (city_model.hasObject(city_model.CRIMINAL_AGENT, x, y)) {
            g.drawImage(criminalImage, x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH, this);
        }
        // Default: draw cell background
        else {
            g.setColor(getBackground());
            g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
        }
    }

} // CityView
