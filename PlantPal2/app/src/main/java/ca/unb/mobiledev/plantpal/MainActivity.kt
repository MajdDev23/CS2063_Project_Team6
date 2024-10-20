package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigate to the customize screen
        val customizeButton = findViewById<Button>(R.id.customize_btn)
        customizeButton.setOnClickListener {
            val customizeIntent = Intent(this, CustomizeActivity::class.java)
            startActivity(customizeIntent)
        }

        // Navigate to the subtasks screen
        val subtasksButton = findViewById<Button>(R.id.subtasks_btn)
        subtasksButton.setOnClickListener {
            val subtasksIntent = Intent(this, SubtasksActivity::class.java)
            startActivity(subtasksIntent)
        }

        // Navigate to the task manager screen
        val newTaskButton = findViewById<Button>(R.id.newTask_btn)
        newTaskButton.setOnClickListener {
            val taskManagerIntent = Intent(this, TaskManagerActivity::class.java)
            startActivity(taskManagerIntent)
        }

        val cameraActivityButton = findViewById<Button>(R.id.camera_btn)
        newTaskButton.setOnClickListener {
            val cameraActivityIntent = Intent(this, CameraActivity::class.java)
            startActivity(cameraActivityIntent)
        }
    }
}
