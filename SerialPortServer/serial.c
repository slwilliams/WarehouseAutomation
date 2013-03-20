/* 
 * Serial port support routines
 */

#include "serial.h"

HANDLE serialInit(char *port, int baudRate)
{
	HANDLE hSerial;
	DCB params = {0};
	COMMTIMEOUTS timeouts={0};

	//Try and open the serial port
	hSerial = CreateFile(	port,
				GENERIC_READ | GENERIC_WRITE,
				0,
				0,
				OPEN_EXISTING,
				0,
				0);

	if(hSerial == INVALID_HANDLE_VALUE)
	{
		fprintf(stderr,"Can't open COM1\n");
		return hSerial;
	}


	//Set the port parameters
	params.DCBlength=sizeof(params);

	if(!GetCommState(hSerial, &params))
    {
		fprintf(stderr,"Can't get serial port paramaters\n");
		CloseHandle(hSerial);
		hSerial = INVALID_HANDLE_VALUE;
		return hSerial;
	}

	params.BaudRate=baudRate;
	params.ByteSize=8;
	params.StopBits=ONESTOPBIT;
	params.Parity=NOPARITY;

	if(!SetCommState(hSerial, &params))
	{
		fprintf(stderr,"Can't set serial port paramaters\n");
		CloseHandle(hSerial);
		hSerial = INVALID_HANDLE_VALUE;
		return hSerial;
	}


	//Set the timeouts for Rx
	timeouts.ReadIntervalTimeout=50;
	timeouts.ReadTotalTimeoutConstant=50;
	timeouts.ReadTotalTimeoutMultiplier=10;
	timeouts.WriteTotalTimeoutConstant=50;
	timeouts.WriteTotalTimeoutMultiplier=10;

	if(!SetCommTimeouts(hSerial, &timeouts))
	{
		fprintf(stderr,"Can't set COM port timeouts\n");
		CloseHandle(hSerial);
		hSerial = INVALID_HANDLE_VALUE;
	}

	return hSerial;
}

void serialCleanup(HANDLE hSerial)
{
	CloseHandle(hSerial);
}

//send a message out of the serial port
int serialSend(HANDLE hSerial, uint8_t *data, int length)
{
	DWORD retValue = -1;

	if(!WriteFile(hSerial,data,length, &retValue,NULL))
	{
		fprintf(stderr,"Failed to write to serial port\n");
		retValue = -1;
	}

	return retValue;
}

//read a message in from the robot
int serialRead(HANDLE hSerial, uint8_t *data, int length)
{
	DWORD dwBytesRead = -1;
	int n;

	if(!ReadFile(hSerial, data, length, &dwBytesRead, NULL))
	{
		fprintf(stderr,"Can't read from serial port\n");
   	}
	
	if( dwBytesRead >= 1 )
	{		
		printf("Rx: ");
		for(n=0; n < dwBytesRead; n++)
		{
			printf("%02X ",data[n]);
		}
		printf("\n");
	}
	
	return dwBytesRead;
}

