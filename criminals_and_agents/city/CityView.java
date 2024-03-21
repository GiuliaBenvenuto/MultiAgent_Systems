package city;

import jason.environment.grid.GridWorldView;

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

import city.CityEnvironment;

public class CityView extends GridWorldView {

    CityEnvironment city_env = null;
    CityModel city_model = null;
    JComboBox city_selection;
    private JRadioButton city1Button, city2Button, city3Button, city4Button;
    private ButtonGroup cityGroup;

    public CityView(CityModel model) {
        super(model, "City", 800);
        this.city_model = model;
        // city_env = new CityEnvironment();
        setVisible(true);
        repaint();
    }

    public void setEnv(CityEnvironment env) {
        this.city_env = env;
        // TODO: set the index of the city type
        if (city_env != null) {
            // Get the current city type from the environment (assuming a getter method exists)
            int currentCityType = city_env.getCityType();
            System.out.println("Current city type: " + currentCityType);
            // Update the selected index (0-based indexing)
            // city_selection.setSelectedIndex(currentCityType - 1);
        }
    }

    public void updateView(CityModel model) {
        this.city_model = model;
        repaint();
    }


//    @Override
//    public void initComponents(int width) {
//        super.initComponents(width);
//        JPanel p = new JPanel();
//        p.setLayout(new FlowLayout());
//        p.add(new JLabel("City:"));
//        city_selection = new JComboBox();
//        city_selection.addItem("City 1");
//        city_selection.addItem("City 2");
//        city_selection.addItem("City 3");
//        city_selection.addItem("City 4");
//        city_selection.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED && city_env != null) {
//                    // Adjust the index to match the city type (1-indexed)
//                    city_env.initCity(city_selection.getSelectedIndex() + 1);
//                    //city_selection.setSelectedIndex(city_selection.getSelectedIndex());
//                    //city_selection.repaint();
//                    System.out.println("City selected CITYVIEW: " + (city_selection.getSelectedIndex() + 1));
//                    System.out.println("City selected CITYVIEW INDEX: " + (city_selection.getSelectedIndex()));
//
//                }
//            }
//        });
//        p.add(city_selection);
//        getContentPane().add(BorderLayout.NORTH, p);
//    }
@Override
public void initComponents(int width) {
    super.initComponents(width);
    JPanel p = new JPanel();
    p.setLayout(new FlowLayout());
    p.add(new JLabel("City:"));

    // Create a ButtonGroup to group the radio buttons
    ButtonGroup cityGroup = new ButtonGroup();

    // Create radio buttons for each city
    JRadioButton city1Button = new JRadioButton("City 1");
    JRadioButton city2Button = new JRadioButton("City 2");
    JRadioButton city3Button = new JRadioButton("City 3");
    JRadioButton city4Button = new JRadioButton("City 4");

    // Add the radio buttons to the ButtonGroup
    cityGroup.add(city1Button);
    cityGroup.add(city2Button);
    cityGroup.add(city3Button);
    cityGroup.add(city4Button);

    // Add an ActionListener to the radio buttons
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JRadioButton source = (JRadioButton) e.getSource();
            System.out.println("Action performed on: " + source.getText()); // Debug print statement
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

    // Assign the ActionListener to the radio buttons
    city1Button.addActionListener(actionListener);
    city2Button.addActionListener(actionListener);
    city3Button.addActionListener(actionListener);
    city4Button.addActionListener(actionListener);

    // Add the radio buttons to the panel
    p.add(city1Button);
    p.add(city2Button);
    p.add(city3Button);
    p.add(city4Button);

    // Set a default selection if desired
    city1Button.setSelected(true);

    getContentPane().add(BorderLayout.NORTH, p);
}



    @Override
    public void draw(Graphics g, int x, int y, int object) {
        switch (object) {
        case CityModel.JAIL:
            drawJail(g, x, y);
            break;
        }
    }

    public void drawJail(Graphics g, int x, int y) {
        g.setColor(Color.RED);
        g.fillRect(x * cellSizeW, y * cellSizeH, cellSizeW, cellSizeH);
    }

}
