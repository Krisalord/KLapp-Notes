package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.PopupMenu

class WorkspaceActivity : AppCompatActivity() {

    private lateinit var containerLayout: LinearLayout
    private var workspaceId = -1
    private lateinit var table: Table

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
        workspaceId = intent.getIntExtra("WORKSPACE_ID", -1)
        Log.d("WorkspaceActivity", "Received workspace ID: $workspaceId")

    }

    private fun navigateToNoteActivity() {
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

        // Set borders
        tableLayout.setBackgroundResource(R.drawable.table_border)

        // Add the table to the container layout
        containerLayout.addView(tableLayout)

        // Create a new ImageView for the hamburger menu (three yellow dots)
        val hamburgerMenu = ImageView(this)
        hamburgerMenu.setImageResource(R.drawable.yellow_dots) // Set the custom drawable

        // Set size of the hamburger menu
        val menuSize = resources.getDimensionPixelSize(R.dimen.size_hamburger_menu)
        val menuParams = FrameLayout.LayoutParams(menuSize, menuSize)
        menuParams.gravity = Gravity.TOP or Gravity.END // Position the menu at the top right corner
        hamburgerMenu.layoutParams = menuParams
        hamburgerMenu.setOnClickListener { view ->
            // Create a PopupMenu
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.table_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                // Handle menu item clicks here
                when (item.itemId) {
                    R.id.menu_rename_table -> {
                        // Handle menu item 1 click
                        true
                    }
                    R.id.menu_delete_table -> {
                        // Handle menu item 2 click
                        if (::table.isInitialized) {
                            // Remove the table from the container layout
                            containerLayout.removeView(tableLayout)

                            // Delete the table from the database
                            deleteTableFromDatabase(table.id)
                        }
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        // Add the hamburger menu to the tableLayout
        tableLayout.addView(hamburgerMenu)

        // Initialize the table instance with workspace ID
        val tableName = "Table"  // You can set a default name here or get it from user input
        table = Table(tableName = tableName, workspaceId = workspaceId) // Initialize table

        // Insert the table into the database
        lifecycleScope.launch {
            saveTableToDatabase(table)
        }
    }

    private suspend fun saveTableToDatabase(table: Table) {
        // Access the database instance
        val database = AppDatabase.getDatabase(this)

        // Get the table DAO
        val tableDao = database.tableDao()

        // Insert the table into the database and capture the generated ID
        val tableId = tableDao.insert(table)
        table.id = tableId.toInt() // Update the table ID
    }

    private fun deleteTableFromDatabase(tableId: Int) {
        // Access the database instance
        val database = AppDatabase.getDatabase(this)

        // Use a coroutine to perform database operations
        lifecycleScope.launch {
            // Get the table DAO
            val tableDao = database.tableDao()

            // Delete the table from the database using its ID
            tableDao.deleteById(tableId.toLong())
        }
    }
}
