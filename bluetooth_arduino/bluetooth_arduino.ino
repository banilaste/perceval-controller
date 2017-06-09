#include <SoftwareSerial.h>

/**
 * Classe de gestion du moteur
 */
class Motor {
  public:
    static const int FORWARD = 1, BACKWARD = 0;
  
    Motor(int sens1, int sens2, int speedPin);
    void init();
    void setDirection(int sens);
    void setSpeed(int vitesse);
  private:
    int sens1, sens2, speedPin;
};

/**
 * Constructeur
 */
Motor::Motor(int sens1, int sens2, int speedPin) :
    sens1(sens1), sens2(sens2), speedPin(speedPin) {}

/**
 * Initialisation des pins en sortie
 */
void Motor::init() {
  pinMode(sens1, OUTPUT);
  pinMode(sens2, OUTPUT);  
  pinMode(speedPin, OUTPUT);
}

/**
 * Changement de la vitesse
 */
void Motor::setSpeed(int vitesse) {
  analogWrite(speedPin, vitesse);
}

/**
 * Changement du sens
 */
void Motor::setDirection(int sens) {
  digitalWrite(sens1, sens);
  digitalWrite(sens2, 1 - sens);
}

// Déclaration en global des constantes de classe
const int Motor::FORWARD;
const int Motor::BACKWARD;


// RX sur le pin 0 et TX sur le pin 1
SoftwareSerial bluetooth(0, 1);

// Moteurs et leurs pins associés sur le pont en H
Motor moteurGauche(2, 3, 10), moteurDroit(4, 5, 11);

// Caractère lu du bluetooth
char readChar;

void setup() {
  // Initialisation des connections séries
  Serial.begin(9600);
  bluetooth.begin(115200);

  // Initialisation des moteurs
  moteurDroit.init();
  moteurGauche.init();

  // On met la vitesse à 0
  moteurDroit.setSpeed(0);
  moteurGauche.setSpeed(0);

  // On avance
  moteurDroit.setDirection(Motor::FORWARD);
  moteurGauche.setDirection(Motor::FORWARD);
  
}

void loop() {
  // On attend de pouvoir lire
  if (bluetooth.available()) {
    // Premier caractère : sens du moteur droit
    readChar = getBluetoothChar();
    moteurDroit.setDirection((int) readChar);

    // Second caractère : sens du moteur gauche
    readChar = getBluetoothChar();
    moteurGauche.setDirection((int) readChar);
    
    // Dernier caractère : vitesse
    readChar = getBluetoothChar();

    moteurDroit.setSpeed(2 * (int) readChar);
    moteurGauche.setSpeed(2 * (int) readChar);
  }
}

char getBluetoothChar() {
  char readChar;

  do {
    readChar = bluetooth.read();
  } while(readChar == '\n');

  Serial.print("Recu : ");
  Serial.println((int) readChar);
  
  return readChar;
}



