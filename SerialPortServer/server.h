/* 
 * Server listner
 */
 
#include <windows.h>
#include <winsock2.h>
#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#define LISTEN_PORT 8000

typedef struct
{
	HANDLE	serialPortHandle;
	volatile int listenSocket;
	volatile int socketFd;
	HANDLE	event;
}serialPortServer_t;

//function prototypes
int serverInit(int port);

unsigned __stdcall server(void *args);
