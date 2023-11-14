package com.example.group7

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnRobotLiftedListener
import kotlinx.android.synthetic.main.activity_movement.*

class MovementActivity : AppCompatActivity(), OnRobotLiftedListener {
    lateinit var robot: Robot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movement)

        robot = Robot.getInstance()

        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnTurnBy.setOnClickListener {
            val turnByDegree = turnByInput.text.toString().toIntOrNull()
            if (turnByDegree != null) {
                robot.turnBy(turnByDegree.coerceIn(-359, 359))
            }
        }

        btnTiltAngle.setOnClickListener {
            val tiltAngleDegree = tiltAngleInput.text.toString().toIntOrNull()
            if (tiltAngleDegree != null) {
                robot.tiltAngle(tiltAngleDegree.coerceIn(-15, 45))
            }
        }

        btnTiltBy.setOnClickListener {
            val tiltByDegree = tiltByInput.text.toString().toIntOrNull()
            if (tiltByDegree != null) {
                robot.tiltBy(tiltByDegree.coerceIn(-10, 10))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnRobotLiftedListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotLiftedListener(this)
    }

    override fun onRobotLifted(isLifted: Boolean, reason: String) {
        var status = "FALSE"
        if (isLifted) {
            status = "TRUE"
        }
        statusText.text = "Lifting Status: $status"
    }
}