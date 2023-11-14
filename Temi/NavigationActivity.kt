package com.example.group7

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlinx.android.synthetic.main.activity_navigation.*

class NavigationActivity : AppCompatActivity(), OnGoToLocationStatusChangedListener {
    lateinit var robot: Robot

    lateinit var locationListAdapter: ArrayAdapter<String>

    var locations: MutableList<String> = mutableListOf("Example Location 1", "Example Location 2", "Example Location 3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        robot = Robot.getInstance()

        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnRefresh.setOnClickListener {
            refreshLocations()
        }


        // initialize an array adapter
        locationListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, locations
        )

        // attach the array adapter with list view
        locationListView.adapter = locationListAdapter

        // list view item click listener
        locationListView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val selectedLocationItem = parent.getItemAtPosition(position)
            robot.goTo(selectedLocationItem.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnGoToLocationStatusChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnGoToLocationStatusChangedListener(this)
    }

    fun refreshLocations() {
        locations.clear()
        val robotLocations = robot.locations
        locations.addAll(robotLocations)
        locationListAdapter.notifyDataSetChanged()
    }

    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        statusText.text = "Status: $location, $status"
    }
}