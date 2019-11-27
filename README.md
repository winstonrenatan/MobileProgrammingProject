# OCalculator
This repository is created to explain our project on Mobile Programming 2019 on our major, Informatics. OCalculatoR comes from the word OCR (Optical Character Recognition) and Calculator, being joined together. The goal of this application is to identify what text is given through pictures captured from user's camera. This application will be able to convert the text photo to digital text, which the digital text can be calculated and turn into voice (being read). 

## Authors
Alessandro Luiz Kartika (01082170029)<br>
Winston Renatan (01082170030)<br>
Informatics 2017, Universitas Pelita Harapan Main Campus

## Dependencies
Mobile Vision an API developed by Google.<br>
implementation 'com.google.android.gms:play-services-vision:19.0.0'

## How to use OCalculatoR?
1. Take a picture from "CAMERA" button.<br>
2. Click on "DETECT TEXT" button to see what words are contained in the picture captured.<br>
   The detected words will then appear in a space below all the buttons.<br>
3. If there is a simple equation (one equation) on the detected text, you can press on "CALCULATE" button, to see the result.<br>
   If there is <b>no equation</b> found on the detected words space, this function will not work.<br>
    ```
    Allowed equations are +, -, *, / 
    Sample on simple mathematics equation:
    123+987
    145-23
    146*2
    365/5
    ```
4. You also can hear what the words are using the "SPEAK" button and stop it using "STOP" button.<br>
   If there is <b>no words</b> on the detected words space, there is no sound that is created.
   
## Functionalities
### Capture
The first and important feature of this application is capturing photos. Capture photos is required so that the application can start to identify the text that is contained in the text. Capture functionalities can be accessed with the "CAMERA" button in the main page. That will direct to a page that comes with camera. Here after we capture the photos, we can choose either to use the picture taken or retake the picture.<br>

### Detect Text
After we finish capturing the image we wanted, we can start to identify the texts that is contained in the image. This feature can be accessed through "DETECT TEXT" button. If there is a text inside the image taken, it will show up in the given space. On the other hand, if there is no text captured from the image there will be nothing to display and there is a toast that will be shown for the user. Meanwhile, if there is no captured image (on the start of the application) there will be an error message shown that there is no photos taken. <br>

### Calculate
This feature is accessible once we have done the two previous features, which is capture and detect text. This feature is accessed through the "CALCULATE" button. This feature will result a message that shows up to the user of the equation done and its result. If there is no equation detected, there will be a message that showed up there is no identified text. The calculate function is only for simple equation (one equation or symbol) as mentioned above. There is also some limitation to the calculation, for multiplication that uses starts from 6 digits * 6 digits this will result an error as it passes the Integer limit. For subtraction it will result to a program exit on 10 digits - 10 digits.<br>
For the typewritten (Times New Roman) text numbers, it has an average of 92,5% success rate (scanning and calculation). This is tested starting from 1 digit (equation) 1 digit to 10 digits (equation) 10 digits. "equation" as mentioned before interchanged from `+, -, *, /` with all the numbers being randomed.  For the OCR it all works on any equation and for some equation that is mentioned before, it gives the wrong answer (random number) and also turned off the application.<br>

### Speak
This feature can be tried on with "SPEAK" button even on the start of the application. This function will give out the sound of the detected text. On the other hand, this will give an error message to the user if there is no text in the space detected. For the developers, we think that the TTS (Text to Speech) is not natural enough as spoken by person.<br>

### Stop
This feature can be accessed using the "STOP" button to stop the voices that is currently readed. This function is not the same as pausing the sound, but it will stop the sound being read. After stop, and doing the speak function it will start all over again and not continuing from the previous stop place.<br>
