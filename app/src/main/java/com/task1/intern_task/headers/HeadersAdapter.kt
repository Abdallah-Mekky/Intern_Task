package com.task1.intern_task.headers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.task1.intern_task.R

class HeadersAdapter(var items: MutableList<HeadersItem>? = null) :
    RecyclerView.Adapter<HeadersAdapter.HeadersViewHolder>() {

    class HeadersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var keyText: TextInputEditText = itemView.findViewById(R.id.key_Details)
        var valueText: TextInputEditText = itemView.findViewById(R.id.value_Details)
        var deleteImage: ImageView = itemView.findViewById(R.id.deleteImage)
        var confirmImage: ImageView = itemView.findViewById(R.id.confirmImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadersViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.headers_item, parent, false)

        return HeadersViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeadersViewHolder, position: Int) {

        var itemsList = items?.get(position)

        holder.keyText.setText(itemsList?.key)
        holder.valueText.setText(itemsList?.value)
        holder.deleteImage.setImageResource(itemsList?.deleteImage ?: 0)
        holder.confirmImage.setImageResource(itemsList?.confirmImage ?: 0)

        //callback
        if (onDeleteImageClickListener != null) {

            holder.deleteImage.setOnClickListener {
                onDeleteImageClickListener?.onClick(position, items!!, itemsList!!)
            }
        }

        if (onConfirmImageClickListener != null) {

            holder.confirmImage.setOnClickListener {
                onConfirmImageClickListener?.onClick(position, items!!, itemsList!!)
            }
        }
    }

    override fun getItemCount(): Int {

        return items?.size ?: 0
    }

    fun reloadAdapter(newItems: MutableList<HeadersItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }


    var onDeleteImageClickListener: OnItemClickListener? = null
    var onConfirmImageClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClick(position: Int, items: MutableList<HeadersItem>, itemView: HeadersItem)
    }
}