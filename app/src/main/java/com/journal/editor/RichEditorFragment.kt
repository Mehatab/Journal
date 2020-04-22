package com.journal.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.journal.R
import com.journal.databinding.FragmentRichEditorBinding
import com.journal.utils.NOTEBOOK_ID

class RichEditorFragment : Fragment() {
    private lateinit var binding: FragmentRichEditorBinding
    private val viewModel by viewModels<RichEditorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.notebookId = it.getLong(NOTEBOOK_ID, 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRichEditorBinding.inflate(inflater)
        binding.viewModel = viewModel
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.editor.setEditorTextColor(getString(R.string.editor_font_color))

        binding.editor.render()

        viewModel.style.observe(viewLifecycleOwner, Observer {
            binding.editor.updateTextStyle(it)
        })

        binding.bulletList.setOnClickListener {
            binding.editor.insertList(false)
        }

        binding.link.setOnClickListener {
            binding.editor.insertLink()
        }

        binding.save.setOnClickListener {
            viewModel.save(binding.editor.contentAsHTML)
        }

    }

    companion object {
        fun newInstance(bundle: Bundle? = Bundle()) = RichEditorFragment().apply {
            arguments = bundle
        }
    }
}