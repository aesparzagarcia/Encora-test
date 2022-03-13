package com.ares.aspiration.abstraction.util.view

import androidx.viewbinding.ViewBinding

class ViewHolder<T : ViewBinding>(val binding: T) : com.xwray.groupie.GroupieViewHolder(binding.root)