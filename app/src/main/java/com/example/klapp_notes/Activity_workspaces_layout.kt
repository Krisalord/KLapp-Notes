package com.example.klapp_notes

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch


class TableViewModel : ViewModel() {
    val tableIds = mutableListOf<Int>()
    val tableNames = mutableListOf<String>()
}

class Activity_workspaces_layout : Fragment() {

    private lateinit var containerLayout: LinearLayout
    private lateinit var tableViewModel: TableViewModel
    private var tableCounter = 0
   

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_workspaces_layout, container, false)
        containerLayout = view.findViewById(R.id.containerLayout)

        // Use activityViewModels to share ViewModel between fragments
        tableViewModel = ViewModelProvider(requireActivity()).get(TableViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAddTable = view.findViewById<Button>(R.id.buttonAddTable)
        val buttonAddNote = view.findViewById<Button>(R.id.buttonAddNote)


        buttonAddTable.setOnClickListener {
            addTableSquare(containerLayout)
        }

        buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_workspace_layout_to_ActivityNote)
        }

        // Initialize tableCounter based on the maximum table ID
        tableCounter = tableViewModel.tableIds.maxOrNull() ?: 0

        setupTables(savedInstanceState)



        // Restore tables from ViewModel when returning to the fragment

    }


    private fun setupTables(savedInstanceState: Bundle?) {
        // Restore tables from ViewModel when returning to the fragment
        tableViewModel.tableIds.forEach { tableId ->
            // Find the saved table by ID
            val savedTable = containerLayout.findViewById<FrameLayout>(tableId)

            // If the saved table is not found in the layout, add it
            if (savedTable == null) {
                val tableName = tableViewModel.tableNames[tableViewModel.tableIds.indexOf(tableId)]
                addTableSquare(containerLayout, tableId, tableName)
            } else {
                // If the table is found in the layout, add the hamburger menu
                addHamburgerMenu(savedTable)
            }
        }

        // Restore state from the savedInstanceState
        savedInstanceState?.let {
            val savedTableIds = it.getIntegerArrayList("tableIds")
            val savedTableNames = it.getStringArrayList("tableNames")

            savedTableIds?.forEachIndexed { index, tableId ->
                if (!tableViewModel.tableIds.contains(tableId)) {
                    val tableName = tableViewModel.tableNames[tableViewModel.tableIds.indexOf(tableId)]
                    addTableSquare(containerLayout, tableId, tableName)
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the table IDs and names to the bundle
        outState.putIntegerArrayList("tableIds", ArrayList(tableViewModel.tableIds))
        outState.putStringArrayList("tableNames", ArrayList(tableViewModel.tableNames))
    }

    private fun addTableSquare(containerLayout: LinearLayout, tableId: Int? = null, tableName: String? = null) {
        tableCounter++
        val frameLayout = FrameLayout(requireContext())
        val marginLayoutParams = ViewGroup.MarginLayoutParams(
            resources.getDimensionPixelSize(R.dimen.width_table_square),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val margin = resources.getDimensionPixelSize(R.dimen.margin_between_tables)
        marginLayoutParams.marginEnd = margin
        frameLayout.layoutParams = marginLayoutParams

        val borderSize = resources.getDimensionPixelSize(R.dimen.border_size)
        val borderColor = resources.getColor(R.color.colorTableBorder)
        val shapeDrawable = ShapeDrawable()
        shapeDrawable.paint.color = Color.TRANSPARENT
        shapeDrawable.paint.style = Paint.Style.STROKE
        shapeDrawable.paint.strokeWidth = borderSize.toFloat()
        shapeDrawable.paint.color = borderColor
        frameLayout.background = shapeDrawable

        frameLayout.id = tableId ?: View.generateViewId()
        containerLayout.addView(frameLayout)

        // Save the table ID to the ViewModel if it's not already present
        if (!tableViewModel.tableIds.contains(frameLayout.id)) {
            tableViewModel.tableIds.add(frameLayout.id)
        }

        // Save the table name to the ViewModel
        val finalTableName = tableName ?: "Table ${tableCounter + 1}"
        if (!tableViewModel.tableNames.contains(finalTableName)) {
            tableViewModel.tableNames.add(finalTableName)
        }

        // Add the hamburger menu to the FrameLayout
        addHamburgerMenu(frameLayout)

        containerLayout.orientation = LinearLayout.HORIZONTAL
        val containerLayoutParams = containerLayout.layoutParams as LinearLayout.LayoutParams
        containerLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        containerLayout.layoutParams = containerLayoutParams

        // Set click listener for the hamburger menu
        frameLayout.setOnClickListener {
            deleteTable(frameLayout)
        }

        // Add a TextView with the table name
        val textView = TextView(requireContext())
        textView.text = finalTableName
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        frameLayout.addView(textView)

        // Save the table to Room database
        if (tableId == null) {
            saveTableToDatabase(frameLayout.id, finalTableName)
        }
    }

    private fun saveTableToDatabase(tableId: Int, tableName: String) {
        // Access the database instance
        val database = AppDatabase.getDatabase(requireContext())

        // Use a coroutine to perform database operations
        lifecycleScope.launch {
            // Get the table DAO
            val tableDao = database.tableDao()

            // Create a new Table instance and set the tableName property
            val table = Table(id = tableId, tableName = tableName)

            // Insert the table into the database
            tableDao.insertTable(table)
        }
    }

    private fun addHamburgerMenu(frameLayout: FrameLayout) {
        val hamburgerMenu = ImageView(requireContext())
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.END or Gravity.TOP
        hamburgerMenu.layoutParams = layoutParams
        hamburgerMenu.setImageResource(R.drawable.ic_hamburger_menu)
        frameLayout.addView(hamburgerMenu)

        hamburgerMenu.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.table_options_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_delete_table -> {
                        deleteTable(frameLayout)
                        true
                    }

                    R.id.menu_rename_table -> {
                        renameTable(frameLayout)
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }
    }

    private fun deleteTable(frameLayout: FrameLayout) {
        val containerLayout = frameLayout.parent as LinearLayout
        containerLayout.removeView(frameLayout)
        tableCounter--

        // Remove the table ID from the ViewModel
        tableViewModel.tableIds.remove(frameLayout.id)
        tableViewModel.tableNames.remove((frameLayout.getChildAt(1) as TextView).text.toString())
    }

    private fun renameTable(frameLayout: FrameLayout) {
        val textView = frameLayout.getChildAt(1) as TextView
        val currentName = textView.text.toString()

        val dialog = AlertDialog.Builder(requireContext())
        val editText = EditText(requireContext())
        editText.setText(currentName)
        dialog.setView(editText)
            .setTitle("Rename Table")
            .setPositiveButton("OK") { _, _ ->
                val newName = editText.text.toString()
                textView.text = newName

                // Update the table name in the database
                val tableId = frameLayout.id
                updateTableNameInDatabase(tableId, newName)

                // Update the ViewModel with the new name
                updateViewModelTableName(frameLayout, newName)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateViewModelTableName(frameLayout: FrameLayout, newName: String) {
        // Remove the old name
        tableViewModel.tableNames.remove((frameLayout.getChildAt(1) as TextView).text.toString())

        // Add the new name
        tableViewModel.tableNames.add(newName)

        // Update the ViewModel with the new table name
        val index = tableViewModel.tableIds.indexOf(frameLayout.id)
        if (index != -1) {
            // Update the name in the ViewModel
            tableViewModel.tableNames[index] = newName
        }
    }



    private fun updateTableNameInDatabase(tableId: Int, newName: String) {
        // Access the database instance
        val database = AppDatabase.getDatabase(requireContext())

        // Use a coroutine to perform database operations
        lifecycleScope.launch {
            // Get the table from the database
            val tableDao = database.tableDao()
            val table = tableDao.getTableById(tableId)

            // Check if the table is not null
            table?.let {
                // Update the table name
                it.tableName = newName

                // Update the table in the database
                tableDao.updateTable(it)
            }
        }
    }


}
