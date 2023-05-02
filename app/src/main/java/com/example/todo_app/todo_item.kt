package com.example.todo_app

import android.media.Image
import android.os.Parcel
import android.os.Parcelable

data class TodoItem(val title: String, val description: String)


data class TodoListItem(
    val id: Long,
    val title: String,
    val description: String,
    val image: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoListItem> {
        override fun createFromParcel(parcel: Parcel): TodoListItem {
            return TodoListItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoListItem?> {
            return arrayOfNulls(size)
        }
    }
}