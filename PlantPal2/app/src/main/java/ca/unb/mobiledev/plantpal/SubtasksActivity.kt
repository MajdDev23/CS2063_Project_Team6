package ca.unb.mobiledev.plantpal

import android.content.Context
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SubtasksActivity : AppCompatActivity() {

    private val subtaskList = mutableListOf<String>()
    private lateinit var adapter: SubtaskAdapter
    private val taskName: String by lazy { intent.getStringExtra("TASK_NAME") ?: "" }
    private val subtaskMap = mutableMapOf<String, MutableList<String>>()

    private val addSubtaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newSubtaskName = result.data?.getStringExtra("SUBTASK_NAME")
            newSubtaskName?.let {
                subtaskList.add(it)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Subtask added.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtasks)
        loadSubTasks()

        val titleTextView = findViewById<TextView>(R.id.subtask_title)
        titleTextView.text = "Subtasks for: $taskName"

        val recyclerView = findViewById<RecyclerView>(R.id.subtasks_recycler_view)
        adapter = SubtaskAdapter(subtaskList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupSwipeToDelete(recyclerView)

        val createSubtaskButton = findViewById<Button>(R.id.subtasks_btn)
        createSubtaskButton.setOnClickListener {
            val intent = Intent(this, CreateSubtaskActivity::class.java)
            addSubtaskLauncher.launch(intent)
        }

        val backButton = findViewById<Button>(R.id.home_btn)
        backButton.setOnClickListener {
            val backIntent = Intent(this, TaskManagerActivity::class.java)
            saveSubTasks()
            finish()
            startActivity(backIntent)
        }
    }

    //function to let user swipe left to delete subtasks
    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            //When the app registers a swipe gesture from the user, moves the subtask item to the left
            //and it removes the subtask from the list
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeItem(position) // Call the adapter's removeItem method
                Toast.makeText(this@SubtasksActivity, "Subtask deleted.", Toast.LENGTH_SHORT).show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    private fun saveSubTasks() {
        val sharedPreferences = getSharedPreferences("SubTaskPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        subtaskMap[taskName] = subtaskList
        editor.putString(taskName, subtaskList.joinToString(";"))
        editor.apply()
    }

    private fun loadSubTasks() {
        val sharedPreferences = getSharedPreferences("SubTaskPrefs", Context.MODE_PRIVATE)
        val savedSubTasks = sharedPreferences.getString(taskName, null)
        if (savedSubTasks != null) {
            subtaskMap[taskName] = savedSubTasks.split(";").filter { it.isNotBlank() }.toMutableList()
        } else {
            subtaskMap[taskName] = mutableListOf()
        }
        subtaskList.clear()
        subtaskList.addAll(subtaskMap[taskName] ?: emptyList())
    }
}
