package com.journal.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook
import com.journal.databinding.FragmentHomeBinding
import com.journal.utils.editNote
import com.journal.utils.newNote
import com.journal.utils.openNotebook

class HomeFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentHomeBinding
    private var notebookAdapter = GenericRVAdapter<Notebook>(R.layout.adapter_journal_item, this)
    private var notesAdapter = GenericRVAdapter<NoteInfo>(R.layout.adapter_home_note_item, this)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentHomeBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.booksRv.adapter = notebookAdapter
        binding.rv.adapter = notesAdapter

        viewModel.notebooks.observe(viewLifecycleOwner, Observer {
            notebookAdapter.setList(it)
        })

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            notesAdapter.setList(it)
        })

        initListeners()
    }

    private fun initListeners() {
        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener onOptionsItemSelected(it.itemId)
        }

        binding.add.setOnClickListener {
            newNote(binding.add)
        }

        binding.addNotebook.setOnClickListener {
            findNavController().navigate(R.id.home_to_new_notebook)
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.notebook -> {
                viewModel.getNotebook(position)?.let {
                    openNotebook(it)
                }
            }

            R.id.note_layout -> {
                editNote(viewModel.getNote(position), view)
            }
        }
    }

    private fun onOptionsItemSelected(itemId: Int): Boolean {
        return when (itemId) {
            R.id.setting -> {
                findNavController().navigate(R.id.goto_setting)
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //exitTransition = Hold()
    }
}