package com.redmango.profilecard

import android.content.Context
import android.sax.RootElement
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drsmarineservices.nikhil.utility.LogUtil
import com.redmango.profilecard.data.local.db.entitity.ProfileDetails
import java.lang.StringBuilder

class ProfileAdapter(val ctx: Context, val profileDetailsList: List<ProfileDetails>, val onItemClickListener: ProfileAdapter.OnItemClickListener) :
        RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView
        var tv_profile_info: TextView
        var tv_profile_accept_decline_message: TextView
        var iv_profile_image: ImageView
        var ibtn_decline: ImageButton
        var ibtn_accept: ImageButton
        var rl_accept_decline: RelativeLayout

        init {
            tv_name = view.findViewById(R.id.tv_name)
            tv_profile_info = view.findViewById(R.id.tv_profile_info)
            tv_profile_accept_decline_message = view.findViewById(R.id.tv_profile_accept_decline_message)
            ibtn_decline = view.findViewById(R.id.ibtn_decline)
            ibtn_accept = view.findViewById(R.id.ibtn_accept)
            iv_profile_image = view.findViewById(R.id.iv_profile_image)
            rl_accept_decline = view.findViewById(R.id.rl_accept_decline)

            ibtn_accept.setOnClickListener {
                LogUtil.error("Accept Btn Clicked  $adapterPosition")
                onItemClickListener.onAccepted(adapterPosition)
            }

            ibtn_decline.setOnClickListener {
                LogUtil.error("Decline Btn Clicked  $adapterPosition")
                onItemClickListener.onDeclined(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.holder_profile, parent, false)
        return ProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val data=profileDetailsList[position]
        Glide.with(ctx).load(data.picture.thumbnail).into(holder.iv_profile_image)
        val profileInfo: StringBuilder = StringBuilder()
        profileInfo.apply {
            append(data.dob.age)
            append(",")
            append(data.location.state)
            append(",")
            append("\n")
            append(data.location.city)
            append(",")
            append(data.location.country)
        }
        holder.tv_profile_info.text = profileInfo
        holder.tv_name.text = data.name.title
        if (data.isAcceptedDeclined) {

            holder.rl_accept_decline.visibility = View.GONE
            holder.tv_profile_accept_decline_message.text=data.acceptDeclineMessage
            holder.tv_profile_accept_decline_message.visibility = View.VISIBLE
        }
        else{
            holder.tv_profile_accept_decline_message.visibility = View.GONE
            holder.rl_accept_decline.visibility = View.VISIBLE
        }

    }

    override fun getItemCount(): Int {
        return profileDetailsList.size
    }


    interface OnItemClickListener {
        fun onAccepted(position: Int)
        fun onDeclined(position: Int)
    }

}