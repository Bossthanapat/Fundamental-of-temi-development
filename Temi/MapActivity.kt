package com.example.group7

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.map.MapDataModel
import kotlinx.android.synthetic.main.activity_map.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class MapActivity : AppCompatActivity() {
    lateinit var robot: Robot

    private var bitmap: Bitmap? = null

    private var mapDataModel: MapDataModel? = null

    private val singleThreadExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        robot = Robot.getInstance()

        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnRefresh.setOnClickListener {
            refreshMap()
        }
        textViewMapElements.movementMethod = ScrollingMovementMethod()
        refreshMap()
    }

    private fun refreshMap() {
        progressBar.visibility = View.VISIBLE
        singleThreadExecutor.execute {
            mapDataModel = robot.getMapData() ?: return@execute
            val mapImage = mapDataModel!!.mapImage
            Log.i("mapImage.typeId", mapDataModel!!.mapImage.typeId)

            bitmap = Bitmap.createBitmap(
                mapImage.data.map { Color.argb((it * 2.55).roundToInt(), 0, 0, 0) }.toIntArray(),
                mapImage.cols,
                mapImage.rows,
                Bitmap.Config.ARGB_8888
            )
            runOnUiThread {
                progressBar.visibility = View.GONE
                textViewMapElements.text = ""
                Log.i("mapId", mapDataModel!!.mapId)
                textViewMapElements.append("Map ID: ${mapDataModel!!.mapId} \n")
                Log.i("mapInfo", mapDataModel!!.mapInfo.toString())
                textViewMapElements.append("Map Info: ${mapDataModel!!.mapInfo} \n")
                textViewMapElements.append("Green Paths: ${mapDataModel!!.greenPaths} \n")
                textViewMapElements.append("Virtual Walls: ${mapDataModel!!.virtualWalls} \n")
                imageViewMap.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        bitmap?.recycle()
        singleThreadExecutor.shutdownNow()
        super.onDestroy()
    }
}