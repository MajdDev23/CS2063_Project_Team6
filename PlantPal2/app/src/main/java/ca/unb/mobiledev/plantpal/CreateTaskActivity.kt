package ca.unb.mobiledev.plantpal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateTaskActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task)

        // Find the views
        val TaskNameEditText = findViewById<EditText>(R.id.task_name_text)
        val createButton = findViewById<Button>(R.id.btn_create_task)

        // Set up the "create" button to return the subtask name to TaskManagerActivity
        createButton.setOnClickListener {
            val taskName = TaskNameEditText.text.toString()
            if (taskName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("TASK_NAME", taskName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        // Set up the back button to finish the activity
        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val backIntent = Intent(this, TaskManagerActivity::class.java)
            startActivity(backIntent)
        }
    }

}