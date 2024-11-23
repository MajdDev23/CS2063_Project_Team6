package ca.unb.mobiledev.plantpal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class SubtasksActivity : AppCompatActivity() {

    private val subtaskList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private val addSubtaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newSubtaskName = result.data?.getStringExtra("SUBTASK_NAME")
            newSubtaskName?.let {
                subtaskList.add(it)
                adapter.notifyDataSetChanged()
            }
            Toast.makeText(this, "Subtask added.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtasks)

        val taskName = intent.getStringExtra("TASK_NAME")
        val titleTextView = findViewById<TextView>(R.id.subtask_title)
        titleTextView.text = "Subtasks for: $taskName"

        val subtaskListView = findViewById<ListView>(R.id.subtasks_list)
        adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subtaskList) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)

                // Change the text color for each subtask to #4A8C61
                textView.setTextColor(Color.parseColor("#4A8C61"))
                return view
            }
        }
        subtaskListView.adapter = adapter

        val createSubtaskButton = findViewById<Button>(R.id.subtasks_btn)
        createSubtaskButton.setOnClickListener {
            val intent = Intent(this, CreateSubtaskActivity::class.java)
            addSubtaskLauncher.launch(intent)
        }

        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            finish()
        }
    }
}
