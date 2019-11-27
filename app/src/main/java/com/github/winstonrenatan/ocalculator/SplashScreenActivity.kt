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
import android.os.Handler
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.loader.content.CursorLoader
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Set splash package to do animation
        splashPackage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_in))
        //Set the splash_in
        Handler().postDelayed({
            splashPackage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.splash_out))
            //Set the splash_out
            Handler().postDelayed({
                //Things to do after the splash_out animation is done (unshow the splash screen and start MainActivity)
                splashPackage.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 1000)
        }, 500)
    }
}
