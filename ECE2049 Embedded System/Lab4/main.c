#include <msp430.h>
#include <stdlib.h>
#include "peripherals.h"   //include the libraries

//declare function prototypes
void configButtons();
int checkButtons();
void DACInit(void);
void DACSetValue(unsigned int dac_code);
void Timer_A2_setup(void);
void displayScroll(unsigned int in_value);
float voltageADC();
void displayADC(unsigned int dispTemp);
void displayTemp(float inAvgTempC);

//Global variable
long unsigned int count = 0;
unsigned int last_count;
unsigned int adcValue;
int state = 0;

unsigned int dacCodes[2] = {0,2000};

// Main
void main(void)

{
   WDTCTL = WDTPW | WDTHOLD;   // stop watchdog timer

    // Useful code starts here
   configDisplay();
   configButtons();
   DACInit();
   _BIS_SR(GIE);

   // *** Intro Screen ***
   Graphics_clearDisplay(&g_sContext);
   Graphics_drawStringCentered(&g_sContext,"User Guide",10,48,5, TRANSPARENT_TEXT);
   Graphics_drawStringCentered(&g_sContext,"S1=DC Voltage   ",16,48,25, TRANSPARENT_TEXT);
   Graphics_drawStringCentered(&g_sContext,"S2=Square Wave  ",16,48,45, TRANSPARENT_TEXT);
   Graphics_drawStringCentered(&g_sContext,"S3=Sawtooth Wave",16,48,65, TRANSPARENT_TEXT);
   Graphics_drawStringCentered(&g_sContext,"S4=Triangle Wave",16,48,85, TRANSPARENT_TEXT);
   Graphics_flushBuffer(&g_sContext);

   //Configure Scroll Wheel
   P8SEL &= ~BIT0;
   P8DIR |= BIT0;
   P8OUT |= BIT0;

   unsigned int in_value;
   float displayADC;

   REFCTL0 &= ~REFMSTR;    // Reset REFMSTR to hand over control of
                           // internal reference voltages to
            // ADC12_A control registers

   ADC12CTL0 = ADC12SHT0_9 | ADC12ON;     // Internal ref = 1.5V

   ADC12CTL1 = ADC12SHP;           // Enable sample timer

   // Using ADC12MEM0 to store reading
   ADC12MCTL0 = ADC12SREF_0 + ADC12INCH_0;

   __delay_cycles(100);                    // delay to allow Ref to settle
   ADC12CTL0 |= ADC12ENC;              // Enable conversion




   Timer_A2_setup();

   //case2
   last_count = count;

   /*
   //case3
   long unsigned int dacCodeSawT[4096];
   long int i;
   for (i = 0; i < 4096;i++){
       dacCodeSawT[i] = i;
   }
   */

   while(1){

   if (checkButtons() == BIT3)
       {
           state = 1;
           Graphics_clearDisplay(&g_sContext);
       }
   if (checkButtons() == BIT2)
       {
           state = 2;
           Graphics_clearDisplay(&g_sContext);
       }
   if (checkButtons() == BIT1)
       {
           count = 0;
           state = 3;
           Timer_A2_setup();
           Graphics_clearDisplay(&g_sContext);
       }
   if (checkButtons() == BIT0)
       {
           //count = 0;
           Timer_A2_setup();
           state = 4;
           Graphics_clearDisplay(&g_sContext);
       }

   ADC12CTL0 &= ~ADC12SC;  // clear the start bit
   ADC12CTL0 |= ADC12SC;

   while (ADC12CTL1 & ADC12BUSY)
       __no_operation();

       in_value = ADC12MEM0;
       __no_operation();

   switch(state)
   {
       case 1:
           Graphics_drawStringCentered(&g_sContext,"DC Voltage",10,48,15, TRANSPARENT_TEXT);
           //Graphics_flushBuffer(&g_sContext);

           displayADC = voltageADC();
           displayTemp(displayADC);

           Graphics_flushBuffer(&g_sContext);

           in_value = ADC12MEM1;  //if use scroll wheel, switch to MEM0
           //displayScroll(in_value);   //we disabled the scroll wheel for the last sign-off

           DACSetValue(in_value);//in_value
           //adc - dac records
           //2.84 - 2.95
           //3.00 - 3.06
           //3.17 - 3.21
           //0.00 - 0.36
           //0.50 - 0.98
           //0.96 - 1.36
           //1.32 - 1.67

           break;
       case 2:
           //Graphics_drawStringCentered(&g_sContext,"Square Wave",11,48,15, TRANSPARENT_TEXT);
           //Graphics_flushBuffer(&g_sContext);

           in_value = ADC12MEM0;
           //displayScroll(in_value);

           dacCodes[1] = in_value;

           while(count == last_count){
               __no_operation();
           }
           //if(last_count != count)
           DACSetValue(dacCodes[count%2]);
           last_count = count;

           break;
       case 3:

           break;
       case 4:

           break;
   }
   }
}

