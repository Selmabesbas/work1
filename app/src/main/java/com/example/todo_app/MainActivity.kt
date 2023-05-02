package com.example.todo_app

import TodoListDbHelper
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.screens.AddTodoItemActivity
import com.example.todo_app.screens.EditTodoItemActivity
import com.example.todo_app.screens.LanguageRateActivity


class MainActivity : AppCompatActivity() {
//    val todoItems = listOf(
//        TodoListItem("Buy milk", "1 gallon of whole milk",""),
//        TodoListItem("Do laundry", "Wash clothes and dry",""),
//        TodoListItem("Go for a run", "Run for 30 minutes",""),
//        TodoListItem("Finish project", "Complete the project by Friday",""),
//
//    )

    private lateinit var todoItems: MutableList<TodoListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoItems = TodoListDbHelper(this).getAllTodoItems().toMutableList()

        val recyclerView = findViewById<RecyclerView>(R.id.todoList)
        val adapter = TodoListAdapter(todoItems,applicationContext){
            val intent = Intent(this,EditTodoItemActivity::class.java)

            val bundle = Bundle().apply {
                putParcelable("todoItem", it)
            }
            intent.putExtras(bundle)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val createTodoButton = findViewById<Button>(R.id.create_todo_item_button)
        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            todoItems = TodoListDbHelper(this).getAllTodoItems().toMutableList()
            adapter.notifyDataSetChanged()
        }

        createTodoButton.setOnClickListener {
            val intent = Intent(this,AddTodoItemActivity::class.java)
            resultLauncher.launch(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_1 -> {
                showDialog("Item 1", "You clicked on Item 1.")
                true
            }
            R.id.menu_item_2 -> {
                showDialog("Item 2", "You clicked on Item 2.")
                true
            }
            R.id.menu_item_3 -> {
                val intent = Intent(this,LayoutTaskActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_item_4 -> {
                val intent = Intent(this,LanguageRateActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            // Do something when the user clicks the positive button
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do something when the user clicks the negative button
        }
        builder.show()
    }
}