package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klapp_notes.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.room.Insert
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var database: AppDatabase
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Room database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()


        val openDialogButton: Button = findViewById(R.id.openDialogButton)
        openDialogButton.setOnClickListener {
            showCustomDialog()
        }

        // Fetch and log the data when the activity is created (for testing purposes)
        lifecycleScope.launch {
            val workspaces = database.workspaceDao().getAllWorkspaces()
            logWorkspaces(workspaces)
        }
    }
    private fun logWorkspaces(workspaces: List<Workspace>) {
        for (workspace in workspaces) {
            Log.d("MainActivity", "Workspace ID: ${workspace.id}, Name: ${workspace.name}")
        }
    }
    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
        val editText: TextInputEditText = dialogView.findViewById(R.id.editText)
        val submitButton: Button = dialogView.findViewById(R.id.submitDialogButton)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()


        submitButton.setOnClickListener {
            //save entered text to variable (workspace name) for future use in db
            val workspaceName = editText.text.toString()
            // Launch a coroutine to call the suspend function
            lifecycleScope.launch {
                // Insert the workspace name into the database
                database.workspaceDao().insert(Workspace(name = workspaceName))
            }
            //launch function to open new page
            navigateToWorkspaceActivity()
            //close dialog
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun navigateToWorkspaceActivity() {
        val intent = Intent(this, WorkspaceActivity::class.java)
        startActivity(intent)
    }
}