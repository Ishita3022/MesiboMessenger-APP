

package org.mesibo.messenger

import android.app.Dialog
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.mesibo.api.Mesibo
import com.mesibo.api.MesiboUtils

import java.util.ArrayList


class BlockedSettingsFragment : Fragment() {

    internal lateinit var mBlockedList: RecyclerView
    internal lateinit var mBlockedLayout: LinearLayout
    internal lateinit var mBlockedContacts: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.activity_blocked_users, container, false)


        val mToolbar = v.findViewById<Toolbar>(R.id.toolbar)


        mBlockedList = v.findViewById(R.id.blockedRecyclerList)

        mBlockedLayout = v.findViewById(R.id.blockedLayout)
        mBlockedContacts = v.findViewById(R.id.blockedContactTV)

        RelaodList()

        return v

    }


    fun RelaodList() {
        val usersList = Mesibo.getSortedUserProfiles()


        val newBlockedList = ArrayList<Mesibo.UserProfile>()


        for (i in usersList.indices) {

            if (usersList[i].groupid <= 0 && usersList[i].isBlocked) {

                val userProfile = usersList[i]
                newBlockedList.add(userProfile)


            }

        }

        setBlockedmUserProfileList(newBlockedList)
        mBlockedContacts.text = "Blocked users : " + newBlockedList.size
    }

    fun setBlockedmUserProfileList(arrayList: ArrayList<Mesibo.UserProfile>) {


        val mAdapter = BlockedContactsAdapter(this, arrayList)

        //mBlockedList.setHasFixedSize(true);
        val layoutManager = LinearLayoutManager(activity)
        mBlockedList.layoutManager = layoutManager
        //mBlockedList.getRecycledViewPool().setMaxRecycledViews(0, 0);
        //mBlockedList.setItemAnimator(new DefaultItemAnimator());
        //mBlockedList.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 70));
        mBlockedList.adapter = mAdapter

    }


    ///Adapter

    inner class BlockedContactsAdapter(private val context: BlockedSettingsFragment, private val mUserProfileList: ArrayList<Mesibo.UserProfile>)//        this.listener = listener;
        : RecyclerView.Adapter<BlockedContactsAdapter.MyViewHolder>() {


        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var name: TextView
            var thumbnail: ImageView
            var callStatus: ImageView? = null
            var callVideo: ImageView? = null
            internal var mTimeStatusLayut: LinearLayout? = null

            init {
                name = view.findViewById(R.id.name)
                thumbnail = view.findViewById(R.id.thumbnail)


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.blocked_user_row_item, parent, false)

            return MyViewHolder(itemView)
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val mUserProfile = mUserProfileList[position]


            val pos = holder.adapterPosition

            holder.name.text = mUserProfileList[pos].name


            val imagePath = Mesibo.getUserProfilePicturePath(mUserProfileList[pos], Mesibo.FileInfo.TYPE_AUTO)
            if (null != imagePath) {
                val b = BitmapFactory.decodeFile(imagePath)
                if (null != b)
                    holder.thumbnail.setImageDrawable(MesiboUtils.getRoundImageDrawable(b))
            }


            holder.itemView.setOnClickListener {
                val dialog = Dialog(activity!!)
                dialog.setCancelable(true)


                dialog.setContentView(R.layout.unblock_row_item)

                val unblock = dialog.findViewById<TextView>(R.id.unblockTV)

                unblock.text = "Unblock " + mUserProfileList[pos].name + ""


                unblock.setOnClickListener {
                    //Do something

                    val mUser1 = Mesibo.getUserProfile(mUserProfileList[pos].address)
                    mUser1.blockMessages(false)
                    mUser1.blockGroupMessages(false)

                    Mesibo.setUserProfile(mUser1, false)

                    Toast.makeText(activity, "Unblocked", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    context.RelaodList()
                }


                dialog.show()
            }


        }

        override fun getItemCount(): Int {
            return mUserProfileList.size
        }


    }


}
