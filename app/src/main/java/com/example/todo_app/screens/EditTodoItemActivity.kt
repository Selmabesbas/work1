package com.example.todo_app.screens

import TodoListDbHelper
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todo_app.R
import com.example.todo_app.TodoListItem

class EditTodoItemActivity : AppCompatActivity() {
    private  lateinit var currentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo_item)

        val intent = intent
        val bundle = intent.extras

        val todoItem = bundle?.getParcelable<TodoListItem>("todoItem")
        val editTitle = findViewById<EditText>(R.id.edit_title)
        val editDescription = findViewById<EditText>(R.id.edit_description)
        val saveButton = findViewById<Button>(R.id.button_save)
        val cancelButton = findViewById<Button>(R.id.button_cancel)
        val editImage = findViewById<ImageView>(R.id.edit_image)

        if (todoItem != null) {
            editImage.setImageURI(Uri.parse(todoItem.image))
        }

        if (todoItem != null) {
            editTitle.setText(todoItem.title)
        }

        if (todoItem != null) {
            editDescription.setText(todoItem.description)
        }

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                editImage.setImageURI(imageUri)
                currentPhotoPath = saveImageToInternalStorage(imageUri)
            }
        }
        val uploadButton: Button = findViewById<Button>(R.id.upload_edit_todo_image)

        uploadButton.setOnClickListener {
            getContent.launch("image/*")
        }

        saveButton.setOnClickListener {
            val dbHelper = TodoListDbHelper(this)
            val newTodoItem: TodoListItem? = todoItem?.let { it1 -> TodoListItem(it1.id,editTitle.text.toString(),editDescription.text.toString(),currentPhotoPath) }
            val updateRecord = newTodoItem?.let { it1 -> dbHelper.updateTodoItem(it1) }

            setResult(RESULT_OK)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }

    }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val inputStream = contentResolver.openInputStream(imageUri)
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val outputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
        inputStream?.copyTo(outputStream)
        return filesDir.absolutePath + "/" + fileName
    }

}