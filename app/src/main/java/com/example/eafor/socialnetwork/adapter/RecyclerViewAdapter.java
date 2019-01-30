package com.example.eafor.socialnetwork.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eafor.socialnetwork.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<UserData> listUserData;
    Context mContext;

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
                Toast.makeText(mContext, listUserData.get(i).nickname, Toast.LENGTH_SHORT).show();
            }
        });
        if(listUserData.get(i).status.equals("online")) {
            viewHolder.userStatus.setText(listUserData.get(i).status);
        }else{
            viewHolder.userStatus.setText(listUserData.get(i).last_online);
        }

    }

    @Override
    public int getItemCount() {
        return listUserData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView imageName, userStatus;
        ConstraintLayout parentLayout;

           public ViewHolder(View itemView){
               super(itemView);
               image = itemView.findViewById(R.id.image);
               imageName=itemView.findViewById(R.id.user_nick);
               parentLayout = itemView.findViewById(R.id.parent_holder);
               userStatus=itemView.findViewById(R.id.user_status);
           }
    }
}
