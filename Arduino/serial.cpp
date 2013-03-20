/*
* Serial - functions relating to serial comunication
*/

// Includes
#include "serial.h"
#include "motorControl.h"
#include "sensor.h"

// Globals
char g_buffer[SERIAL_BUFF_SIZE];
int g_buffPos;

//returns a serial character
char getSerial()
{
  char c;
  c = Serial.read();
  Serial.flush();
  return c; 
}

//returns true if there is a new command
boolean newCommand()
{
  if(Serial.available() > 0)
  {
    return true;
  } 
  else
  {
    return false;
  }
}

//sends the request char to the computer
void requestCommand()
{
  Serial.print("C");
}

