/*
* MotorControl - Contains functions for controlling the motors
*/

//includes
#include "motorControl.h"

//global variables
int leftMotorSpeed;
int rightMotorSpeed;

//sets both of the motors to the same speed
void setBoth(int speed)
{
   leftMotorSpeed = speed;
   rightMotorSpeed = speed;
   setMotorSpeed(speed,0);
   setMotorSpeed(speed,1);
}

//sets an individual motors speed
void setMotorSpeed(int speed, int motor)
{
  int motorDir;
  int motorPWM;
  
  if(speed > 255)
  {
    speed = 255;
  }
  if(speed < -255)
  {
    speed = -255;    
  }
  
  motorDir = motor ? M1_DIR : M2_DIR;
  motorPWM = motor ? M1_SPEED : M2_SPEED;
  speed = -speed;

  if(speed < 0)
  {
    digitalWrite(motorDir, LOW);
    speed = -speed;
  }
  else
  {
    digitalWrite(motorDir, HIGH);
  }
  
  analogWrite(motorPWM,speed & 0xff);
}

//turns the robot
void turnMotor(int amount)
{
  setMotorSpeed(leftMotorSpeed - amount, 0);
  setMotorSpeed(rightMotorSpeed + amount, 1);
}

//backs up the robot back onto the junction
void backUp()
{
  delay(500);
  
  setBoth(-80);
  
  while( (getSensor(4) < 630) || (getSensor(5) < 630) )
  {
    delay(50);
  }
  
  setBoth(0);  
}

void leftTurn()
{
  //set the motors to turn the robot
  setMotorSpeed(-140,0);
  setMotorSpeed(140,1);
  
  //we will stay in this loop whilst both sensors cannot see a line
  while((getSensor(4) > 630) || (getSensor(5) > 630))
  {
    delay(5);
  }
  
  delay(100);
  
  //at this point the robot is facing the slightly wrong direction
  //so we turn for a bit more
  while((getSensor(4) < 630) && (getSensor(5) < 630))
  {
    delay(5); 
  }
  
  //the robot has compleated a 90 degrees turn
  //stop the motors
  setBoth(0);
}

void rightTurn()
{
  setMotorSpeed(190,0);
  setMotorSpeed(-145,1);
  
  while((getSensor(4) > 630) || (getSensor(5) > 630))
  {
    delay(5);
  }
  
  delay(100);
  
  while((getSensor(4) < 630) && (getSensor(5) < 630))
  {
    delay(5); 
  }
  setBoth(0);
}

void uTurn()
{
  setMotorSpeed(150,0);
  setMotorSpeed(-150,1);
  delay(400);
}

void moveOffJunction()
{
  setBoth(150);
  delay(150);
  setBoth(0);
}

void manualLeftTurn()
{
  setMotorspeed(150,0);
  setMotorSpeed(-150,1);
  delay(150);
  setBoth(0);
}

void manualRightTurn()
{
  setMotorspeed(-150,0);
  setMotorSpeed(150,1);
  delay(150);
  setBoth(0);
}
