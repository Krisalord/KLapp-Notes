package com.example.klapp_notes


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.klapp_notes.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            // Inflate the custom layout
            val customView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)

            // Create an AlertDialog.Builder
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
                .setView(customView)
                .setTitle("Add new work space")

            // Set positive button and its click listener
            alertDialogBuilder.setPositiveButton("+") { dialog, which ->
                // Perform the action when OK button is clicked
                // For example, you can close the dialog or navigate to another fragment
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }

            // Create and show the alert dialog
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
