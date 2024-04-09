package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    //var to track grid order
    private var counter = 0;
    private var workspaceBox: LinearLayout? = null
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

        //get workspaces from the database
        lifecycleScope.launch{
            val workspaces = database.workspaceDao().getAllWorkspaces()
            //if workspaces list is empty - load activity_main.xml (landing page for "No Workspaces"
            if(workspaces.isEmpty()){

            }
            //if workspaces list is not empty, load page with list of workspaces
            else{
                findViewById<View>(R.id.noWorkspacesBox).visibility = View.GONE

                val workspacesBox: LinearLayout = findViewById(R.id.workspacesBox)
                workspacesBox.visibility = View.VISIBLE
                workspacesBox.removeAllViews()

                for (workspace in workspaces) {
                    if (counter % 2 == 0) {
                        // Create a new vertical layout every 2 workspaces
                        workspaceBox = createVerticalLayout()
                        workspacesBox.addView(workspaceBox)
                    }

                    // Add workspace to the current vertical layout
                    workspaceBox?.addView(createWorkspaceBox(workspace))
                    counter++
                }

                // Inside onCreate() function after the for loop
                val addButton = Button(this@MainActivity)
                addButton.text = "+"
                addButton.setTextColor(Color.parseColor("#FFFF00")) // Set text color to yellow
                addButton.setBackgroundColor(Color.TRANSPARENT) // Set background to transparent
                addButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f) // Set text size to 20sp
                addButton.setOnClickListener {
                    showCustomDialog()
                }
                // Add the addButton to the current workspaceBox if it exists, otherwise create a new one
                if (workspaceBox != null) {
                    workspaceBox!!.addView(addButton)
                } else {
                    workspaceBox = createVerticalLayout()
                    workspacesBox.addView(workspaceBox)
                    workspaceBox!!.addView(addButton)
                }
            }
        }
    }

    private fun createVerticalLayout(): LinearLayout {
        val verticalLayout = LinearLayout(this)
        verticalLayout.orientation = LinearLayout.VERTICAL
        verticalLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        return verticalLayout
    }
    //function to create a box for each workspace
    private fun createWorkspaceBox(workspace: Workspace): LinearLayout{
        //just add the new box to the vertical layout element
        val workspaceBox = LayoutInflater.from(this).inflate(R.layout.workspace_template, null) as LinearLayout
        val workspaceNameTextView: TextView = workspaceBox.findViewById(R.id.workspaceNameTextView)
        workspaceNameTextView.text = workspace.name

        // Set OnClickListener for the workspace box
        workspaceBox.setOnClickListener {
            navigateToWorkspaceActivity(workspace.id)
            //Toast.makeText(this, "Clicked on workspace: ${workspace.name}", Toast.LENGTH_SHORT).show()
        }

        return workspaceBox


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

                // Insert the workspace name into the database and retrieve the ID
                val workspaceId = database.workspaceDao().insert(Workspace(name = workspaceName)).toInt()
                //launch function to open new page
                navigateToWorkspaceActivity(workspaceId)

            }
            //close dialog
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun navigateToWorkspaceActivity(workspaceId: Int) {
        val intent = Intent(this@MainActivity, WorkspaceActivity::class.java)
        intent.putExtra("WORKSPACE_ID", workspaceId)
        startActivity(intent)
    }
}