/*
* Sensor - Functions relating to reading in sensor data 
*/

// Includes
#include "WProgram.h"
#include "sensor.h"

#define DEBUG
//privtae variables
int s_maxValue[MAX_SENSOR];
int s_minValue[MAX_SENSOR];
float s_scaleFactor[MAX_SENSOR];

//returns raw sensor data
int getSensor(int sensor)
{
  return analogRead(sensor);
}

//returns scaled sensor data
int getSensorScaled(int sensor)
{
  //the result to return
  int retValue = 0;
  //the raw sensor data
  int sensorVal
  //the average sensor value
  int middle;
  
  //get the raw sensor data
  sensorVal  = getSensor(sensor);

  
  if(sensorVal < s_minValue[sensor])
  {
    sensorVal = s_minValue[sensor];
  }
  
  if(sensorVal > s_maxValue[sensor])
  {
    sensorVal = s_maxValue[sensor];
  }
  
  middle = (s_maxValue[sensor] + s_minValue[sensor])/2;
  retValue = ((float)(sensorVal-middle)) * s_scaleFactor[sensor];

  return retValue;
}

//sets the max detected sensor value
void setSensorMax(int sensor)
{
  int value;
  
  if(s_maxValue[sensor] < (value = getSensor(sensor)))
  {
    s_maxValue[sensor] = value;
  }  
}

//sets the min detected sensor value
void setSensorMin(int sensor)
{
  int value;
  
  if(s_minValue[sensor] > (value = getSensor(sensor)))
  {
    s_minValue[sensor] = value;
  }  
}

//assigns the sensor scaled value
void setSensorScale(int sensor)
{
  float scale;  
  scale = 510.0/((float)s_maxValue[sensor] - (float)s_minValue[sensor]);
  
  s_scaleFactor[sensor] = scale;
}

///////
// Debug function
//////
void setSensorMinMaxManually(int sensor, int minVal, int maxVal)
{
  s_minValue[sensor] = minVal;
  s_maxValue[sensor] = maxVal;
}



