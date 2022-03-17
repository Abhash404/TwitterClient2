package com.codepath.apps.restclienttemplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(private val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivProfileImage: ImageView = itemView.findViewById(R.id.ivProfileImage)
        val tvUserName: TextView = itemView.findViewById(R.id.tvUsername)
        val tvHandle: TextView = itemView.findViewById(R.id.tvHandle)
        val tvTweetBody: TextView = itemView.findViewById(R.id.tvTweetBody)
        val tvTweetTime: TextView = itemView.findViewById(R.id.tvTweetTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tweet: Tweet = tweets[position]

        holder.tvUserName.text = tweet.user?.name
        holder.tvHandle.text = tweet.user?.screenName
        holder.tvTweetBody.text = tweet.body
        holder.tvTweetTime.text = tweet.timestamp
        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }

    override fun getItemCount() = tweets.size

    fun clear() {

        tweets.clear()
        notifyDataSetChanged()
    }

    fun addAll(tweetList: List<Tweet>) {

        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }
}