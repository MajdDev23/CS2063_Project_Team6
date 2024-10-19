package ca.unb.mobiledev.plantpal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CreateSubtaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_subtask)

        // Find the views
        val subtaskNameEditText = findViewById<EditText>(R.id.subtask_text)
        val createButton = findViewById<Button>(R.id.create_btn)

        // Set up the "create" button to return the subtask name to SubtasksActivity
        createButton.setOnClickListener {
            val subtaskName = subtaskNameEditText.text.toString()
            if (subtaskName.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("SUBTASK_NAME", subtaskName)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        // Set up the back button to finish the activity
        val backButton = findViewById<Button>(R.id.back_btn)
        backButton.setOnClickListener {
            finish()
        }
    }
}
