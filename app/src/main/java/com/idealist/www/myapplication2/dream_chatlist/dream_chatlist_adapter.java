package com.idealist.www.myapplication2.dream_chatlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.idealist.www.myapplication2.dream_friend.dream_friend_item;
import com.idealist.www.myapplication2.lucidmain;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 박종원 on 2018-04-22.
 */

public class dream_chatlist_adapter extends BaseAdapter {

    private Activity mContext;
    private ArrayList<dream_chatlist_item> listitem;

    String profilephoto;

    ImageView chat_room_photo;
    TextView chat_room_subject, chat_room_time, chat_room_message;
    Button chat_room_number_of_member;

    public dream_chatlist_adapter(Context context, ArrayList<dream_chatlist_item> listItem) {

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final dream_chatlist_item item = (dream_chatlist_item) getItem(position); //전역으로 할수도 있지만 그냥 안에다가 여러번선언

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_chat_list, null);
        }

        chat_room_photo = convertView.findViewById(R.id.chatroom_image);
        chat_room_subject = convertView.findViewById(R.id.chatroom_subject);
        chat_room_number_of_member = convertView.findViewById(R.id.chatroom_number_of_member);
        chat_room_time = convertView.findViewById(R.id.chatroom_time);
        chat_room_message = convertView.findViewById(R.id.chatroom_front_message);



        SharedPreferences shared = mContext.getSharedPreferences(item.chat_room_subject, MODE_PRIVATE);
        profilephoto = shared.getString("profilephoto", null);
        Glide.with(mContext).load(photo_Uri()).centerCrop().into(chat_room_photo);


        chat_room_subject.setText(item.getChat_room_subject());
        chat_room_number_of_member.setText(item.getChat_room_number_of_member()+"");
        chat_room_time.setText(item.getChat_room_time());
        chat_room_message.setText(item.getChat_room_message());


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myintent = new Intent(mContext, dream_chatting.class);
                myintent.putExtra("FRIENDID", item.getChat_room_subject());
                myintent.putExtra("POSITION",position);
                mContext.startActivity(myintent);

                Toast.makeText(mContext, "1:1채팅을 시작했습니다.", Toast.LENGTH_SHORT).show();

                lucidmain.dream_friend_apply_layout.setVisibility(View.INVISIBLE);
                lucidmain.dream_friend_list_layout.setVisibility(View.INVISIBLE);
                lucidmain.dream_friend_chatlist_layout.setVisibility(View.VISIBLE);

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
