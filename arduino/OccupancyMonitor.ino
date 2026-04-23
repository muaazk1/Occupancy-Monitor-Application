#include <Servo.h>

Servo myServo;

const int trigPin = 7;
const int echoPin = 8;

// distace threshold in inches
int threshold = 6; 

void setup() {
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  myServo.attach(10);
}

void loop() {
  long duration;

  // trigger the sensor
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // read the echo (timeout after 30000us = ~400 inches)
  duration = pulseIn(echoPin, HIGH, 30000);

  int distanceInches;
  if(duration == 0) {
    distanceInches = 0; 
  } else {
    distanceInches = microsecondsToInches(duration);
  }

  bool occupied = distanceInches < threshold;

  if (occupied) {
    myServo.write(80);
  } else {
    delay(1000);
    myServo.write(10);
  }

  // send serial output
  Serial.print(distanceInches);
  Serial.print(",");
  Serial.print(occupied ? "1" : "0");
  Serial.print(",");
  Serial.println(occupied ? "OPEN" : "CLOSED");

  delay(700); 
}

long microsecondsToInches(long microseconds) {
  return microseconds / 74 / 2;
}
