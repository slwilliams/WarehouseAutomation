/*
* Sensor - function prototypes for sensor.cpp
*/

//definitions
enum
{
  LINE_SENSOR,
  LEFT_SENSOR,
  RIGHT_SENSOR,
  MAX_SENSOR
};

//function prototypes
int getSensor(int sensor);

int getSensorScaled(int sensor);

void setSensorMax(int sensor);
void setSensorMin(int sensor);

void setSensorScale(int sensor);

void setSensorMinMaxManually(int sensor, int minVal, int maxVal);