void DACSetValue(unsigned int dac_code)
{
     // Start the SPI transmission by asserting CS (active low)
     // This assumes DACInit() already called
     DAC_PORT_CS_OUT &= ~DAC_PIN_CS;

     // Write in DAC configuration bits. From DAC data sheet
     // 3h=0011 to highest nibble.
     // 0=DACA, 0=buffered, 1=Gain=1, 1=Out Enbl
     dac_code |= 0x3000;   // Add control bits to DAC word

     uint8_t lo_byte = (unsigned char)(dac_code & 0x00FF);
     uint8_t hi_byte = (unsigned char)((dac_code & 0xFF00) >> 8);

     // First, send the high byte
     DAC_SPI_REG_TXBUF = hi_byte;

     // Wait for the SPI peripheral to finish transmitting
     while(!(DAC_SPI_REG_IFG & UCTXIFG)) {
      _no_operation();
     }

     // Then send the low byte
     DAC_SPI_REG_TXBUF = lo_byte;

     // Wait for the SPI peripheral to finish transmitting
     while(!(DAC_SPI_REG_IFG & UCTXIFG)) {
      _no_operation();
     }

     // We are done transmitting, so de-assert CS (set = 1)
     DAC_PORT_CS_OUT |=  DAC_PIN_CS;

     // This DAC is designed such that the code we send does not
    // take effect on the output until we toggle the LDAC pin.
    // This is because the DAC has multiple outputs. This design
    // enables a user to send voltage codes to each output and
     // have them all take effect at the same time.
     DAC_PORT_LDAC_OUT &= ~DAC_PIN_LDAC;  // Assert LDAC
      __delay_cycles(10);                 // small delay
     DAC_PORT_LDAC_OUT |=  DAC_PIN_LDAC;  // De-assert LDAC
}

void DACInit(void)
{
    // Configure LDAC and CS for digital IO outputs
    DAC_PORT_LDAC_SEL &= ~DAC_PIN_LDAC;
    DAC_PORT_LDAC_DIR |=  DAC_PIN_LDAC;
    DAC_PORT_LDAC_OUT |= DAC_PIN_LDAC; // Deassert LDAC

    DAC_PORT_CS_SEL   &= ~DAC_PIN_CS;
    DAC_PORT_CS_DIR   |=  DAC_PIN_CS;
    DAC_PORT_CS_OUT   |=  DAC_PIN_CS;  // Deassert CS
}

void configButtons()  //configure the buttons
{
    P7SEL &= ~ BIT0|BIT4;  //set as digital I/O
    P3SEL &= ~ BIT6;
    P2SEL &= ~ BIT2;

    P7DIR &= ~ BIT0|BIT4;  //set as input
    P3DIR &= ~ BIT6;
    P2DIR &= ~ BIT2;

    P7REN |= BIT0|BIT4;  //enable internal resistors
    P3REN |= BIT6;
    P2REN |= BIT2;

    P7OUT |= BIT0|BIT4;  //pull-up resistors
    P3OUT |= BIT6;
    P2OUT |= BIT2;
}

float voltageADC()
{   P6DIR  = 0x01; // Set P6.1 as intput

    REFCTL0 &= ~REFMSTR;    // Reset REFMSTR to hand over control of
                            // internal reference voltages to
             // ADC12_A control registers

    ADC12CTL0 = ADC12SHT0_9 | ADC12ON;

    ADC12CTL1 = ADC12SHP | ADC12CSTARTADD_1;           // Enable sample timer

    // Using ADC12MEM0 to store reading
    ADC12MCTL1 = ADC12SREF_0 + ADC12INCH_1;

    __delay_cycles(100);                    // delay to allow Ref to settle
    ADC12CTL0 |= ADC12ENC;              // Enable conversion

    ADC12CTL0 &= ~ADC12SC;  // clear the start bit
    ADC12CTL0 |= ADC12SC;

    while (ADC12CTL1 & ADC12BUSY)
        __no_operation();
        adcValue = ADC12MEM1;
        __no_operation();
        return adcValue * 3.3 / 4096;
}

