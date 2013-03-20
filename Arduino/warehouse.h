/*
* Warehouse - definitions for Warehouse.pde
*/

//Definitions

///***** VERSION NUMBER ****///
#define VERSION_STRING "Version 1.2 Alpha"

#define POLL_INTERVAL 50

// These for the Pololu library. We are using 4 sensors wired to Analogue channels 0 -> 3
#define NUM_SENSORS             4 
#define NUM_SAMPLES_PER_SENSOR  4  // average 4 analog samples per sensor reading
#define EMITTER_PIN             2

#define CALIBRATION_LOOP_CNT 256

