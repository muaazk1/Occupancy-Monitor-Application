# Occupancy Monitor & Gate Controller

Embedded gate automation system built with Java Swing and Arduino.  
Uses an **HC-SR04 ultrasonic sensor** for distance detection and a **multithreaded, thread-safe serial pipeline** (jSerialComm) for real-time data transfer, enabling servo-based gate control with automatic and manual override modes.

---

## Features

- **Real-time distance monitoring**
  - HC-SR04 ultrasonic sensor measures distance to approaching objects.
  - Live distance values streamed from Arduino to Java over serial.

- **Gate control logic**
  - Servo-based gate mechanism (open/close).
  - **Automatic mode:** gate reacts to distance thresholds.
  - **Manual override mode:** user can force open/close via UI controls.

- **Multithreaded serial communication**
  - Uses **jSerialComm** for robust serial I/O.
  - Background thread for reading sensor data without blocking the UI.
  - Thread-safe UI updates using `SwingUtilities.invokeLater`.

- **Java Swing UI**
  - Real-time display of distance readings.
  - Visual indication of gate state (open/closed).
  - Mode selection: automatic vs manual.

---

## Tech Stack

- **Languages:** Java, Arduino C/C++
- **Desktop UI:** Java Swing
- **Hardware:**
  - Arduino Uno
  - HC-SR04 ultrasonic sensor
  - Servo motor (for gate)
- **Libraries & Tools:**
  - [jSerialComm](https://fazecast.github.io/jSerialComm/)
  - NetBeans
  - Arduino IDE

---

## Project Structure

```text
Occupancy-Monitor-Application/
├─ src/occupancymonitor/        # Java source files (UI, logic, serial pipeline)
├─ arduino/                     # Arduino firmware (.ino)
├─ nbproject/                   # NetBeans project metadata
├─ build.xml                    # Ant build script
└─ manifest.mf                  # Java manifest (main class, metadata)
