package com.journal.notebook.details

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
import com.journal.utils.NOTEBOOK_ID
import com.journal.utils.TRANSITION_NAME

class NotebookFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentNotebookBinding
    private val viewModel by viewModels<NotebookViewModel>()
    private var adapter = GenericRVAdapter<Note>(R.layout.adapter_note_item, this)

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
        binding = FragmentNotebookBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
        }

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }

        binding.rv.adapter = adapter

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
        })

        binding.add.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding.add to binding.add.transitionName)
            findNavController().navigate(R.id.action_notebook_to_editor, Bundle().apply {
                putLong(NOTEBOOK_ID, viewModel.notebookId.value ?: 0)
                putString(TRANSITION_NAME, binding.add.transitionName)
            }, null, extras)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }

        binding.sort.setOnClickListener {
            viewModel.ascOrder.postValue(it.isSelected.not())
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.note_layout -> {
                val note = viewModel.getNote(position)
                val action = NotebookFragmentDirections.notebookToEditor(
                    note.noteId ?: 0,
                    note.notebookId ?: 0,
                    view.transitionName
                )
                findNavController().navigate(
                    action,
                    FragmentNavigatorExtras(view to view.transitionName)
                )
            }
        }
    }

    private fun onOptionsItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.edit -> {
                val action =
                    NotebookFragmentDirections.actionNotebookToEdit(viewModel.notebookId.value ?: 0)
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }
}