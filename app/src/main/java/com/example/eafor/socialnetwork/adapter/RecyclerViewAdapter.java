package com.example.eafor.socialnetwork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;
import com.example.eafor.socialnetwork.activities.MainActivity;
import com.example.eafor.socialnetwork.support.AvatarAdapter;
import com.example.eafor.socialnetwork.support.TimeManager;
import com.example.eafor.socialnetwork.support.UserData;

import java.util.List;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<UserData> listUserData;
    Context mContext;
    Message msg;

    public RecyclerViewAdapter(List<UserData> list, Context mContext) {
        this.listUserData=list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_container, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        AvatarAdapter.setImg(Integer.parseInt(listUserData.get(i).avatar),mContext, viewHolder.image);
        viewHolder.imageName.setText(listUserData.get(i).nickname);
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = new Message();
                msg.obj = "/code_load_user";
                MainActivity.chosenUserId=i;
                MainActivity.handler.sendMessage(msg);

            }
        });
        if(listUserData.get(i).status.equals("online")) {
            viewHolder.userStatus.setText(listUserData.get(i).status);
            viewHolder.userStatus.setTextColor(mContext.getResources().getColor(R.color.colorGreenOnline));
        }else{
            viewHolder.userStatus.setText(TimeManager.parseString(listUserData.get(i).last_online));
            viewHolder.userStatus.setTextColor(mContext.getResources().getColor(R.color.colorGray1));
        }

    }

    @Override
    public int getItemCount() {
        return listUserData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName, userStatus;
        FrameLayout parentLayout;

           public ViewHolder(View itemView){
               super(itemView);
               image = itemView.findViewById(R.id.image);
               imageName=itemView.findViewById(R.id.user_nick);
               parentLayout = itemView.findViewById(R.id.parent_holder);
               userStatus=itemView.findViewById(R.id.user_status);
           }
    }
}
