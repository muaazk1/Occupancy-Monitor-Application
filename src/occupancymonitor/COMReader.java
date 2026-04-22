package occupancymonitor;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;

/**
 * Handles serial communication for the Occupancy Monitor application.
 *
 * Opens the specified COM port, continuously reads incoming data from the
 * Arduino, parses sensor values, and forwards the results to a callback
 * supplied by the UI layer. The reader operates on a dedicated background
 * thread to ensure non‑blocking live updates
 * 
 * @author Mu'aaz Khan
 */
public class COMReader {

    private final String portName;
    private final DataCallback callback;
    
    /**
     * Callback interface used to deliver parsed sensor data to the UI layer.
     */
    public interface DataCallback {
        void newData(float distance, boolean occupied, String gateStatus);
    }

    /**
     * Creates a COMReader object bound to a specific port and callback.
     *
     * @param portName the COM port to open
     * @param callback the method who will receive the parsed sensor data
     */
    public COMReader(String portName, DataCallback callback) {
        this.portName = portName;
        this.callback = callback;
    }
    
    /**
    * Opens the serial port and starts the read loop
    */
    public void start() {
        SerialPort port = SerialPort.getCommPort(portName);
        port.setBaudRate(9600);
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (!port.openPort()) {
            System.out.println("Failed to open port.");
            return;
        }

        System.out.println("Port opened successfully!");

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        new Thread(() -> readLoop(port)).start();
    }
    
    /**
     * Continuously reads the input stream bytes from the serial port and uses them
     * to assemble full lines. Forwards them for parsing after.
     */
    private void readLoop(SerialPort port) {
        InputStream in = port.getInputStream();
        byte[] buffer = new byte[1024];
        StringBuilder line = new StringBuilder();

        while (true) {
            try {
                int bytes = in.read(buffer);
                if (bytes > 0) {
                    for (int i = 0; i < bytes; i++) {
                        char c = (char) buffer[i];
                        if (c == '\n') {
                            processLine(line.toString().trim());
                            line.setLength(0);
                        } else if (c != '\r') {
                            line.append(c);
                        }
                    }
                }
                Thread.sleep(10);
            } catch (Exception e) {
                System.out.println("Serial read error: " + e.getMessage());
            }
        }
    }

    /**
     * Parses a CSV line from the Arduino and delivers the values to the
     * callback.
     *
     * @param line the raw message string
     */
    private void processLine(String line) {
        try {
            if (line.isEmpty()) return;

            String[] parts = line.split(",");
            if (parts.length < 3) return;

            float distance = Float.parseFloat(parts[0].trim());
            boolean occupied = parts[1].trim().equals("1");
            String gate = parts[2].trim();

            distance = Math.min(distance, 300);

            callback.newData(distance, occupied, gate);

        } catch (Exception e) {
            System.out.println("Error parsing: " + line);
        }
    }
}
