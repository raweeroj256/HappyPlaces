package je.raweeroj.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import je.raweeroj.happyplaces.R
import je.raweeroj.happyplaces.activities.AddHappyPlaceActivity
import je.raweeroj.happyplaces.activities.MainActivity
import je.raweeroj.happyplaces.database.DatabaseHandler
import je.raweeroj.happyplaces.databinding.ItemHappyPlaceBinding
import je.raweeroj.happyplaces.models.HappyPlaceModel

open class HappyPlacesAdapter(private val context : Context,
                              private var list:ArrayList<HappyPlaceModel>) :
    RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {

    inner class ViewHolder (binding : ItemHappyPlaceBinding):
    RecyclerView.ViewHolder(binding.root){
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val civPlaceImage = binding.ivPlaceImage
    }

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
           ItemHappyPlaceBinding.inflate(
               LayoutInflater.from(parent.context)
               ,parent,
               false)
            )

    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        if(holder is ViewHolder){
            holder.civPlaceImage.setImageURI(Uri.parse(model.image))
            holder.tvTitle.text = model.title
            holder.tvDescription.text = model.description
            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }


    fun notifyEditItem(activity:Activity,position: Int,requestCode:Int){
        val intent = Intent(context,AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
    }



    override fun getItemCount(): Int {
        return list.size
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if(isDeleted >0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }


//    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}