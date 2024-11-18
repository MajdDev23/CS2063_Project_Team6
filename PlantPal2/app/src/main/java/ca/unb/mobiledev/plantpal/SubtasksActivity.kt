package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SubtasksActivity : AppCompatActivity() {

    private val subtaskList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    // Define the activity launcher for getting a result from AddSubtaskActivity
    private val addSubtaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newSubtaskName = result.data?.getStringExtra("SUBTASK_NAME")
            newSubtaskName?.let {
                subtaskList.add(it)
                adapter.notifyDataSetChanged() // Update the ListView
            }
        }
        val text = "Subtask added."
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this,text,duration)
        toast.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtasks)

        // Set up the ListView and Adapter
        val subtaskListView = findViewById<ListView>(R.id.subtasks_list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subtaskList)
        subtaskListView.adapter = adapter

        // Set up the button to create a new subtask
        val createSubtaskButton = findViewById<Button>(R.id.subtasks_btn)
        createSubtaskButton.setOnClickListener {
            // Launch AddSubtaskActivity and wait for result
            val intent = Intent(this, CreateSubtaskActivity::class.java)
            addSubtaskLauncher.launch(intent)
        }

        // Back to Main screen
        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
