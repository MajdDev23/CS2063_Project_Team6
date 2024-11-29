package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

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
            val plantImageView = findViewById<ImageView>(R.id.plant_space)
            val plantImageResId = data?.getIntExtra("PLANT_IMAGE", R.drawable.plant_image)
            val creationDate = data?.getLongExtra("CREATION_DATE", System.currentTimeMillis())
            val dueDateStr = data?.getStringExtra("DUE_DATE")

            // Set the initial plant image
            plantImageResId?.let { plantImageView.setImageResource(it) }

            // Parse the due date
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dueDate = sdf.parse(dueDateStr)?.time ?: return

            // Calculate durations
            val totalDuration = dueDate - (creationDate ?: System.currentTimeMillis())
            val firstMilestone = totalDuration / 3
            val secondMilestone = (2 * totalDuration) / 3
            val finalMilestone = totalDuration - TimeUnit.DAYS.toMillis(1)

            // Update plant image at each milestone
            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed({
                plantImageView.setImageResource(R.drawable.plant_alt1) // Update to 2nd stage
            }, firstMilestone)

            handler.postDelayed({
                plantImageView.setImageResource(R.drawable.plant_alt2) // Update to 3rd stage
            }, secondMilestone)

            handler.postDelayed({
                plantImageView.setImageResource(R.drawable.plant_alt3) // Update to wilting stage
            }, finalMilestone)


        }
    }


}
