/* Serial port support routines
 *
 * With thanks to 123a321MicrocontrollerProject (http://123a321.wordpress.com/)
 */

#include <windows.h>
#include <stdint.h>
#include <stdio.h>

//function prototypes
HANDLE serialInit(char *port, int baudRate);

void serialCleanup(HANDLE hSerial);

int serialSend(HANDLE hSerial, uint8_t *data, int length);

int serialRead(HANDLE hSerial, uint8_t *data, int length);
