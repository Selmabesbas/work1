package com.example.todo_app.screens

import TodoListDbHelper
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_app.R

class AddTodoItemActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private  lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo_item)

        // Open the database
        db = TodoListDbHelper(this).writableDatabase
        val imageView = findViewById<ImageView>(R.id.imageView)
        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                imageView.setImageURI(imageUri)
                currentPhotoPath = saveImageToInternalStorage(imageUri)
            }
        }
        // Set up the save button click listener
        val saveButton: Button = findViewById(R.id.save_todo_button)
        val uploadButton: Button = findViewById(R.id.upload_todo_image)
        saveButton.setOnClickListener { saveTodoItem() }
        uploadButton.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun saveTodoItem() {
        val title = findViewById<EditText>(R.id.todo_title_input).text.toString()
        val description = findViewById<EditText>(R.id.todo_description_input).text.toString()

        // Save the todo item to the SQLite database
        val dbHelper = TodoListDbHelper(this)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(TodoListContract.TodoEntry.COLUMN_NAME_TITLE, title)
            put(TodoListContract.TodoEntry.COLUMN_NAME_DESCRIPTION, description)
            put(TodoListContract.TodoEntry.COLUMN_NAME_IMAGE_PATH, currentPhotoPath)
        }

        db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, values)
        db.close()

        // Return to the main activity
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
        inputStream?.copyTo(outputStream)
        return filesDir.absolutePath + "/" + fileName
    }

    override fun onDestroy() {
        super.onDestroy()
        // Close the database
        db.close()
    }
}




