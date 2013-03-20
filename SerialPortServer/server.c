/* 
 * Listner for serial port server
 */

#include "server.h"
#include "serial.h"

//inistialise the server
int serverInit(int port)
{
	int listenFd = -1;
	WSADATA wsadata;
	int on,len,n;
	struct sockaddr_in server_addr;
	struct sockaddr_in client_addr;
	unsigned int client_addr_length = sizeof(client_addr);

	if(WSAStartup(MAKEWORD(1,1),&wsadata) == SOCKET_ERROR)
	{
		fprintf(stderr,"Error creating Socket\n");
		return -1;
	}

	if((listenFd = socket(AF_INET,SOCK_STREAM,0)) < 0)
	{
		fprintf(stderr,"Error opening socket\n");
		return -1;
	}

	memset(&server_addr,0,sizeof(server_addr));
	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	server_addr.sin_port = htons(LISTEN_PORT);

	on = 1;
	setsockopt(listenFd,SOL_SOCKET,SO_REUSEADDR,(char*)&on,sizeof(on));

	if(bind(listenFd,(struct sockaddr *)&server_addr,sizeof(server_addr)) < 0)
	{
		fprintf(stderr,"Error binding to socket\n");
		return -1;
	}

	if(listen(listenFd,10) < 0)
    {
		fprintf(stderr,"Error listening on socket.");
		return -1;
	}
	
	return listenFd;
}


unsigned __stdcall server(void *args)
{
	serialPortServer_t *sps = (serialPortServer_t *) args;
	int socketFd,listenFd,len;
	struct sockaddr_in client_addr;
	unsigned int client_addr_length = sizeof(client_addr);
	HANDLE hEvent;
	char request[8192];

	hEvent = sps->event;

	listenFd = sps->listenSocket;
	
	//Listen for connection and handle the data
	while(1)
	{
		socketFd = accept(listenFd,(struct sockaddr *)&client_addr,&client_addr_length);	

		sps->socketFd = socketFd;
		printf("Connection\n");

		do
		{
			len = recv(socketFd,request,sizeof(request),0);
			if(len == 1)
			{
					switch(request[0])
					{
						case 'L' : printf("L\n");
								   serialSend(sps->serialPortHandle,"L",1);
								   SetEvent(hEvent);
								   break;

						case 'R' : printf("R\n");
								   serialSend(sps->serialPortHandle,"R",1);
								   SetEvent(hEvent);
								   break;

						case 'F' : printf("F\n");
								   serialSend(sps->serialPortHandle,"F",1);
								   SetEvent(hEvent);
								   break;

						case 'U' : printf("U\n");
								   serialSend(sps->serialPortHandle,"U",1);
								   SetEvent(hEvent);
								   break;

						case 'E' : SetEvent(hEvent);
								   printf("EVENT\n");
								   break;
								   
						case 'N' : printf("N\n");
								   serialSend(sps->serialPortHandle,"N",1);
								   SetEvent(hEvent);
								   break;
								   
						case 'M' : printf("M\n");
								   serialSend(sps->serialPortHandle, "M", 1);
								   SetEvent(hEvent);
								   break;
						
						case '1' : printf("1\n");
								   serialSend(sps->serialPortHandle, "1", 1);
								   SetEvent(hEvent);
								   break;
						
						case '2' : printf("2\n");
								   serialSend(sps->serialPortHandle, "2", 1);
								   SetEvent(hEvent);
								   break;
						
						case '3' : printf("3\n");
								   serialSend(sps->serialPortHandle, "3", 1);
								   SetEvent(hEvent);
								   break;
								   
						case '4' : printf("4\n");
							       serialSend(sps->serialPortHandle, "4", 1)
								   SetEvent(hEvent);
								   break;
								   
						case 'S' : printf("S\n");
								   serialSend(sps->serialPortHandle, "S", 1);
								   SetEvent(hEvent);
								   break;
					}
			}
		}while(len > 0);

		close(socketFd);
		printf("Disconnection\n");
	}
	return 0;
}
