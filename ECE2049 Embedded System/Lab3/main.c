#include <msp430.h>
#include <stdlib.h>
#include "peripherals.h"   //include the libraries
#define CALADC12_15V_30C *((unsigned int *)0x1A1A)
#define CALADC12_15V_85C *((unsigned int *)0x1A1C)

//function prototype
void displayTime(long unsigned int inTime);
void displayTemp(float inAvgTempC);
float calculateAvg(unsigned int array[]);
bool pressLeftButton ();
bool pressRightButton ();
void displayScroll(unsigned int dispTemp);
void swDelay(char numLoops, int s);
//global variables
long unsigned int count = 19872000;
long unsigned int time_cnt;
float temp;
unsigned int in_temp;
int state = 0;

int main(void)
{
  volatile float temperatureDegC;
  volatile float temperatureDegF;
  volatile float degC_per_bit;
  volatile unsigned int bits30, bits85;
  WDTCTL = WDTPW + WDTHOLD;      // Stop WDT

  // Init code here
  configDisplay();
  _BIS_SR(GIE);  //enable interrupts

  Graphics_clearDisplay(&g_sContext);
  Graphics_flushBuffer(&g_sContext);

  // Configure P8.0 as digital IO output and set it to 1
  // This supplied 3.3 volts across scroll wheel potentiometer
  // See schematic at end or MSP-EXP430F5529 board users guide
  P8SEL &= ~BIT0;
  P8DIR |= BIT0;
  P8OUT |= BIT0;

  unsigned int in_value[36];
  unsigned int in_value2;

  REFCTL0 &= ~REFMSTR;    // Reset REFMSTR to hand over control of
                          // internal reference voltages to
           // ADC12_A control registers

  ADC12CTL0 = ADC12SHT0_9 | ADC12REFON | ADC12ON | ADC12MSC;     // Internal ref = 1.5V

  ADC12CTL1 = ADC12SHP |ADC12CONSEQ_1|ADC12CSTARTADD_0 ;           // Enable sample timer

  // Using ADC12MEM0 to store reading
  ADC12MCTL0 = ADC12SREF_1 + ADC12INCH_10;  // ADC i/p ch A10 = temp sense ACD12SREF_1 = internal ref = 1.5v
  ADC12MCTL1 = ADC12SREF_0 + ADC12INCH_0 + ADC12EOS;

  __delay_cycles(100);                    // delay to allow Ref to settle
  ADC12CTL0 |= ADC12ENC;              // Enable conversion

  // Use calibration data stored in info memory
  bits30 = CALADC12_15V_30C;
  bits85 = CALADC12_15V_85C;
  degC_per_bit = ((float)(85.0 - 30.0))/((float)(bits85-bits30));

  Timer_A2_setup();

  while(1)
  {

      P2REN |= 0x02; // Enable resistor on P1.1
      P2OUT  = 0x02; // Set resistor to pull-up, P1.0 low
      P2DIR  = 0x01; // Set P2.1 as intput

      long unsigned int month, date, hour, minute, second;

      if (pressLeftButton()) state += 1;
      if (pressRightButton()){
          count = time_cnt + (date - 1) * 86400 + hour * 3600 + minute * 60 + second;
          Graphics_clearDisplay(&g_sContext); // Clear the display
          state = 0;
      }

      int j;
       for (j = 0; j < 36; j++)
       {
       ADC12CTL0 &= ~ADC12SC;  // clear the start bit
       ADC12CTL0 |= ADC12SC;       // Sampling and conversion start

       while (ADC12CTL1 & ADC12BUSY)
        __no_operation();

       in_value[j] = ADC12MEM0;
       in_value2 = ADC12MEM1;
       __no_operation();
       }

       long unsigned int avg = calculateAvg(in_value);

       temperatureDegC = (float)((float)avg - CALADC12_15V_30C) * degC_per_bit +30.0;


    switch (state)
    {

    case 0:

    if(count%3 == 0)
    {
        displayTime(count);
        displayTemp(temperatureDegC);

    }
    Graphics_flushBuffer(&g_sContext);
    _no_operation();                       // SET BREAKPOINT HERE
    break;

    case 1:
        Graphics_drawStringCentered(&g_sContext,"Date => AUG 19",14,48,15, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        if(count%2){
        Graphics_drawStringCentered(&g_sContext,"Date =>     19",14,48,15, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        }
        time_cnt = 0;
        Graphics_drawStringCentered(&g_sContext, "EDIT MONTH", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
        in_value2 = ADC12MEM1;
        month = in_value2 / 342 + 1;
        displayScroll(month);
        switch(month){
        case 1: time_cnt = 0; break;
        case 2: time_cnt = 2678400; break;
        case 3: time_cnt = 5097600; break;
        case 4: time_cnt = 7776000; break;
        case 5: time_cnt = 1036800; break;
        case 6: time_cnt = 13046400; break;
        case 7: time_cnt = 15638400; break;
        case 8: time_cnt = 18316800; break;
        case 9: time_cnt = 20995200; break;
        case 10: time_cnt = 23587200; break;
        case 11: time_cnt = 26265600; break;
        case 12: time_cnt = 28857600; break;

        default: break;
        }

        if (pressRightButton()) {
            Graphics_clearDisplay(&g_sContext); // Clear the display
            state = 0;
            break;
        }

        P2REN |= 0x02; // Enable resistor on P1.1
        P2OUT  = 0x02; // Set resistor to pull-up, P1.0 low
        P2DIR  = 0x01; // Set P2.1 as intput

        Graphics_flushBuffer(&g_sContext);
        break;

    case 2:
        Graphics_drawStringCentered(&g_sContext,"Date => AUG 19",14,48,15, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        if(count%2){
        Graphics_drawStringCentered(&g_sContext,"Date => AUG   ",14,48,15, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        }
        Graphics_flushBuffer(&g_sContext);
            Graphics_drawStringCentered(&g_sContext, " EDIT DATE ", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
            Graphics_flushBuffer(&g_sContext);
            in_value2 = ADC12MEM1;
            date = in_value2 / 133 + 1;
            displayScroll(date);
            break;
    case 3:

        Graphics_drawStringCentered(&g_sContext,"Date => AUG 19",14,48,15, OPAQUE_TEXT);
        Graphics_drawStringCentered(&g_sContext,"Time =>   :00:00",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        if(count%2){
        Graphics_drawStringCentered(&g_sContext,"Time => 00:00:00",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        }
        Graphics_flushBuffer(&g_sContext);
            Graphics_drawStringCentered(&g_sContext, " EDIT HOUR ", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
            Graphics_flushBuffer(&g_sContext);
            in_value2 = ADC12MEM1;
            hour = in_value2 / 171;
            displayScroll(hour);
            break;
    case 4:
        Graphics_drawStringCentered(&g_sContext,"Time => 00:  :00",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        if(count%2){
        Graphics_drawStringCentered(&g_sContext,"Time => 00:00:00",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        }
        Graphics_flushBuffer(&g_sContext);
            Graphics_drawStringCentered(&g_sContext, " EDIT MINUTE ", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
            Graphics_flushBuffer(&g_sContext);
            in_value2 = ADC12MEM1;
            minute = in_value2 / 69;
            displayScroll(minute);
            break;
    case 5:
        Graphics_drawStringCentered(&g_sContext,"Time => 00:00:  ",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        if(count%2){
        Graphics_drawStringCentered(&g_sContext,"Time =>   :00:00",16,48,25, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        }
        Graphics_flushBuffer(&g_sContext);
            Graphics_drawStringCentered(&g_sContext, " EDIT SECOND ", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
            Graphics_flushBuffer(&g_sContext);
            in_value2 = ADC12MEM1;
            second = in_value2 / 69;
            displayScroll(second);
            break;
    default:
        //Graphics_clearDisplay(&g_sContext); // Clear the display
        //configDisplay();
            Graphics_drawStringCentered(&g_sContext, " DEFAULT ", AUTO_STRING_LENGTH, 48, 55, OPAQUE_TEXT);
            Graphics_flushBuffer(&g_sContext);
            break;


  }

 }
}
void displayScroll(unsigned int dispTemp){
    unsigned int in_value2;
    in_value2 = ADC12MEM1;
    int dispTemp0,dispTemp1,dispTemp2,dispTemp3,dispTemp4;
    unsigned char disp5[7];
    disp5[0] = ' ';
    disp5[6] = ' ';

    dispTemp0 = dispTemp / 10000;
    dispTemp1 = dispTemp / 1000 - dispTemp0 * 10;
    dispTemp2 = dispTemp / 100 - dispTemp0 * 100 - dispTemp1 * 10;
    dispTemp3 = dispTemp / 10 - dispTemp0 * 1000 - dispTemp1 * 100 - dispTemp2 * 10;
    dispTemp4 = dispTemp - dispTemp0 * 10000 - dispTemp1 * 1000 - dispTemp2 * 100 - dispTemp3 * 10;

    disp5[1] = dispTemp0 + 0x30;
    disp5[2] = dispTemp1 + 0x30;
    disp5[3] = dispTemp2 + 0x30;
    disp5[4] = dispTemp3 + 0x30;
    disp5[5] = dispTemp4 + 0x30;


    Graphics_drawStringCentered(&g_sContext,disp5,7,48,65, OPAQUE_TEXT);

}

float calculateAvg(unsigned int array[]){
    int i = 0, total;
    for(i = 0; i < sizeof(array)/sizeof(array[0]) ; i++)
    { total += array[i];
    }
    return total/ (sizeof(array)/sizeof(array[0]));
}

//Function to check the state of the left button on launchpad
bool pressLeftButton (){
    bool returnValue = false;

     P2REN |= 0x02; // Enable resistor on P1.1
     P2OUT  = 0x02; // Set resistor to pull-up, P1.0 low
     P2DIR  = 0x01; // Set P2.1 as intput



       if( !(P2IN & 0x02)) // If push button is pressed
       {
//         P1OUT ^= 0x01;
//         P4OUT ^= 0x80;

         returnValue = true;

       }

     return returnValue;
}

//Function to check the state of the right button on launchpad
bool pressRightButton (){
    bool returnValue = false;
     P1REN |= 0x02; // Enable resistor on P1.1
     P1OUT  = 0x02; // Set resistor to pull-up, P1.0 low
     P1DIR  = 0x01; // Set P1.0 as output and P1.1 as intput

       if( !(P1IN & 0x02)) // If push button is pressed
       {
//         P1OUT ^= 0x01;
//         P4OUT ^= 0x80;

         returnValue = true;

       }

     return returnValue;
}

void Timer_A2_setup(void) //when you run timer
{
    TA2CTL = TASSEL_1 + ID_0 + MC_1;
    TA2CCR0 = 32767;       // 1s
    TA2CCTL0 = CCIE;     // TA2CCR0 interrupt enabled
}

#pragma vector=TIMER2_A0_VECTOR
__interrupt void Timer_A2_ISR(void)
{
        count ++;
}


void displayTime(long unsigned int inTime)
{
    double second = inTime;
    long unsigned int day;
    long unsigned int hour, minute;
    char month[3];

    day = second / 24 / 3600;

    second -= day * 24 * 3600;
    hour = second / 3600;

    second -= hour * 3600;
    minute = second / 60;

    second -= minute * 60;

    if(0 <= day && day < 31)
    {
        month[0] = 'J';
        month[1] = 'A';
        month[2] = 'N';
    }
    else if(31 <= day && day < 59)
    {
        month[0] = 'F';
        month[1] = 'E';
        month[2] = 'B';
        day -= 31;
    }
    else if(59 <= day && day < 90)
    {
        month[0] = 'M';
        month[1] = 'A';
        month[2] = 'R';
        day -= 59;
    }
    else if(90 <= day && day < 120)
    {
        month[0] = 'A';
        month[1] = 'P';
        month[2] = 'R';
        day -= 90;
    }
    else if(120 <= day && day < 151)
    {
        month[0] = 'M';
        month[1] = 'A';
        month[2] = 'Y';
        day -= 120;
    }
    else if(151 <= day && day < 181)
    {
        month[0] = 'J';
        month[1] = 'U';
        month[2] = 'N';
        day -= 151;
    }
    else if(181 <= day && day < 212)
    {
        month[0] = 'J';
        month[1] = 'U';
        month[2] = 'L';
        day -= 181;
    }
    else if(212 <= day && day < 243)
    {
        month[0] = 'A';
        month[1] = 'U';
        month[2] = 'G';
        day -= 212;
    }
    else if(243 <= day && day < 273)
    {
        month[0] = 'S';
        month[1] = 'E';
        month[2] = 'P';
        day -= 243;
    }
    else if(273 <= day && day < 304)
    {
        month[0] = 'O';
        month[1] = 'C';
        month[2] = 'T';
        day -= 273;
    }
    else if(304 <= day && day < 334)
    {
        month[0] = 'N';
        month[1] = 'O';
        month[2] = 'V';
        day -= 304;
    }
    else if(334 <= day && day < 365)
    {
        month[0] = 'D';
        month[1] = 'E';
        month[2] = 'C';
        day -= 334;
    }

    day += 1;

    unsigned char disp[14], disS = 14;
    disp[0] = 'D';
    disp[1] = 'a';
    disp[2] = 't';
    disp[3] = 'e';
    disp[4] = ' ';
    disp[5] = '=';
    disp[6] = '>';
    disp[7] = ' ';
    disp[8] = month[0];
    disp[9] = month[1];
    disp[10] = month[2];
    disp[11] = ' ';
    disp[12] = day / 10 + 0x30;
    disp[13] = day % 10 + 0x30;

    Graphics_drawStringCentered(&g_sContext,disp,disS,48,15, OPAQUE_TEXT);

    unsigned char disp2[16], disS2 = 16;
    disp2[0] = 'T';
    disp2[1] = 'i';
    disp2[2] = 'm';
    disp2[3] = 'e';
    disp2[4] = ' ';
    disp2[5] = '=';
    disp2[6] = '>';
    disp2[7] = ' ';
    disp2[8] = hour / 10 + 0x30;
    disp2[9] = hour % 10 + 0x30;
    disp2[10] = ':';
    disp2[11] = minute / 10 + 0x30;
    disp2[12] = minute % 10 + 0x30;
    disp2[13] = ':';
    disp2[14] = second / 10 + 0x30;
    disp2[15] = second - (int) second / 10 * 10+ 0x30;
    Graphics_drawStringCentered(&g_sContext,disp2,disS2,48,25, OPAQUE_TEXT);
}

void displayTemp(float inAvgTempC)
{
    float dispTemp = inAvgTempC;
    float dispTemp5;
    int dispTemp1,dispTemp2,dispTemp3,dispTemp4,dispTemp6,dispTemp7,dispTemp8,dispTemp9;

    unsigned char disp3[16], disS3 = 16;

    dispTemp1 = dispTemp / 100;
    dispTemp2 = dispTemp / 10 - dispTemp1 * 10;
    dispTemp3 = dispTemp - dispTemp1 * 100 - dispTemp2 * 10;
    dispTemp4 = dispTemp * 10 - dispTemp1 * 1000 - dispTemp2 * 100 - dispTemp3 * 10;

    disp3[0] = 'T';
    disp3[1] = 'e';
    disp3[2] = 'm';
    disp3[3] = 'p';
    disp3[4] = '(';
    disp3[5] = 'C';
    disp3[6] = ')';
    disp3[7] = '=';
    disp3[8] = '>';
    disp3[9] = dispTemp1 + 0x30;
    disp3[10] = dispTemp2 + 0x30;
    disp3[11] = dispTemp3 + 0x30;
    disp3[12] = '.';
    disp3[13] = dispTemp4 + 0x30;
    disp3[14] = ' ';
    disp3[15] = 'C';
    Graphics_drawStringCentered(&g_sContext,disp3,disS3,48,35, OPAQUE_TEXT);

    dispTemp5 = dispTemp * 1.8 + 32;

    dispTemp6 = dispTemp5 / 100;
    dispTemp7 = dispTemp5 / 10 - dispTemp6 * 10;
    dispTemp8 = dispTemp5 - dispTemp6 * 100 - dispTemp7 * 10;
    dispTemp9 = dispTemp5 * 10 - dispTemp6 * 1000 - dispTemp7 * 100 - dispTemp8 * 10;

    unsigned char disp4[16], disS4 = 16;
    disp4[0] = 'T';
    disp4[1] = 'e';
    disp4[2] = 'm';
    disp4[3] = 'p';
    disp4[4] = '(';
    disp4[5] = 'F';
    disp4[6] = ')';
    disp4[7] = '=';
    disp4[8] = '>';
    disp4[9] = dispTemp6 + 0x30;
    disp4[10] = dispTemp7+ 0x30;
    disp4[11] = dispTemp8 + 0x30;
    disp4[12] = '.';
    disp4[13] = dispTemp9 + 0x30;
    disp4[14] = ' ';
    disp4[15] = 'F';
    Graphics_drawStringCentered(&g_sContext,disp4,disS4,48,45, OPAQUE_TEXT);
}

void swDelay(char numLoops, int s)
{
    // This function is a software delay. It performs
    // useless loops to waste a bit of time
    //
    // Input: numLoops = number of delay loops to execute
    // Output: none
    //
    // smj, ECE2049, 25 Aug 2013

    volatile unsigned int a,b;  // volatile to prevent removal in optimization
                                // by compiler. Functionally this is useless code

    for (b=0; b<numLoops; b++)
    {
        a = 50000/s;                // SW Delay
        while (a > 0)               // could also have used while (i)
           a--;
    }
}
