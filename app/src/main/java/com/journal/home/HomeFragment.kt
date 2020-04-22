package com.journal.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.journal.R
import com.journal.common.GenericRVAdapter
import com.journal.database.entities.NoteInfo
import com.journal.database.entities.Notebook
import com.journal.databinding.FragmentHomeBinding
import com.journal.editor.RichEditorFragment
import com.journal.notebook.NewNotebook
import com.journal.notebook.details.NotebookFragment
import com.journal.setting.SettingFragment
import com.journal.utils.NOTEBOOK_ID
import com.journal.utils.requireNavigation
import com.ncapdevi.fragnav.FragNavTransactionOptions

class HomeFragment : Fragment(), GenericRVAdapter.OnListItemViewClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var notebookAdapter: GenericRVAdapter<Notebook>
    private lateinit var notesAdapter: GenericRVAdapter<NoteInfo>
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        notebookAdapter = GenericRVAdapter(R.layout.adapter_journal_item, this)
        notesAdapter = GenericRVAdapter(R.layout.adapter_home_note_item, this)
        binding.booksRv.adapter = notebookAdapter
        binding.rv.adapter = notesAdapter

        viewModel.notebooks.observe(viewLifecycleOwner, Observer {
            notebookAdapter.items = it
        })

        viewModel.notes.observe(viewLifecycleOwner, Observer {
            notesAdapter.items = it
        })

        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.add.setOnClickListener {
            requireNavigation()?.pushFragment(RichEditorFragment.newInstance())
        }

        binding.addNotebook.setOnClickListener {
            requireNavigation()?.showDialogFragment(NewNotebook.newInstance())
        }

        binding.menu.setOnClickListener {
            requireNavigation()?.pushFragment(
                SettingFragment.newInstance(),
                FragNavTransactionOptions
                    .newBuilder()
                    .apply {
                        enterAnimation = R.anim.slide_in_left
                        exitAnimation = R.anim.slide_out_left
                        popEnterAnimation = R.anim.slide_in_right
                        popExitAnimation = R.anim.slide_out_right
                    }.build()
            )
        }
    }

    override fun onClick(view: View, position: Int) {
        when (view.id) {
            R.id.notebook -> {
                viewModel.getNotebook(position)?.let {
                    requireNavigation()?.pushFragment(NotebookFragment.newInstance(Bundle().apply {
                        putLong(NOTEBOOK_ID, it.notebookId ?: 0)
                    }), null)
                }
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle? = Bundle()) = HomeFragment().apply {
            arguments = bundle
        }
    }
}