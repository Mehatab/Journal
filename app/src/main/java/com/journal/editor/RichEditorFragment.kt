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

package com.journal.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.editor.MarkdownHintStyles
import com.editor.MarkdownHints
import com.editor.MarkdownSpanPool
import com.editor.flexmark.FlexmarkMarkdownParser
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.journal.R
import com.journal.customviews.toolbar.AddLinkDialog
import com.journal.customviews.toolbar.Link
import com.journal.customviews.toolbar.OnLinkInsertListener
import com.journal.databinding.FragmentRichEditorBinding
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min

class RichEditorFragment : Fragment(), OnLinkInsertListener {

    private lateinit var binding: FragmentRichEditorBinding
    private val mViewModel by viewModels<RichEditorViewModel>()
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    private var saveNoteJob: Job? = null
    private val args: RichEditorFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRichEditorBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        binding.parent.transitionName = args.transitionName
        viewLifecycleOwner.lifecycle.addObserver(mViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mViewModel.notebookId = args.notebookId
        mViewModel.noteId.postValue(args.noteId)

        val parser = FlexmarkMarkdownParser(markdownHintStyles(), MarkdownSpanPool())
        val markdownHints = MarkdownHints(binding.editor, parser)
        binding.editor.addTextChangedListener(markdownHints.textWatcher())

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.editorFormatToolbar.onMarkdownSyntaxApplied =
            { syntax -> syntax.insert(binding.editor) }
        binding.editorFormatToolbar.onInsertLinkClicked = {
            // selectionStart can be lesser than selectionEnd if the selection was made right-to-left.
            val selectionStart = min(binding.editor.selectionStart, binding.editor.selectionEnd)
            val selectionEnd = max(binding.editor.selectionStart, binding.editor.selectionEnd)

            // preFilledTitle will be empty when there's no text selected.
            val preFilledTitle = binding.editor.text?.subSequence(selectionStart, selectionEnd)
            AddLinkDialog.showPreFilled(childFragmentManager, preFilledTitle.toString())
        }


        var note = "# "
        binding.editor.doAfterTextChanged {
            if (note == it.toString().trim())
                return@doAfterTextChanged

            saveNoteJob?.cancel()
            saveNoteJob = coroutineScope.launch {
                it?.let {
                    delay(500)
                    mViewModel.update()
                    note = it.toString().trim()
                }
            }
        }

        mViewModel.noteLiveData.observe(viewLifecycleOwner, Observer {

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        saveNoteJob?.cancel()
    }

    private fun markdownHintStyles(): MarkdownHintStyles {
        val color = { colorResId: Int -> ContextCompat.getColor(requireContext(), colorResId) }
        val dimensPx = { dimenResId: Int -> resources.getDimensionPixelSize(dimenResId) }

        return MarkdownHintStyles(
            context = requireContext(),
            syntaxColor = color(R.color.markdown_syntax),
            blockQuoteIndentationRuleColor = color(R.color.markdown_blockquote_indentation_rule),
            blockQuoteTextColor = color(R.color.markdown_blockquote_text),
            linkUrlColor = color(R.color.markdown_link_url),
            linkTextColor = color(R.color.markdown_link_text),
            horizontalRuleColor = color(R.color.markdown_horizontal_rule),
            codeBackgroundColor = color(R.color.markdown_code_background)
        )
    }

    override fun onLinkInsert(link: Link) {
        link.insert(binding.editor)
    }
}