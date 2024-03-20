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
    JComboBox city_selection;

    public CityView(CityModel model) {
        super(model, "City", 800);
        // city_env = new CityEnvironment();
        setVisible(true);
        repaint();
    }

    public void setEnv(CityEnvironment env) {
        this.city_env = env;
        city_selection.setSelectedIndex(city_env.getCityType() - 1);
    }

    @Override
    public void initComponents(int width) {
        super.initComponents(width);
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(new JLabel("City:"));
        city_selection = new JComboBox();
        city_selection.addItem("City 1");
        city_selection.addItem("City 2");
        city_selection.addItem("City 3");
        city_selection.addItem("City 4");
        city_selection.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    city_env.initCity(city_selection.getSelectedIndex() - 1);
                }
            }
        });
        p.add(city_selection);
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
