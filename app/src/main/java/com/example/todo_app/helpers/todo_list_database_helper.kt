import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.todo_app.TodoListItem

class TodoListDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "TodoList.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the todo item table
        db.execSQL(TodoListContract.SQL_CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This app does not need to handle database upgrades
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    fun updateTodoItem(todoItem: TodoListItem): Int {
        val values = ContentValues()
        values.put(TodoListContract.TodoEntry.COLUMN_NAME_TITLE, todoItem.title)
        values.put(TodoListContract.TodoEntry.COLUMN_NAME_DESCRIPTION, todoItem.description)
        values.put(TodoListContract.TodoEntry.COLUMN_NAME_IMAGE_PATH, todoItem.image)

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(todoItem.id.toString())

        return writableDatabase.update(
            TodoListContract.TodoEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
    }

    fun getAllTodoItems(): List<TodoListItem> {
        val todoItems = mutableListOf<TodoListItem>()
        val db = readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            TodoListContract.TodoEntry.COLUMN_NAME_TITLE,
            TodoListContract.TodoEntry.COLUMN_NAME_DESCRIPTION,
            TodoListContract.TodoEntry.COLUMN_NAME_IMAGE_PATH
        )

        val sortOrder = "${TodoListContract.TodoEntry.COLUMN_NAME_TITLE} ASC"

        val cursor = db.query(
            TodoListContract.TodoEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
            )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(TodoListContract.TodoEntry.COLUMN_NAME_TITLE))
                val description = getString(getColumnIndexOrThrow(TodoListContract.TodoEntry.COLUMN_NAME_DESCRIPTION))
                val imagePath = getString(getColumnIndexOrThrow(TodoListContract.TodoEntry.COLUMN_NAME_IMAGE_PATH))

                todoItems.add(TodoListItem(id,title, description, imagePath))
            }
        }

        cursor.close()
        db.close()

        return todoItems
    }

    fun deleteTodoItem(id: Long) {
        val db = writableDatabase
        db.delete("todo", "_id=?", arrayOf(id.toString()))
        db.close()
    }
}

object TodoListContract {
    object TodoEntry : BaseColumns {
        const val COLUMN_NAME_IMAGE_PATH = "image_path"
        const val TABLE_NAME = "todo"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
    }

    const val SQL_CREATE_TODO_TABLE =
        "CREATE TABLE ${TodoEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${TodoEntry.COLUMN_NAME_TITLE} TEXT," +
                "${TodoEntry.COLUMN_NAME_DESCRIPTION} TEXT," +
                "${TodoEntry.COLUMN_NAME_IMAGE_PATH} TEXT)"
}