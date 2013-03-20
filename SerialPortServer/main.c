/* 
 * Serial port Server
 * Listen on TCP port for a connection. Then send data received over TCP to com port
 */

#include <stdio.h>
#include <process.h>

#include "serial.h"
#include "server.h"

serialPortServer_t serialPortServer;

int main(int argc, char **argv)
{
	HANDLE h, hThread,hEvent;
	uint8_t buffer[1024];
	int threadID, n;
	int listenFd,socketFd,timeout;

	printf("Serial port Server V1.8\n");

	argv++;

	printf("Using COMPORT: %s\n",*argv);
	printf("Listening on port %d\n\n",LISTEN_PORT);

	h = serialInit(*argv,38400);

	serialPortServer.serialPortHandle = h;

	if((listenFd=serverInit(LISTEN_PORT)) < 0)
	{
		return -1;
	}

	hEvent = CreateEvent(NULL, TRUE, FALSE, NULL);

	serialPortServer.listenSocket = listenFd;
	serialPortServer.event = hEvent;

	//Start the listner thread
	hThread = (HANDLE) _beginthreadex(NULL,8192,&server,&serialPortServer,1,&threadID);

	//Wait for an event from the listner
	while(1)
	{
		//Server will signal we have a command in progress
		n = WaitForSingleObject(hEvent,INFINITE);
		socketFd = serialPortServer.socketFd;

		if(n == 0)
		{
			printf("Reset Event\n");
			ResetEvent(hEvent);

			//Wait while the command is carried out
			timeout = 120;
			while(serialRead(h,buffer,10) <= 0)
			{
				Sleep(500);
				send(socketFd,"Moving\n\r",8,0);
				if(--timeout == 0)
				{
					break;
				}
			}

			Sleep(500);
			if(timeout)
			{
				send(socketFd,"Request\n\r",9,0);
			}
			else
			{
				send(socketFd,"FAILED\n\r",8,0);
			}
		}
	}

	return 0;
}
