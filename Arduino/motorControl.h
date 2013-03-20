/*
* motorControl - Contains function prototypes for motorcontrol.cpp
*/

// Includes
#include "WProgram.h"
#include "sensor.h"

// Definitions

// Motor IO
#define M1_DIR 4
#define M1_SPEED 5
#define M2_DIR 7
#define M2_SPEED 6

// General
#define HIGH 1
#define LOW 0

// Function prototypes
void setMotorSpeed(int speed, int motor);

void backUp();

void turnMotor(int amount);

void setBoth(int speed);

void leftTurn();

void rightTurn();

void uTurn();

void moveOffJunction();

void manualLeftTurn();

void manualRightTurn();