void displayScroll(unsigned int in_value)
{
    //setiing to 5v -> real value can generate is 5.02v, min is 0.598 V
    //setting to 3.3v -> 3.32v , min is 0.550 V

    float in_value2 = in_value / 1240.0;

    int disp1,disp2,disp3;
    disp1 = in_value2 / 1;
    disp2 = in_value2 * 10 / 1 - disp1 * 10;
    disp3 = in_value2 * 100 / 1 - disp2 * 10 - disp1 * 100;

    unsigned char disp[6];
    disp[0] = ' ';
    disp[1] = disp1 + 0x30;
    disp[2] = '.';
    disp[3] = disp2 + 0x30;
    disp[4] = disp3 + 0x30;
    disp[5] = ' ';

    Graphics_drawStringCentered(&g_sContext,disp,6,48,45, OPAQUE_TEXT);

}

int checkButtons()  //return a value that corresponds to pressed buttons
{
    //define local variables
    int s1 = 0;
    int s2 = 0;
    int s3 = 0;
    int s4 = 0;
    int result;

    //generate a value that corresponds to pressed buttons
    if ((P7IN & BIT0)==0) s1= BIT3;
    if ((P3IN & BIT6)==0)  s2= BIT2;
    if ((P2IN & BIT2)==0)  s3= BIT1;
    if ((P7IN & BIT4)==0)  s4= BIT0;

    result = s1|s2|s3|s4;

    return result;
}

void Timer_A2_setup(void) //when you run timer
{


    if(state == 3)
    {
        TA2CTL = TASSEL_2 + ID_0 + MC_1;
        TA2CCR0 = 350;       // 0.005s
    }
    else if(state == 4){
        TA2CTL = TASSEL_2 + ID_0 + MC_1;
        TA2CCR0 = 350; // 150Hz
    }
    else {
        TA2CTL = TASSEL_1 + ID_0 + MC_1;
        TA2CCR0 = 163;
    }

    TA2CCTL0 = CCIE;     // TA2CCR0 interrupt enabled
}

#pragma vector=TIMER2_A0_VECTOR
__interrupt void Timer_A2_ISR(void)
{

        if(state == 2)
        {
            count ++;
            DACSetValue(dacCodes[count%2]);
        }

        if(state == 3)
        {
            if(count < 4096){
                count += 100;
            DACSetValue(count);}
            else {count = 0;
            DACSetValue(count);}
        }

        if(state == 4){
            if(count < 4000)
            {
                count += 100;
                DACSetValue(count);
            }
            else if (count < 8100 && count >= 4000)
            {
                count += 100;
                DACSetValue(8100 - count);
            }
            else if (count >= 8100)
            {
            count = 0;}
        }
}

void displayADC(unsigned int dispTemp){

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

    Graphics_drawStringCentered(&g_sContext,disp5,7,48,35, OPAQUE_TEXT);
}

void displayTemp(float inAvgTempC)
{
    float dispTemp = inAvgTempC;
    float dispTemp5;
    int dispTemp1,dispTemp2,dispTemp3,dispTemp4,dispTemp6,dispTemp7,dispTemp8,dispTemp9;

    unsigned char disp3[6], disS3 = 6;

    dispTemp1 = dispTemp / 100;
    dispTemp2 = dispTemp / 10 - dispTemp1 * 10;
    dispTemp3 = dispTemp - dispTemp1 * 100 - dispTemp2 * 10;
    dispTemp4 = dispTemp * 10 - dispTemp1 * 1000 - dispTemp2 * 100 - dispTemp3 * 10;


    disp3[0] = dispTemp1 + 0x30;
    disp3[1] = dispTemp2 + 0x30;
    disp3[2] = dispTemp3 + 0x30;
    disp3[3] = '.';
    disp3[4] = dispTemp4 + 0x30;
    disp3[5] = 'V';
    Graphics_drawStringCentered(&g_sContext,disp3,disS3,48,35, OPAQUE_TEXT);
}
