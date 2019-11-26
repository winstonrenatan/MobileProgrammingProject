package com.github.winstonrenatan.ocalculator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.loader.content.CursorLoader
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    //Initiating variables
    var currentPath: String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE = 2
    //Optical Character Recognizer
    private lateinit var textRecognizer: TextRecognizer
    //Text To Speech
    lateinit var TTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initiating TTS
        TTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                //If there is no error the set the language
                TTS.language = Locale.US
            }
        })

        //Listener for buttons
        button_camera.setOnClickListener {
            dispatchCameraIntent()
        }
        button_gallery.setOnClickListener{
            dispatchGalleryIntent()
        }
        button_detect_text.setOnClickListener {
            detectTextFromImage()
        }
        button_calculate.setOnClickListener {
            calculateEquations()
        }
        button_speak.setOnClickListener {
            speakDetectedText()
        }
        button_stop.setOnClickListener {
            stopReadingText()
        }
    }

    //Either take picture or select picture is selected
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val file = File(currentPath)
                val uri = Uri.fromFile(file)
                imageView.setImageURI(uri)
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                /*
                val file = File(uri.getPath())
                currentPath = file.getPath()
                 */
                imageView.setImageURI(uri)
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //Function for camera to take picture
    fun dispatchCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            try {
                photoFile = createImage()
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
            if (photoFile!=null) {
                //YOU MUST CREATE A CONTENT PROVIDER MATCHING THE AUTHORITY
                var photoUri = FileProvider.getUriForFile(this,
                    "com.github.winstonrenatan.ocalculator.fileprovider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, TAKE_PICTURE)
            }
        }
    }

    //Function to create and store picture
    fun createImage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd__HHmmss").format(Date())
        val imageName = "JPEG_"+timeStamp+"_"
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName, ".jpg", storageDir)
        currentPath = image.absolutePath
        return image
    }

    //Function to access the gallery
    fun dispatchGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE)
    }

    //Function to detect what is the text in the given picture
    fun detectTextFromImage() {
        //Change Image to Bitmap Type
        var bitmap = BitmapFactory.decodeFile(currentPath)
        var frame = Frame.Builder().setBitmap(bitmap).build()

        //  Create Text Recognizer
        textRecognizer = TextRecognizer.Builder(this).build()

        if (!textRecognizer.isOperational) {
            Toast.makeText(this, "Could not get the Text", Toast.LENGTH_SHORT).show()
        }
        else {
            // Detector Processor (To make available camera to text)
            val items = textRecognizer.detect(frame)
            val stringBuilder = StringBuilder()
            for (i in 0 until items.size()) {
                val item = items.valueAt(i)
                stringBuilder.append(item.value)
                stringBuilder.append("\n")
            }
            //Display the text detected
            detected_words.text = stringBuilder.toString()
        }
    }

    //Function to call calculator to calculate result of detected text
    fun calculateEquations() {
        //Change the detected text to string for next process
        val stringInput = detected_words.text.toString()
        //To see whether there is equation or not in the string
        var containEquation = false
        //Record the location of the equation for next step
        var equationLocation = 0
        //Search for equation in the detected text
        for (i in stringInput.indices) {
            if(stringInput[i]=='+' || stringInput[i]=='-' || stringInput[i]=='*' || stringInput[i]=='/') {
                equationLocation = i
                containEquation=true
                break
            }
            else {
                containEquation=false
            }
        }
        //To determine things to do whether there is equation or not
        //Enable calculator if there is an equation
        if (containEquation==true) {
            calculator(stringInput, equationLocation)
        }
        //Tell user that there is no equation to be calculated
        else {
            Toast.makeText(this, "There is no equation detected.", Toast.LENGTH_SHORT).show()
        }
    }

    //Function that acts like somewhat calculator
    private fun calculator(stringInput: String, equationLocation: Int) {
        // Get the length of the string to do logical things
        val stringLength = stringInput.length
        // Do the simple math equation (addition, substitution, multiplication, and division)
        if(stringInput[equationLocation]=='+') {
            var numStringA:String = stringInput.subSequence(0,equationLocation).toString()
            var numStringB:String = stringInput.subSequence(equationLocation+1,stringLength-1).toString()
            var numA = numStringA.toInt()
            var numB = numStringB.toInt()
            var result = numA + numB
            Toast.makeText(this,"$numA + $numB = $result", Toast.LENGTH_SHORT).show()
        }
        else if(stringInput[equationLocation]=='-') {
            var numStringA:String = stringInput.subSequence(0,equationLocation).toString()
            var numStringB:String = stringInput.subSequence(equationLocation+1,stringLength-1).toString()
            var numA = numStringA.toInt()
            var numB = numStringB.toInt()
            var result = numA - numB
            Toast.makeText(this,"$numA - $numB = $result", Toast.LENGTH_SHORT).show()
        }
        else if(stringInput[equationLocation]=='*') {
            var numStringA:String = stringInput.subSequence(0,equationLocation).toString()
            var numStringB:String = stringInput.subSequence(equationLocation+1,stringLength-1).toString()
            var numA = numStringA.toInt()
            var numB = numStringB.toInt()
            var result = numA * numB
            Toast.makeText(this,"$numA * $numB = $result", Toast.LENGTH_SHORT).show()
        }
        else if(stringInput[equationLocation]=='/') {
            var numStringA:String = stringInput.subSequence(0,equationLocation).toString()
            var numStringB:String = stringInput.subSequence(equationLocation+1,stringLength-1).toString()
            var numA = numStringA.toDouble()
            var numB = numStringB.toDouble()
            var result = numA / numB
            Toast.makeText(this,"$numA / $numB = $result", Toast.LENGTH_SHORT).show()
        }
    }

    //Function to speak the text inside the textView
    fun speakDetectedText() {
        val toSpeak = detected_words.text.toString()
        if (toSpeak == "") {
            //If there is no text
            Toast.makeText(this, "Enter the text please.", Toast.LENGTH_SHORT).show()
        }
        else {
            //If there is text
            TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    //Function to stop reading and start over again when speak is clicked
    fun stopReadingText() {
        //If speaking then stop
        if (TTS.isSpeaking) {
            TTS.stop()
        }
        //If not speaking
        else {
            Toast.makeText(this, "Currently app is not speaking.", Toast.LENGTH_SHORT).show()
        }
    }

    //Function to stop reading when app is onPause
    override fun onPause() {
        //If speaking then stop
        if (TTS.isSpeaking) {
            TTS.stop()
        }
        super.onPause()
    }
}
