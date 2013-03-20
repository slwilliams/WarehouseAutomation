/*
* Pid - Fucntions relating to the line following algorithm
*/

// Includes
#include "pid.h"

// Global variables
float lastError;
float sumError;
float factorP = 0.05;
float factorI = 0.0;
float factorD = 0.225;
//0.225

//the main pid function
//returns the result of the algorithm
int pid(int error)
{
  float result;
  float deltaErr;

  deltaErr = error - lastError;
  lastError = error;
  sumError += error;
  	
  result = ((float)error * factorP) + (sumError * factorI) + (deltaErr * factorD);
  
  return (int)result;
}

//these functions allow manual setting of the factors
//used for debugging purposes
void setFactorP(float p)
{
  factorP = p;
  lastError = sumError = 0;
}

void setFactorI(float i)
{
  factorI = i;
  lastError = sumError = 0;
}

void setFactorD(float d)
{
  factorD = d;
  lastError = sumError = 0;
}
