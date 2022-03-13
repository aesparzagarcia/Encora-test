package com.ares.aspiration.ui.iviews

import android.view.View
import com.ares.aspiration.R
import com.ares.aspiration.abstraction.util.SOURCE
import com.ares.aspiration.abstraction.util.view.ViewBindingItem
import com.ares.aspiration.databinding.ItemFixerBinding

class FixerItem(
    private val textOne: String,
    private val textTwo: String,
    private val action: (currency: String) -> Unit?,
    private val source: String
): ViewBindingItem<ItemFixerBinding>() {

    override fun getLayout(): Int = R.layout.item_fixer

    override fun inflate(itemView: View): ItemFixerBinding = ItemFixerBinding.bind(itemView)

    override fun bind(viewBinding: ItemFixerBinding, position: Int) {
        viewBinding.apply {
            textItemOne.text = textOne
            textItemTwo.text = if (source.contains(SOURCE.SYMBOLS.name)) textTwo else "$$textTwo"
            cardView.setOnClickListener {
                action.invoke(textOne)
            }
        }
    }
}