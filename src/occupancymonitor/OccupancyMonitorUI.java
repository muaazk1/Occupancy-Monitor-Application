package occupancymonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Graphical user interface for the Occupancy Monitor application.
 *
 * Builds and manages the Swing based UI, displaying live sensor data
 * received from the Arduino via the COMReader. This class is responsible for
 * layout, component initialization, and updating the interface in response to
 * incoming data or interactions from the user. 
 *
 * @author Mu'aaz Khan
 */
public class OccupancyMonitorUI {

    private JLabel distanceLabel;
    private JLabel statusLabel;
    private JLabel gateLabel;
    private JLabel currentModeLabel;

    private JPanel controlPanel;

    public OccupancyMonitorUI() {
        buildUI();
    }
    
    /**
     * Builds and configures the main application UI, including layout,
     * labels, and container panels.
     *
     */
    private void buildUI() {
        JFrame frame = new JFrame("Occupancy Monitor");
        frame.setSize(900, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        distanceLabel = new JLabel("Distance: -- in");
        statusLabel = new JLabel("Status: --");
        gateLabel = new JLabel("Gate: --");
        currentModeLabel = new JLabel("Current Mode:");

        Font bold = new Font("Arial", Font.BOLD, 20);
        distanceLabel.setFont(bold);
        statusLabel.setFont(bold);
        gateLabel.setFont(bold);
        currentModeLabel.setFont(bold);

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        distanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dataPanel.add(Box.createVerticalStrut(50));
        dataPanel.add(distanceLabel);
        dataPanel.add(Box.createVerticalStrut(100));
        dataPanel.add(statusLabel);
        dataPanel.add(Box.createVerticalStrut(100));
        dataPanel.add(gateLabel);
        dataPanel.add(Box.createVerticalStrut(100));

        JPanel modePanel = buildModePanel();

        controlPanel = new JPanel();

        frame.add(dataPanel);
        frame.add(modePanel);
        frame.add(currentModeLabel);
        frame.add(Box.createVerticalStrut(20));
        frame.add(controlPanel);

        frame.setVisible(true);
    }

    /**
     * Responsible for building the Access Mode section of the UI, including Auto/Manual controls 
     * and listeners that update the interface based on user interaction.
     *
     * @return a JPanel containing the mode selection controls
     */
    private JPanel buildModePanel() {
        JPanel panel = new JPanel();

        JLabel modeLabel = new JLabel("Access Mode:");
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JCheckBox autoBtn = new JCheckBox("Auto");
        JCheckBox manualBtn = new JCheckBox("Manual");
        JButton allowButton = new JButton("Allow");

        Font bold = new Font("Arial", Font.BOLD, 20);
        autoBtn.setFont(bold);
        manualBtn.setFont(bold);

        ButtonGroup group = new ButtonGroup();
        group.add(autoBtn);
        group.add(manualBtn);

        manualBtn.addItemListener(e -> {
            boolean manual = e.getStateChange() == ItemEvent.SELECTED;
            currentModeLabel.setText("Current Mode: " + (manual ? "Manual" : "Auto"));

            if (manual) {
                controlPanel.add(allowButton);
            } else {
                controlPanel.remove(allowButton);
            }

            controlPanel.revalidate();
            controlPanel.repaint();
        });

        autoBtn.addActionListener(e -> currentModeLabel.setText("Current Mode: Auto"));

        panel.add(modeLabel);
        panel.add(autoBtn);
        panel.add(manualBtn);

        return panel;
    }
    
    /**
     * Updates the UI with new sensor data. Uses SwingUtilities.invokeLater() to
     * ensure thread‑safe updates from the COMReader background thread.
     *
     * @param distance the measured distance from the sensor
     * @param occupied true if the monitored area is occupied, false otherwise
     * @param gateStatus the current gate status string, either opened or closed.
     */
    public void updateData(float distance, boolean occupied, String gateStatus) {
        SwingUtilities.invokeLater(() -> {
            distanceLabel.setText("Distance: " + distance + " in");
            statusLabel.setText("Status: " + (occupied ? "Occupied" : "Empty"));
            statusLabel.setForeground(occupied ? Color.RED : Color.GREEN);
            gateLabel.setText("Gate: " + gateStatus);
        });
    }
}
