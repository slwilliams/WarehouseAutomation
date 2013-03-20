/*
* Warehouse management robot code
*/

//includes
#include "warehouse.h"
#include "motorControl.h"
#include <PololuQTRSensors.h>
#include "sensor.h"
#include "pid.h"
#include "serial.h"

// Program STATES
typedef enum
{
      WAIT_FOR_CMD,
      LINE_FOLLOWING,
      MOVE_FORWARDS,
      TURN_LEFT,
      TURN_RIGHT,
      U_TURN,
	  MANUAL
}currentState_t;
 

//global variables
unsigned long lastPollTime;
unsigned int sensorValues[NUM_SENSORS];
currentState_t currentState;

// Instantiate the Pololu library class
PololuQTRSensorsAnalog sensorQTRA((unsigned char[]) {0, 1, 2, 3}, NUM_SENSORS, NUM_SAMPLES_PER_SENSOR, QTR_NO_EMITTER_PIN);


//setup function called once when the robot is turned on
void setup()
{
  //ALLWAYS keep this delay - otherwise the IDE can't gain control of the board
  delay(2000);  
  
  Serial.begin(38400); 
  
  pinMode(M1_DIR, OUTPUT);
  pinMode(M1_SPEED, OUTPUT);
  pinMode(M2_DIR, OUTPUT);
  pinMode(M2_SPEED, OUTPUT);
  pinMode(LED,OUTPUT);

  Serial.println("Calibation in progress...................");
  
  //We need to calibrate the sensor array. Should take about 12 seconds
  //of moving backwards and forwards
  for(int n=0; n < CALIBRATION_LOOP_CNT; n++)
  {
    if( n & 32 )
    {
      setBoth(-170);
    }
    else
    {
      setBoth(180);
    }
    
    sensorQTRA.calibrate();
  }
  
  setBoth(0);
  
  //print the calibration minimum values measured when emitters were on
  for (int n = 0; n < NUM_SENSORS; n++)
  {
    Serial.print(sensorQTRA.calibratedMinimumOn[n]);
    Serial.print(' ');
  }
  Serial.println();
  
  //print the calibration maximum values measured when emitters were on
  for (int n = 0; n < NUM_SENSORS; n++)
  {
    Serial.print(sensorQTRA.calibratedMaximumOn[n]);
    Serial.print(' ');
  }
  Serial.println();
  Serial.println();
  
  delay(1000);
  
  Serial.println(" "); 
  Serial.println(VERSION_STRING);
  Serial.println("Running.........");
}

//main loop function
void loop()
{
  //pid algorithm variables
  //how far the robot is away from the line
  int error;
  //what the robot should turn to decrease the error
  int newTurnValue = 0;
  //the raw position of the robot relative to the line
  unsigned int positionOnLine;
  //how long the loop took
  unsigned long timeChange;
  //how many times we poll the sensors per second
  unsigned long pollInterval = POLL_INTERVAL;
  //the current system time
  unsigned long timeNow = millis();  
  
  //check to see if we have a message
  if(newCommand())
  {
	  //we do, parse the serial command
      switch(toupper(getSerial()))
      {
        case 'L' :  currentState = TURN_LEFT;
                    break;
        case 'R' :  currentState = TURN_RIGHT;
                    break;
        case 'F' :  currentState = MOVE_FORWARDS;
                    break;
        case 'U' :  currentState = U_TURN;
                    break;
		case 'M' :  currentState =  MANUAL;
					break;
        default  :  currentState =  WAIT_FOR_CMD;
                    break;
      }
  }
  
  //main state machine
  switch(currentState)
  {
    case WAIT_FOR_CMD   : //stop the robot
						  setBoth(0);
                          break;
                        
    case LINE_FOLLOWING : //check to see if we are at a junction
						  if((getSensor(4) > 630) && (getSensor(5) > 630))
                          {
						     //we are, stop the robot 
                             setBoth(0);
                             backUp();
                             currentState = WAIT_FOR_CMD;
							 //ask for a new command
                             requestCommand(); 
                          }
                          else
                          {
                            //Run the PID algorithm at defined intervals
                            if((timeChange = timeNow - lastPollTime) > pollInterval)
                            {
							  lastPollTime = timeNow;
                              //read the sensors
							  positionOnLine = sensorQTRA.readLine(sensorValues);
                              error = 1500 - positionOnLine;
                              error = -error;
                              //calculate the turn value to correct the robot
							  newTurnValue = pid(error);
							  //turn the robot with the above value
                              turnMotor(newTurnValue);
                            }
                          }
                          break;
                          
    case MOVE_FORWARDS  : moveOffJunction();
                          setBoth(100);
                          currentState = LINE_FOLLOWING;
                          break;
                          
    case TURN_LEFT     :  leftTurn();
                          currentState = MOVE_FORWARDS;
                          break;
                          
    case TURN_RIGHT    :  rightTurn();
                          currentState = MOVE_FORWARDS;
                          break;
                          
    case U_TURN        :  uTurn();
                          currentState = MOVE_FORWARDS;
                          break;
						  
	case MANUAL		   :  //state for manual control
						  while((char f = getSerial()) != 'S')
						  {
						    switch(f)
							{
							  case '1' :  setBoth(100);
									      break;
							  case '2' :  setBoth(-100);
										  break;
							  case '3' :  manualLeftTurn();
										  break;
							  case '4' :  manualRightTurn();
										  break;
							}
						  }
						  currentState = WAIT_FOR_COMMAND;
						  break;    
  }  
}
