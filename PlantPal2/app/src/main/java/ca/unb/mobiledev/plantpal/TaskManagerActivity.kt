package ca.unb.mobiledev.plantpal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TaskManagerActivity : AppCompatActivity() {
    private val taskList = mutableListOf<String>()
    private lateinit var adapter: TaskAdapter
    private var selectedPlantResId: Int? = null

    @SuppressLint("NotifyDataSetChanged")
    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newTaskName = result.data?.getStringExtra("TASK_NAME")
            val plantImageResId = result.data?.getIntExtra("PLANT_IMAGE", R.drawable.plant_image)
            newTaskName?.let {
                taskList.add(it)
                adapter.notifyDataSetChanged()

                saveTasks()
            }
            val mainIntent = Intent()
            mainIntent.putExtra("PLANT_IMAGE", plantImageResId)
            setResult(RESULT_OK, mainIntent)
        }
        Toast.makeText(this, "Task added.", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)

        loadTasks()

        val recyclerView = findViewById<RecyclerView>(R.id.tasks_list)

        //When the user clicks a task name, it redirects them to the subtask creation page
        adapter = TaskAdapter(taskList) { taskName ->
            // Navigate to SubtasksActivity with the task name
            val intent = Intent(this, SubtasksActivity::class.java)
            intent.putExtra("TASK_NAME", taskName)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val createTaskButton = findViewById<Button>(R.id.create_task_btn)
        createTaskButton.setOnClickListener {
            val createTaskIntent = Intent(this, CreateTaskActivity::class.java)
            addTaskLauncher.launch(createTaskIntent)
        }

        val deleteTaskButton = findViewById<Button>(R.id.delete_task_btn)
        deleteTaskButton.setOnClickListener {
            val selectedCount = adapter.getSelectedTaskCount()
            if (selectedCount == 0) {
                Toast.makeText(this, "No tasks selected!", Toast.LENGTH_SHORT).show()
            } else {
                adapter.deleteSelectedTasks()

                saveTasks()
                Toast.makeText(this, "Tasks deleted!", Toast.LENGTH_SHORT).show()
            }
        }

        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            backIntent.putExtra("PLANT_IMAGE", selectedPlantResId)
            setResult(RESULT_OK, backIntent)
            startActivity(backIntent)
            moveTaskToBack(false)
        }
    }

    private fun saveTasks() {
        val sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("TASK_LIST", taskList.joinToString(";"))
        editor.apply()
    }

    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val savedTasks = sharedPreferences.getString("TASK_LIST", null)
        if (savedTasks != null) {
            taskList.addAll(savedTasks.split(";").filter { it.isNotBlank() })
        }
    }
}
