package ca.unb.mobiledev.plantpal

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TaskManagerActivity : AppCompatActivity() {
    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter
    private var selectedPlantResId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)

        //Loads up the saved tasks
        loadTasks()
        val recyclerView = findViewById<RecyclerView>(R.id.tasks_list)


        adapter = TaskAdapter(taskList) { task ->
            val intent = Intent(this, SubtasksActivity::class.java)
            intent.putExtra("TASK_NAME", task.name) // Pass the task's name
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Button to create tasks
        val createTaskButton = findViewById<Button>(R.id.create_task_btn)
        createTaskButton.setOnClickListener {
            val createTaskIntent = Intent(this, CreateTaskActivity::class.java)
            addTaskLauncher.launch(createTaskIntent)
        }

        //Button to delete tasks
        val deleteTaskButton = findViewById<Button>(R.id.delete_task_btn)
        deleteTaskButton.setOnClickListener {
            val selectedCount = adapter.getSelectedTaskCount()
            if (selectedCount == 0) {
                Toast.makeText(this, "No tasks selected!", Toast.LENGTH_SHORT).show()
            } else {
                adapter.deleteSelectedTasks()
                saveTasks() //Saves tasks
                Toast.makeText(this, "Tasks deleted!", Toast.LENGTH_SHORT).show()
            }
        }

        //Button to return to main menu
        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            backIntent.putExtra("PLANT_IMAGE", selectedPlantResId)
            setResult(RESULT_OK, backIntent)
            startActivity(backIntent)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private val addTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newTaskName = result.data?.getStringExtra("TASK_NAME")
            val plantImageResId = result.data?.getIntExtra("PLANT_IMAGE", R.drawable.plant_image)
            val dueDateString = result.data?.getStringExtra("DUE_DATE")
            val dueDate = try {
                // Initialize the due date variable to hold both the DATE and the TIME for the due date of the task
                //Using a SimplDateFormat to parse the text into a date.
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dueDateString)?.time
            } catch (e: Exception) {
                null
            }

            //if the due date is empty OR if the due date is set to before the current date notify the user.
            if (dueDate == null || dueDate <= System.currentTimeMillis() + 10000) {
                Toast.makeText(this, "Invalid or past due date.", Toast.LENGTH_SHORT).show()
            } else { //Otherwise add the task to the list and set the alarm.
                val task = Task(newTaskName!!, dueDate)
                taskList.add(task)
                adapter.notifyDataSetChanged()

                setAlarm(newTaskName, dueDate)
                Toast.makeText(this, "Task added.", Toast.LENGTH_SHORT).show()
                saveTasks()
            }


            val mainIntent = Intent()
            mainIntent.putExtra("PLANT_IMAGE", plantImageResId)
            setResult(RESULT_OK, mainIntent)
        }
    }

    //Function for setting the alarm for a task.
    private fun setAlarm(taskName: String, dueDate: Long) {
        Log.d("TaskManager", "Setting alarm for $taskName at $dueDate")

        //Check to see if exact alarm permissions have been granted to the app.
        //if not, the app will notify the user. Otherise the app will run normally.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Exact alarm permission not granted.", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
                return
            }
        }

        //Uses AlarmManager to setup the alarm.
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("TASK_NAME", taskName)
        }


        //Assigns a request code to tasks depending on the name,
        //This helps in organizing multiple alarms incase the user creates
        //multiple tasks.
        val requestCode = taskName.hashCode()

        //A pending intent that can be executed at a later time.
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Schedules the alarm to go off at a specific time given the due date and the pending intent.
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dueDate, pendingIntent)
        Log.d("TaskManager", "Alarm set for task: $taskName at $dueDate")
        Toast.makeText(this, "Alarm set for $taskName", Toast.LENGTH_SHORT).show()
    }


    //Function to save tasks using shared preferences
    private fun saveTasks() {
        val sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val tasksString = taskList.joinToString(";") { "${it.name},${it.dueDate}" }
        editor.putString("TASK_LIST", tasksString)
        editor.apply()
    }

    //Function to load up the saved tasks using shared preferences
    private fun loadTasks() {
        val sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val savedTasks = sharedPreferences.getString("TASK_LIST", null)
        savedTasks?.split(";")?.forEach { taskString ->
            val taskParts = taskString.split(",")
            if (taskParts.size == 2) {
                val name = taskParts[0]
                val dueDate = taskParts[1].toLongOrNull()
                if (dueDate != null) {
                    taskList.add(Task(name, dueDate))
                }
            }
        }
    }
    //Data class which holds the name and the due date for the Task.
    data class Task(
        val name: String, // Task name
        val dueDate: Long // Due date in milliseconds
    )
}

