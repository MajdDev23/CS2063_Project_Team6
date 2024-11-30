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
import androidx.compose.ui.text.intl.Locale
import java.text.SimpleDateFormat
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {
    private var selectedPlantResId: Int = R.drawable.plant_image // Default plant image
    private var selectedDueDate: String? = null // Store the selected due date
    private var selectedTimer: String? = null // Store the selected time

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

        // Sets up the button to select the due date
        selectDueDateButton.setOnClickListener {
            // Get the current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show the date picker
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

        // Sets up the timer for the due date
        selectTimerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                { _, selectedHour, selectedMinute ->
                    val formattedTime = formatTime(selectedHour, selectedMinute)
                    selectedTimer = "$selectedHour:$selectedMinute" // Store in 24-hour format
                    timerDisplay.text = "Timer: $formattedTime"
                },
                hour,
                minute,
                false // Use false for 12-hour format, true for 24-hour format
            )
            timePicker.show()
        }

        // Sets up the create task button
        createButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()

            if (taskName.isNotEmpty() && selectedDueDate != null && selectedTimer != null) {
                // Combine date and time
                val combinedDueDateStr = "$selectedDueDate $selectedTimer"

                // Parse the combined date and time
                val combinedDueDate = try {
                    SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).parse(combinedDueDateStr)?.time
                } catch (e: Exception) {
                    null
                }

                if (combinedDueDate == null || combinedDueDate <= System.currentTimeMillis() + 10000) {
                    dueDateDisplay.text = "Invalid or past due date!"
                } else {
                    val resultIntent = Intent().apply {
                        putExtra("TASK_NAME", taskName)
                        putExtra("PLANT_IMAGE", selectedPlantResId)
                        putExtra("DUE_DATE", combinedDueDateStr) // Send the full date-time string
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
            } else {
                if (selectedDueDate == null || selectedTimer == null) {
                    dueDateDisplay.text = "Please select both due date and time!"
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
}
