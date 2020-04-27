/*
 * Copyright 2020 Mehatab Shaikh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.journal.customviews.toolbar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.journal.R

class AddLinkDialog : DialogFragment() {

    private lateinit var titleEditText: EditText
    private lateinit var urlEditText: EditText
    private lateinit var cancelButton: Button
    private lateinit var insertButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireParentFragment() !is OnLinkInsertListener) {
            throw AssertionError()
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val dialogLayout =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_link, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogLayout)
            .create()

        // Show keyboard automatically on start. Doesn't happen on its own.
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        titleEditText = dialogLayout.findViewById(R.id.addlinkdialog_title)
        urlEditText = dialogLayout.findViewById(R.id.addlinkdialog_url)
        cancelButton = dialogLayout.findViewById(R.id.addlinkdialog_cancel)
        insertButton = dialogLayout.findViewById(R.id.addlinkdialog_insert)

        if (savedInstanceState == null && requireArguments().containsKey(KEY_PRE_FILLED_TITLE)) {
            val preFilledTitle = requireArguments().getString(KEY_PRE_FILLED_TITLE)
            titleEditText.setTextWithCursor(preFilledTitle)
        }

        cancelButton.setOnClickListener { dismiss() }
        insertButton.setOnClickListener { validateLinkAndSendCallback() }
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    validateLinkAndSendCallback()
                    true
                }
                else -> false
            }
        }

        return dialog
    }

    private fun validateLinkAndSendCallback() {
        val title = titleEditText.text.toString().trim()
        val url = urlEditText.text.toString().trim()

        titleEditText.error = when {
            title.isBlank() -> getString(R.string.composereply_addlink_error_empty_field)
            else -> null
        }
        urlEditText.error = when {
            url.isBlank() -> getString(R.string.composereply_addlink_error_empty_field)
            else -> null
        }

        if (title.isNotBlank() && url.isNotBlank()) {
            (requireParentFragment() as OnLinkInsertListener).onLinkInsert(Link(title, url))
            dismiss()
        }
    }

    private fun EditText.setTextWithCursor(string: CharSequence?) {
        setText(string)
        setSelection(text.length)
    }

    companion object {

        private const val KEY_PRE_FILLED_TITLE = "preFilledTitle"
        private const val TAG = "AddLinkDialog"

        fun showPreFilled(fragmentManager: FragmentManager, preFilledTitle: String?) {
            var dialog = fragmentManager.findFragmentByTag(TAG) as AddLinkDialog?
            dialog?.dismiss()

            val arguments = Bundle()
            if (preFilledTitle != null) {
                arguments.putString(KEY_PRE_FILLED_TITLE, preFilledTitle)
            }
            dialog = AddLinkDialog()
            dialog.arguments = arguments
            dialog.show(fragmentManager, TAG)
        }
    }
}
