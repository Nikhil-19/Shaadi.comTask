package com.example.drsmarineservices.nikhil.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.redmango.profilecard.R

class CustomDialogFragment(val ctx: Context, msgPleaseWait: Int, buttonCancel: Int) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(ctx)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_data, null))
            builder.setTitle(getString(R.string.app_name))
            // Add action buttons
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_insent)
            return alertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}