package gr.repo.chessgame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gr.repo.chessgame.R

class MovesAdapter(
        val moveList: MutableList<String>

): RecyclerView.Adapter<MovesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.move_item, parent, false)
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var view = itemView.findViewById<TextView>(R.id.moveTxtV)

        fun bind(item: String, position: Int) {
            if(item == "**") {
                view.text = "Path"
            }else{
                if((position + 1) != moveList.size){
                    if(moveList[position + 1] == "**"){
                        view.text = ""
                    }else {
                        view.text = "$item -> ${moveList[position + 1]}"
                    }
                }else{
                    view.text = ""
                }
            }
        }
    }

    fun updateItems(newItems: List<String>) {
        moveList.clear()
        moveList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(moveList[position], position)
    }

    override fun getItemCount() = moveList.size

}