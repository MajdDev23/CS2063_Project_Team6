package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    companion object {
        const val TASK_MANAGER_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigate to the customize screen
        val customizeButton = findViewById<Button>(R.id.customize_btn)
        customizeButton.setOnClickListener {
            val customizeIntent = Intent(this, CustomizeActivity::class.java)
            startActivity(customizeIntent)
        }


        // Navigate to the task manager screen
        val newTaskButton = findViewById<Button>(R.id.newTask_btn)
        newTaskButton.setOnClickListener {
            val taskManagerIntent = Intent(this, TaskManagerActivity::class.java)
            startActivityForResult(taskManagerIntent, TASK_MANAGER_REQUEST_CODE)
        }

        val cameraActivityButton = findViewById<Button>(R.id.camera_btn)
        cameraActivityButton.setOnClickListener {
            val cameraActivityIntent = Intent(this, CameraActivity::class.java)
            startActivity(cameraActivityIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TASK_MANAGER_REQUEST_CODE && resultCode == RESULT_OK) {
            val plantImageResId = data?.getIntExtra("PLANT_IMAGE", R.drawable.plant_image)
            plantImageResId?.let {
                val plantImageView = findViewById<ImageView>(R.id.plant_space) // Ensure this matches your XML
                plantImageView.setImageResource(it)
            }
        }
    }


}
