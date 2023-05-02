package com.example.todo_app

import TodoListDbHelper
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter(
    private var todoList: MutableList<TodoListItem>,
    private val context: Context,
    val onEditClickListener: (todoItem: TodoListItem) -> Unit
    ) : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val todoImage: ImageView = itemView.findViewById(R.id.todo_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item_row, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            // Add menu items
            menu.add("Edit").setOnMenuItemClickListener {
                onEditClickListener(todoList[position])
                true
            }
            menu.add("Delete").setOnMenuItemClickListener {
                val dbHelper = TodoListDbHelper(context)
                dbHelper.deleteTodoItem(todoList[position].id)
                todoList.removeAt(position)
                notifyDataSetChanged()
                true
            }
        }

        val item = todoList[position]
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description
        holder.todoImage.setImageURI(Uri.parse(item.image))
    }

    override fun getItemCount(): Int {
        return todoList.size
    }


}