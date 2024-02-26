package com.example.klapp_notes



import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.klapp_notes.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            showPopup(view)
        }
    }

    private fun showPopup(view: View) {
        // Hide the "+" button in the FirstFragment
        binding.buttonFirst.visibility = View.INVISIBLE

        val customView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)
        val popupWindow = PopupWindow(
            customView,
            // Set width and height to WRAP_CONTENT initially
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set background drawable to handle touch events outside the window
        popupWindow.setBackgroundDrawable(requireContext().getDrawable(android.R.color.transparent))

        // Set the desired width and height
        popupWindow.width = resources.getDimensionPixelSize(R.dimen.popup_width)
        popupWindow.height = resources.getDimensionPixelSize(R.dimen.popup_height)

        // Show the PopupWindow at a specific location on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Set up the button click listener in the PopupWindow
        val openSecondFragmentButton = customView.findViewById<Button>(R.id.buttonOpenActivityNote)
        openSecondFragmentButton.setOnClickListener {
            // Dismiss the PopupWindow when the button is clicked
            popupWindow.dismiss()

            // Navigate to the SecondFragment
            findNavController().navigate(R.id.action_FirstFragment_to_activityNoteFragment2)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}