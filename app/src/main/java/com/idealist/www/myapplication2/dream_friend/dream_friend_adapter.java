package com.idealist.www.myapplication2.dream_friend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_chatroom.dream_chatting;
import com.idealist.www.myapplication2.lucidmain;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 박종원 on 2018-04-18.
 */

public class dream_friend_adapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<dream_friend_item> listitem;

    String profilephoto, profilepr;

    ImageView friend_list_photo, friend_photo;
    TextView friend_list_id, friend_list_text, friend_id, friend_introduce;
    Button friend_chat_start;

    public dream_friend_adapter(Context context, ArrayList<dream_friend_item> listItem) {

        mContext = (Activity) context;
        listitem = listItem;

    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public Object getItem(int position) {
        return listitem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_friend_list, null);
        }

        final dream_friend_item item = (dream_friend_item) getItem(position); //전역으로 할수도 있지만 그냥 안에다가 여러번선언


        friend_list_photo = convertView.findViewById(R.id.friend_list_photo);
        friend_list_id = convertView.findViewById(R.id.friend_list_id);
        friend_list_text = convertView.findViewById(R.id.friend_list_text);

        SharedPreferences shared = mContext.getSharedPreferences(item.getFriend_id(), MODE_PRIVATE);
        profilephoto = shared.getString("profilephoto", null);
        profilepr = shared.getString("profilepr", null);


        Glide.with(mContext).load(photo_Uri()).centerCrop().into(friend_list_photo);
        friend_list_text.setText(profilepr);
        friend_list_id.setText(item.getFriend_id());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = mContext.getLayoutInflater().inflate(R.layout.activity_friend_info, null);


                friend_photo = mView.findViewById(R.id.friend_photo);
                friend_introduce = mView.findViewById(R.id.friend_introduce);
                friend_id = mView.findViewById(R.id.friend_id);
                friend_chat_start = mView.findViewById(R.id.friend_apply);
                friend_chat_start.setText("1:1대화");

                SharedPreferences shared = mContext.getSharedPreferences(item.getFriend_id(), MODE_PRIVATE);
                profilephoto = shared.getString("profilephoto", null);
                profilepr = shared.getString("profilepr", null);


                Glide.with(mContext).load(photo_Uri()).centerCrop().into(friend_photo);
                friend_introduce.setText(profilepr);
                friend_id.setText(item.getFriend_id());

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                friend_chat_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent myintent = new Intent(mContext, dream_chatting.class);
                        myintent.putExtra("FRIENDID", item.getFriend_id());
                        mContext.startActivity(myintent);

                        Toast.makeText(mContext, "1:1채팅을 시작했습니다.", Toast.LENGTH_SHORT).show();

                        lucidmain.dream_friend_apply_layout.setVisibility(View.INVISIBLE);
                        lucidmain.dream_friend_list_layout.setVisibility(View.INVISIBLE);
                        lucidmain.dream_friend_chatlist_layout.setVisibility(View.VISIBLE);

                        dialog.dismiss();


                    }
                });

            }
        });

        return convertView;
    }

    public Uri photo_Uri() {

        //profilephoto를 URI로 바꿔주는 메소드
        //기존의 리스트뷰 잘못만들었음
        //잘못만든 부분 -> array-listitem-json에 프사와 상메를 미리 저장해두고 뿌리게되면 프사와 상메가 수정된것을 그떄그때 반영할 수 없음
        //id만 저장하고, 그때그때 id에 다른 프사와 상메를 불러오는 방법을 썼어야했음.
        //원래대로라면 dream_friend_item인자값을 id만 들어가게 고쳐야하지만, 그냥 저장하고, id만불러오는 식으로 쓰겠음
        // 왜냐면 코드를 간결하게 하는건 지금당장 엄청 중요하진 않음

        if (profilephoto == null) {
            return Uri.parse("content://media/external/images/media/21030");
        } else {
            return Uri.parse(profilephoto);
        }
    }

}
