package com.example.klapp_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Activity_workspaces_layout : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_workspaces_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAddNote = view.findViewById<Button>(R.id.buttonAddNote)
        buttonAddNote.setOnClickListener {
            // Navigate to the ActivityNote fragment
            findNavController().navigate(R.id.action_workspace_layout_to_ActivityNote)
        }

        val buttonAddTable = view.findViewById<Button>(R.id.buttonAddTable)
        val tableSquare = view.findViewById<FrameLayout>(R.id.tableSquare)

        buttonAddTable.setOnClickListener {
            // Set the visibility of the tableSquare to VISIBLE
            tableSquare.visibility = View.VISIBLE
        }

    }
}
