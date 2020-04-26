package com.journal.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.journal.BR

class GenericRVAdapter<T : DiffItem>(
    @LayoutRes val layoutId: Int,
    var onListItemViewClickListener: OnListItemViewClickListener? = null
) : RecyclerView.Adapter<GenericRVAdapter.GenericViewHolder<T>>() {

    private var inflater: LayoutInflater? = null
    private var itemsList: List<T> by autoNotifyDelegate(adapter = this, initialValue = emptyList())

    fun setList(items: List<T>) {
        this.itemsList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, layoutId, parent, false)
        return GenericViewHolder(binding)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        val itemViewModel = itemsList[position]
        holder.bind(itemViewModel, onListItemViewClickListener, holder.adapterPosition)
    }


    class GenericViewHolder<T : DiffItem>(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            itemViewModel: T,
            onListItemViewClickListener: OnListItemViewClickListener? = null,
            adapterPosition: Int
        ) {
            binding.root.transitionName = "Transition_$adapterPosition"
            binding.setVariable(BR.transitionName, "Transition_$adapterPosition")
            binding.setVariable(BR.viewModel, itemViewModel)
            binding.setVariable(BR.listener, onListItemViewClickListener)
            binding.setVariable(BR.adapterPosition, adapterPosition)
            binding.executePendingBindings()
        }
    }

    interface OnListItemViewClickListener {
        fun onClick(view: View, position: Int)
    }
}