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

    fun getItem(position: Int) = itemsList[position]

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