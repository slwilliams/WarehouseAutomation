all: serial.exe

serial.exe: main.o serial.o server.o
			gcc -mthreads -o serial serial.o server.o main.o -lws2_32

main.o:	main.c
		gcc -c main.c

serial.o:	serial.c serial.h
			gcc -c serial.c

server.o:	server.c
			gcc -c server.c

clean:	
		erase *.o serial.exe
