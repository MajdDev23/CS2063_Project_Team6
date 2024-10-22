package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class TaskManagerActivity : AppCompatActivity() {
    private val taskList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>


    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newTaskName = result.data?.getStringExtra("TASK_NAME")
            newTaskName?.let {
                taskList.add(it)
                adapter.notifyDataSetChanged() // Update the ListView
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)

        // Set up the ListView and Adapter
        val taskListView = findViewById<ListView>(R.id.tasks_list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        taskListView.adapter = adapter

        // Set up the button to create a new task
        val createTaskButton = findViewById<Button>(R.id.create_task_btn)
        createTaskButton.setOnClickListener {
            val createTaskIntent = Intent(this, CreateTaskActivity::class.java)
            addTaskLauncher.launch(createTaskIntent)
        }
        // Back to Main screen
        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
            moveTaskToBack(false)
        }
    }


}
