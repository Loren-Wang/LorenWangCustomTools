package com.example.testapp.image

import android.lorenwang.customview.dialog.AvlwZoomablePreviewDialog
import android.lorenwang.customview.imageview.AvlwFrescoZoomableImageView
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.BaseActivity
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_zoomable_image_view.*

class ZoomableImageViewActivity : BaseActivity() {
    /**
     * 子类create
     */
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_zoomable_image_view)
        vpgList.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(LayoutInflater.from(this@ZoomableImageViewActivity)
                        .inflate(R.layout.item_list_zoomable_imageview, parent,false)) {}
            }

            override fun getItemCount(): Int {
                return 1
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val imageView = holder.itemView.findViewById<AvlwFrescoZoomableImageView>(R.id.imgZoom)

                imageView.setImageURI(Uri.parse("http://l.paipaitxt" +
                        ".com/118851/10/06/30/104_10897237_4ee2ef93d633782.jpg"))
            }

        }

        imgZoom.setImageURI(Uri.parse("http://l.paipaitxt" +
                ".com/118851/10/06/30/104_10897237_4ee2ef93d633782.jpg"))

        btnDialog.setOnClickListener {
            val dialog = AvlwZoomablePreviewDialog(this)
            dialog.setImagePath(R.drawable.ic_launcher_background,R.drawable
                    .icon_empty_add_unable,
            "https://qcampfile.oss-cn-shanghai.aliyuncs" +
                    ".com/qcamp/answerConfig/1910/28/1572252438644_1204x8193.png")
            dialog.show()
        }
    }
}
