package com.example.klapp_notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import android.graphics.drawable.ShapeDrawable
import android.graphics.Paint
import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.TextView


class TableViewModel : ViewModel() {
    val tableIds = mutableListOf<Int>()
}

class Activity_workspaces_layout : Fragment() {

    private lateinit var containerLayout: LinearLayout
    private lateinit var tableViewModel: TableViewModel
    private var tableCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_workspaces_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerLayout = view.findViewById(R.id.containerLayout)
        tableViewModel = ViewModelProvider(requireActivity()).get(TableViewModel::class.java)

        val buttonAddTable = view.findViewById<Button>(R.id.buttonAddTable)
        val buttonAddNote = view.findViewById<Button>(R.id.buttonAddNote)

        buttonAddTable.setOnClickListener {
            addTableSquare(containerLayout)
        }

        buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_workspace_layout_to_ActivityNote)
        }

        // Restore tables from ViewModel when returning to the fragment
        tableViewModel.tableIds.forEach { tableId ->
            val savedTable = view.findViewById<FrameLayout>(tableId)
            if (savedTable != null) {
                // If the table is found in the layout, add the hamburger menu
                addHamburgerMenu(savedTable)
            }
        }
    }

    private fun addTableSquare(containerLayout: LinearLayout) {
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

        frameLayout.id = View.generateViewId()
        containerLayout.addView(frameLayout)

        // Save the table ID to the ViewModel if it's not already present
        if (!tableViewModel.tableIds.contains(frameLayout.id)) {
            tableViewModel.tableIds.add(frameLayout.id)
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
        val tableName = "Table $tableCounter"
        val textView = TextView(requireContext())
        textView.text = tableName
        textView.setTextColor(Color.BLACK)
        textView.gravity = Gravity.CENTER
        frameLayout.addView(textView)
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
    }
}