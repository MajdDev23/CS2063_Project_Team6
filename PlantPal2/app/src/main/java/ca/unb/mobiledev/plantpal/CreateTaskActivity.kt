package ca.unb.mobiledev.plantpal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {
    private var selectedPlantResId: Int = R.drawable.plant_image // Default plant image
    private var selectedDueDate: String? = null // Store the selected due date
    private var selectedTimer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_task)

        // Find views
        val taskNameEditText = findViewById<EditText>(R.id.task_name_text)
        val createButton = findViewById<Button>(R.id.btn_create_task)
        val selectDueDateButton = findViewById<Button>(R.id.select_due_date_btn)
        val dueDateDisplay = findViewById<TextView>(R.id.due_date_display)
        val selectTimerButton = findViewById<Button>(R.id.select_timer_btn)
        val timerDisplay = findViewById<TextView>(R.id.timer_display)
        // Set up "Select Due Date" button
        selectDueDateButton.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show DatePickerDialog
            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDueDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    dueDateDisplay.text = "Due Date: $selectedDueDate"
                },
                year,
                month,
                day
            )
            datePicker.show()
        }
        selectTimerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val formattedTime = formatTime(selectedHour, selectedMinute)
                    selectedTimer = formattedTime
                    timerDisplay.text = "Timer: $selectedTimer"
                },
                hour,
                minute,
                false // Use false for 12-hour format, true for 24-hour format
            )
            timePicker.show()
        }
        // Set up "Create Task" button
        createButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            if (taskName.isNotEmpty() && selectedDueDate != null) {
                val currentDate = System.currentTimeMillis()

                // nick add
                saveTaskToSharedPreferences(taskName, selectedDueDate!!)

                val resultIntent = Intent().apply {
                    putExtra("TASK_NAME", taskName)
                    putExtra("PLANT_IMAGE", selectedPlantResId)
                    putExtra("CREATION_DATE", currentDate)
                    putExtra("DUE_DATE", selectedDueDate)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                if (selectedDueDate == null) {
                    dueDateDisplay.text = "Please select a due date!"
                }
            }
        }

        // Back button to return to TaskManagerActivity
        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val adjustedHour = if (hour % 12 == 0) 12 else hour % 12
        return String.format("%d:%02d %s", adjustedHour, minute, amPm)
    }

    private fun saveTaskToSharedPreferences(taskName: String, dueDate: String) {
        val sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val savedTasks = sharedPreferences.getString("TASK_LIST", null)
        val taskList = mutableListOf<String>()

        if (savedTasks != null) {
            taskList.addAll(savedTasks.split(";").filter { it.isNotBlank() })
        }

        val newTask = "$taskName|$dueDate"
        taskList.add(newTask)

        editor.putString("TASK_LIST", taskList.joinToString(";"))
        editor.apply()
    }
}