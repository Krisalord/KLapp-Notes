package com.example.klapp_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityNote : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the EditText using its id
        val editText = view.findViewById<EditText>(R.id.editText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val noteText = editText.text.toString().trim()

            if (noteText.isNotEmpty()) {
                // Save the note to the Room database
                saveNoteToDatabase(noteText)

                // You can add any additional logic here, like clearing the EditText or navigating to another fragment/activity.
            }
        }
    }

    private fun saveNoteToDatabase(noteText: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val note = Notes(id = null, noteName = noteText)
            AppDatabase.getDatabase(requireContext()).notesDao().insert(note)
        }
    }
}
