package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klapp_notes.databinding.ActivityMainBinding
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val openDialogButton: Button = findViewById(R.id.openDialogButton)
        openDialogButton.setOnClickListener {
            showCustomDialog()
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
            val enteredText = editText.text.toString()

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