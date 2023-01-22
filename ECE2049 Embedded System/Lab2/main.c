#include <msp430.h>
#include <stdlib.h>
#include "peripherals.h"   //include the libraries
#define MAX 48     //define a marco variable MAX that stores the number of notes of the song, which is 48

//declare function prototypes
void configButtons();
int checkButtons();
void configUserLED(char inbits);
void swDelay(char numLoops, int s);

//declare a struct, Note, that has three fields
typedef struct{
    int pitch;
    int duration;
    char led;
} Note;

//global variables
int count = 0;   //timer
int leap_cnt = 0;    //timer leap counting
bool reset;
bool isLose = false;

// Main
void main(void)

{
    unsigned char disP[3], disS = 3;
    WDTCTL = WDTPW | WDTHOLD;   // stop watchdog timer

    //local variables
    int display;
    int counter = 1;
    char currKey;
    int state = 0, i;

    //define notes
    Note A2 = {440,200,'1'};
    Note B2 = {494,200,'2'};
    Note C3 = {523,200,'4'};
    Note D3 = {587,200,'8'};
    Note E3 = {659,200,'1'};
    Note F3 = {698,200,'2'};
    Note G3 = {784,200,'4'};
    Note A3 = {880,200,'8'};

    //define a song, which is an array of notes
    Note song[MAX] = {G3,A3,G3,F3,E3,F3,G3,D3,E3,F3,E3,F3,G3,G3,A3,G3,F3,E3,F3,G3,D3,G3,E3,C3,
                      G3,A3,G3,F3,E3,F3,G3,D3,E3,F3,E3,F3,G3,G3,A3,G3,F3,E3,F3,G3,D3,G3,E3,C3};
    //assign different elements of the song with different durations
    song[6].duration = 400;
    song[9].duration = 400;
    song[12].duration = 400;
    song[19].duration = 400;
    song[20].duration = 400;
    song[21].duration = 400;
    song[23].duration = 400;
    song[30].duration = 400;
    song[33].duration = 400;
    song[36].duration = 400;
    song[43].duration = 400;
    song[44].duration = 400;
    song[45].duration = 400;
    song[47].duration = 400;


     // Useful code starts here
    initLeds();
    configDisplay();
    configKeypad();
    configButtons();
    _BIS_SR(GIE);  //enable interrupts

    // *** Intro Screen ***
    Graphics_clearDisplay(&g_sContext); // Clear the display

    // Draw a box around everything because it looks nice
    Graphics_Rectangle box = {.xMin = 5, .xMax = 91, .yMin = 5, .yMax = 91 };
    Graphics_drawRectangle(&g_sContext, &box);
    Graphics_flushBuffer(&g_sContext);

    disP[0] = ' ';
    disP[2] = ' ';

    while(1){

        disP[1] = checkButtons() + 0x30;  //display the value that the pressed button correspond
        Graphics_drawStringCentered(&g_sContext,disP,disS,48,55, OPAQUE_TEXT);
        Graphics_flushBuffer(&g_sContext);
        configUserLED(checkButtons() + 0x30);  //light up the LED according to the pressed button


           configKeypad();
           currKey = getKey();  //store the entered key into a variable currKey

           if(currKey == '*'){
                           state = 1;}   //when '*' is entered, switch to case 1

           switch(state){
            case 0://welcome screen
                currKey = '0';
                Graphics_drawStringCentered(&g_sContext, "Hi MSP430 Hero", AUTO_STRING_LENGTH, 48, 15, TRANSPARENT_TEXT);
                Graphics_drawStringCentered(&g_sContext, ":^))))", AUTO_STRING_LENGTH, 48, 25, TRANSPARENT_TEXT);
                Graphics_flushBuffer(&g_sContext);
                configKeypad();
                currKey = getKey();
                if(currKey == '*'){   //count-down

                    for( i = 3; i > 0; i --){
                        disP[1] = i + 0x30;
                        Graphics_drawStringCentered(&g_sContext,disP,disS,48,45, OPAQUE_TEXT);
                        Graphics_flushBuffer(&g_sContext);
                        configUserLED(i + 0x30);
                        Timer_A2_setup();  //start the timer
                        while (count <= 200) {}  //wait for 1 second
                        stopTimerA2(true);
                    }

                    Graphics_drawStringCentered(&g_sContext,"GO!",AUTO_STRING_LENGTH, 48, 35, TRANSPARENT_TEXT);
                    Graphics_flushBuffer(&g_sContext);
                    configUserLED('G');  //turn on both LEDs
                    swDelay(1,1);
                    Graphics_clearDisplay(&g_sContext); // Clear the display
                    state = 1;
                }

                break;

            case 1:
                configButtons();
                Graphics_drawStringCentered(&g_sContext, "Song play", AUTO_STRING_LENGTH, 48, 15, TRANSPARENT_TEXT);
                Graphics_flushBuffer(&g_sContext);
                swDelay(1,1);

                for( i = 0 ; i < MAX; i++){
                    int play = song[i].pitch;
                    Timer_A2_setup();  //start the timer
                    while(count <= song[i].duration)
                    {
                        BuzzerOnSet(play);  //play the sound of the i th element of the song
                        setLeds(song[i].led);  //light up the LED that corresponds to the i th element of the song
                        if (count >= 150){  //checking user input
                        if (song[i].led != checkButtons() + 0x30)
                        {   isLose = true;
                            BuzzerOff();  //turn off the buzzer
                            stopTimerA2(true);  //stop the timer
                            break;  //if the user input doesn't match up, break the if loop
                        }
                        if(isLose) break;
                        } //break the while loop
                    }
                    if(isLose) break; //break the for loop
                    BuzzerOff();  //turn off the buzzer
                    stopTimerA2(true);   //stop the timer

                }

                Graphics_clearDisplay(&g_sContext);
                if(isLose){ state = 2;}  //if the user input doesn't match up, switch to loser page
                else state = 3;  //if the for loop is completed, swtich to winner page

                break;

            case 2:  //loser humiliation case
                Graphics_drawStringCentered(&g_sContext, "HAHA LOSER", AUTO_STRING_LENGTH, 48, 15, TRANSPARENT_TEXT);
                Graphics_flushBuffer(&g_sContext);
                currKey = getKey();
                if(currKey == '#'){  //press '#' to restart
                    Graphics_clearDisplay(&g_sContext); // Clear the display
                    isLose = false;
                    state =0;
                    break;
                }
            case 3:  //winner celebration case
                Graphics_drawStringCentered(&g_sContext, "Guitar hero!!", AUTO_STRING_LENGTH, 48, 15, TRANSPARENT_TEXT);
                                Graphics_flushBuffer(&g_sContext);
                                currKey = getKey();
                                if(currKey == '#'){  //press '#' to restart
                                    Graphics_clearDisplay(&g_sContext); // Clear the display
                                    isLose = false;
                                    state =0;
                                    break;}
           }



    }
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

void configUserLED(char inbits)  //configure the user LEDs
{
    P4SEL &= ~BIT7;  //set as digital I/O
    P1SEL &= ~BIT0;

    P4DIR |= BIT7;  //set as output
    P1DIR |= BIT0;

    if(inbits == 0x31 | inbits == 0x33 | inbits == 'G') // G means go
    {
        P1OUT |= BIT0;
    } else
    P1OUT &= ~BIT0;
    if(inbits == 0x32 | inbits == 'G')
    {
        P4OUT |= BIT7;
    } else
        P4OUT &= ~BIT7;

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

void Timer_A2_setup(void) //when you run timer
{
        TA2CTL = TASSEL_1 + ID_0 + MC_1;
        TA2CCR0 = 163;       // 0.005s
        TA2CCTL0 = CCIE;     // TA2CCR0 interrupt enabled
}

void stopTimerA2(bool reset) //when you run timer
{
        TA2CTL = MC_0;
        TA2CCTL0 &= ~CCIE; // TA2CCR0 interrupt disabled
        if (reset)
        {
            count = 0;  //reset the timer
        }
}

#pragma vector=TIMER2_A0_VECTOR
__interrupt void Timer_A2_ISR(void)
{
    // count are Global unsigned integers
    //leap counting every 1024 leaps
    if ( leap_cnt < 1024)
    {  count ++;
            leap_cnt ++;  }
    else
    {  count += 2;
        leap_cnt = 0;  }

}
