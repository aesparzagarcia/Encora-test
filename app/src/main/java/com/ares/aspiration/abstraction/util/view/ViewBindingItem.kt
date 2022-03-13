package com.ares.aspiration.abstraction.util.view

import android.view.View
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.Item

abstract class ViewBindingItem<T : ViewBinding> : Item<ViewHolder<T>> {

    constructor() : super()

    constructor(id: Long) : super(id)

    override fun createViewHolder(itemView: View): ViewHolder<T> {
        return ViewHolder(inflate(itemView))
    }

    override fun bind(viewHolder: ViewHolder<T>, position: Int) {
        @Suppress("TooGenericExceptionThrown")
        throw RuntimeException("Doesn't get called")
    }

    override fun bind(viewHolder: ViewHolder<T>, position: Int, payloads: MutableList<Any>) {
        bind(viewHolder.binding, position)
    }

    abstract fun inflate(itemView: View): T

    abstract fun bind(viewBinding: T, position: Int)

    @Suppress("UnusedPrivateMember")
    private fun bind(viewBinding: T, position: Int, payloads: List<Any>) {
        bind(viewBinding, position, payloads)
    }
}
