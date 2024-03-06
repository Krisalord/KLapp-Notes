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



import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat

class Activity_workspaces_layout : Fragment() {

    private lateinit var containerLayout: LinearLayout
    private var tableCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_workspaces_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAddTable = view.findViewById<Button>(R.id.buttonAddTable)
        val buttonAddNote = view.findViewById<Button>(R.id.buttonAddNote)
        containerLayout = view.findViewById(R.id.containerLayout)

        buttonAddTable.setOnClickListener {
            addTableSquare(containerLayout)
        }

        buttonAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_workspace_layout_to_ActivityNote)
        }
    }

    private fun addTableSquare(containerLayout: LinearLayout) {
        // Increment the table counter
        tableCounter++

        // Create a new FrameLayout
        val frameLayout = FrameLayout(requireContext())

        // Set margin for the FrameLayout
        val marginLayoutParams = ViewGroup.MarginLayoutParams(
            resources.getDimensionPixelSize(R.dimen.width_table_square),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val margin = resources.getDimensionPixelSize(R.dimen.margin_between_tables)
        marginLayoutParams.marginEnd = margin
        frameLayout.layoutParams = marginLayoutParams

        // Create a ShapeDrawable with border
        val borderSize = resources.getDimensionPixelSize(R.dimen.border_size)
        val borderColor = resources.getColor(R.color.colorTableBorder)
        val shapeDrawable = ShapeDrawable()
        shapeDrawable.paint.color = Color.TRANSPARENT
        shapeDrawable.paint.style = Paint.Style.STROKE
        shapeDrawable.paint.strokeWidth = borderSize.toFloat()
        shapeDrawable.paint.color = borderColor
        frameLayout.background = shapeDrawable

        frameLayout.id = View.generateViewId() // Generate a unique ID for the new FrameLayout

        // Add the hamburger menu to the FrameLayout
        addHamburgerMenu(frameLayout)

        // Add the new FrameLayout to the containerLayout
        containerLayout.addView(frameLayout)

        // Set layout parameters for the containerLayout to arrange tables horizontally
        containerLayout.orientation = LinearLayout.HORIZONTAL
        val containerLayoutParams = containerLayout.layoutParams as LinearLayout.LayoutParams
        containerLayoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        containerLayout.layoutParams = containerLayoutParams

        // Set click listener for the hamburger menu
        frameLayout.setOnClickListener {
            // Call the function to delete the table
            deleteTable(frameLayout)
        }
    }

    private fun addHamburgerMenu(frameLayout: FrameLayout) {
        // Create an ImageView for the hamburger menu
        val hamburgerMenu = ImageView(requireContext())
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = Gravity.END or Gravity.TOP // Updated gravity to top-right corner
        hamburgerMenu.layoutParams = layoutParams
        hamburgerMenu.setImageResource(R.drawable.ic_hamburger_menu)

        // Add the hamburger menu ImageView to the FrameLayout
        frameLayout.addView(hamburgerMenu)

        // Set an onClickListener for the hamburger menu
        hamburgerMenu.setOnClickListener { view ->
            // Create a PopupMenu
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.table_options_menu, popupMenu.menu)

            // Set a click listener for menu items
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_delete_table -> {
                        // Handle delete action
                        deleteTable(frameLayout)
                        true
                    }
                    // Add more menu items as needed

                    else -> false
                }
            }

            // Show the PopupMenu
            popupMenu.show()
        }
    }

    private fun deleteTable(frameLayout: FrameLayout) {
        // Remove the FrameLayout from the containerLayout
        val containerLayout = frameLayout.parent as LinearLayout
        containerLayout.removeView(frameLayout)

        // Decrement the table counter
        tableCounter--
    }


}