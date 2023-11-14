package com.example.group7

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import kotlinx.android.synthetic.main.activity_speech.*

class SpeechActivity : AppCompatActivity(), Robot.TtsListener {
    lateinit var robot: Robot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech)

        robot = Robot.getInstance()

        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnSpeak.setOnClickListener {
            val speakText = speakTextInput.text.toString()
            if (speakText != "") {
                val speak =
                    TtsRequest.create(speakText, language = TtsRequest.Language.SYSTEM, showAnimationOnly = false)
                robot.speak(speak)
            }
        }

        btnENSpeak.setOnClickListener {
            val speakENText = speakENTextInput.text.toString()
            if (speakENText != "") {
                val speak =
                    TtsRequest.create(speakENText, language = TtsRequest.Language.TH_TH, showAnimationOnly = false)
                robot.speak(speak)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addTtsListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeTtsListener(this)
    }

    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        var status = statusText.text
        status = "$status \n ${ttsRequest.status}"
        statusText.text = status
    }
}