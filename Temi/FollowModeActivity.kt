package com.example.group7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener

class FollowModeActivity : AppCompatActivity(), OnBeWithMeStatusChangedListener {
    lateinit var robot: Robot
    lateinit var followModeStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_mode)

        robot = Robot.getInstance()

        followModeStatus = findViewById(R.id.followModeStatus)

        val btnFollow = findViewById<Button>(R.id.btnFollow)
        btnFollow.setOnClickListener {
            robot.beWithMe()
        }
        val btnStopFollow = findViewById<Button>(R.id.btnStopFollow)
        btnStopFollow.setOnClickListener {
            robot.stopMovement()
        }

        val btnMainPage = findViewById<Button>(R.id.btnMainPage)
        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnBeWithMeStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnBeWithMeStatusChangedListener(this)
    }

    override fun onBeWithMeStatusChanged(status: String) {
        followModeStatus.text = "Follow Mode Status: $status"
    }
}