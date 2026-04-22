package occupancymonitor;

/**
 * Entry point for the Occupancy Monitor application.
 *
 * Initializes the user interface and starts the COMReader which is responsible for
 * receiving real‑time sensor data from the Arduino over the serial port.
 *
 * This class combines the UI layer and the serial communication module.
 *
 * @author Mu'aaz Khan
 */
public class OccupancyMonitor {

    public static void main(String[] args) {

        OccupancyMonitorUI ui = new OccupancyMonitorUI();

        COMReader reader = new COMReader("COM5", ui::updateData);
        reader.start();
    }
}
