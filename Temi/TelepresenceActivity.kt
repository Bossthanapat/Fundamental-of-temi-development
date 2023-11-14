package com.example.group7

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.robotemi.sdk.Robot
import com.robotemi.sdk.constants.Platform
import com.robotemi.sdk.listeners.OnTelepresenceEventChangedListener
import com.robotemi.sdk.model.CallEventModel
import kotlinx.android.synthetic.main.activity_telepresence.*

class TelepresenceActivity : AppCompatActivity(), OnTelepresenceEventChangedListener {
    lateinit var robot: Robot

    lateinit var userListAdapter: ArrayAdapter<String>

    var users: MutableList<String> = mutableListOf("Example User 1", "Example User 2", "Example User 3")
    var usersMap: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telepresence)

        robot = Robot.getInstance()

        btnMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        btnRefresh.setOnClickListener {
            refreshUsers()
        }

        // initialize an array adapter
        userListAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, users
        )

        // attach the array adapter with list view
        userListView.adapter = userListAdapter

        // list view item click listener
        userListView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val selectedUserItem = parent.getItemAtPosition(position)
            val userId = usersMap[selectedUserItem.toString()]
            if (userId != null) {
                robot.startTelepresence("Test Name", userId, Platform.MOBILE)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnTelepresenceEventChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnTelepresenceEventChangedListener(this)
    }

    override fun onTelepresenceEventChanged(callEventModel: CallEventModel) {
        var status = statusText.text
        if (callEventModel.state == CallEventModel.STATE_STARTED) {
            status = "$status STARTED"
        } else if (callEventModel.state == CallEventModel.STATE_ENDED) {
            status = "$status ENDED, "
        }
        statusText.text = status
    }

    fun refreshUsers() {
        users.clear()
        val robotContacts = robot.allContact
        for (contact in robotContacts) {
            users.add("${contact.name}")
            usersMap[contact.name] = contact.userId
        }
        userListAdapter.notifyDataSetChanged()
    }
}