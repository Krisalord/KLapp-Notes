package com.example.klapp_notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.klapp_notes.databinding.ActivityMainBinding
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
class WorkspaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workspace)

        val openNewNote: Button = findViewById(R.id.buttonAddNote)
        openNewNote.setOnClickListener {
            navigateToNoteActivity()
        }

        val createNewTable: Button = findViewById(R.id.buttonAddTable)
        createNewTable.setOnClickListener {
            addNewTable()
        }
    }

    private fun navigateToNoteActivity(){
        val intent = Intent(this, NoteActivity::class.java)
        startActivity(intent)
    }

    private fun addNewTable(){
        
    }
}
