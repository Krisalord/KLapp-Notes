package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout

class WorkspaceActivity : AppCompatActivity() {

    private lateinit var containerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workspace)

        containerLayout = findViewById(R.id.containerLayout)

        val openNewNote: Button = findViewById(R.id.buttonAddNote)
        openNewNote.setOnClickListener {
            navigateToNoteActivity()
        }

        val createNewTable: Button = findViewById(R.id.buttonAddTable)
        createNewTable.setOnClickListener {
            addNewTable()
        }

        val workspaceId = intent.getIntExtra("WORKSPACE_ID", -1)

    }

    private fun navigateToNoteActivity(){
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }

    private fun addNewTable() {
        // Create a new FrameLayout for the squared table
        val tableLayout = FrameLayout(this)

        // Define the size of the table (adjust as needed)
        val tableSize = resources.getDimensionPixelSize(R.dimen.width_table_square)
        val layoutParams = LinearLayout.LayoutParams(tableSize, ViewGroup.LayoutParams.MATCH_PARENT)

        // Set margin for the table (adjust as needed)
        layoutParams.setMargins(0, 0, 16, 0)

        // Set layout parameters for the table
        tableLayout.layoutParams = layoutParams

        // Set background color for the table
        tableLayout.setBackgroundColor(Color.WHITE)

        // Add the table to the container layout
        containerLayout.addView(tableLayout)
    }

}
