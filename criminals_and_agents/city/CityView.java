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

import city.CityEnvironment;

public class CityView extends GridWorldView {
    CityEnvironment city_env = null;

    public CityView(CityModel model) {
        super(model, "City", 800);
        city_env = new CityEnvironment();
        setVisible(true);
        repaint();
    }

    public void setEnv(CityEnvironment env) {
        this.city_env = env;
    }

}
