package com.journal.notebook.details

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.Hold
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.Note
import com.journal.databinding.FragmentNotebookBinding
import com.journal.utils.*

class NotebookFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentNotebookBinding
    private val viewModel by viewModels<NotebookViewModel>()
    private var adapter = GenericRVAdapter<Note>(R.layout.adapter_note_item, this)
    private var scrollToTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
        arguments?.let {
            viewModel.notebookId.postValue(it.getLong(NOTEBOOK_ID, 0L))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentNotebookBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }

        binding.rv.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            if (scrollToTop) binding.rv.scrollToPosition(0)
            scrollToTop = false
        })

        binding.add.setOnClickListener {
            newNote(viewModel.notebookId.value ?: 0, binding.add)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }

        binding.sort.setOnClickListener { v ->
            scrollToTop = true
            v.rotate()
            viewModel.ascOrder.postValue(v.isSelected.not())
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.note_layout -> {
                editNote(viewModel.getNote(position), view)
            }
        }
    }

    private fun onOptionsItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.edit -> {
                editNoteBook(viewModel.notebookId.value ?: 0)
                true
            }
            else -> false
        }
    }
}