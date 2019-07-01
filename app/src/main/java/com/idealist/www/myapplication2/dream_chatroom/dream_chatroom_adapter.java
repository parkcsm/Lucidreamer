package com.idealist.www.myapplication2.dream_chatroom;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.lucidmain;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 박종원 on 2018-04-22.
 */

public class dream_chatroom_adapter extends BaseAdapter {

    String profilephoto;
    private Activity mContext;
    private ArrayList<dream_chatroom_item> listitem;

    public dream_chatroom_adapter(Context context, ArrayList<dream_chatroom_item> listItem) {
        mContext = (Activity) context;
        listitem = listItem;
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {

        PersonViewHolder viewHolder;

        final dream_chatroom_item item = (dream_chatroom_item) getItem(position);


        // 캐시된 뷰가 없을 경우 새로생성하고 뷰홀더를 생성한다.
        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_chat_message, null);

            viewHolder = new PersonViewHolder();
            viewHolder.chat_room_friend_image = convertView.findViewById(R.id.chat_room_friend_image);
            viewHolder.my_message = convertView.findViewById(R.id.my_message);
            viewHolder.my_message_time = convertView.findViewById(R.id.my_message_time);
            viewHolder.friend_message = convertView.findViewById(R.id.friend_message);
            viewHolder.friend_message_time = convertView.findViewById(R.id.friend_message_time);
            viewHolder.friend_layout = convertView.findViewById(R.id.friend_layout);
            viewHolder.my_layout = convertView.findViewById(R.id.my_layout);
            viewHolder.friend_chat_id = convertView.findViewById(R.id.friend_chat_id);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (PersonViewHolder) convertView.getTag();
        }

        //캐시된 뷰가 있을 경우 저장된 뷰홀더를 사용한다.

        if (item.getMessege_send_id().equals(lucidmain.id)) {
            viewHolder.friend_layout.setVisibility(View.GONE);
            viewHolder.my_message.setText(item.getMessege_text());
            viewHolder.my_message_time.setText(item.getMessege_time());
        } else {

            //친구 프사 적용해주기
            SharedPreferences shared = mContext.getSharedPreferences(item.getMessege_send_id(), MODE_PRIVATE);
            profilephoto = shared.getString("profilephoto", null);

            Glide.with(mContext).load(photo_Uri()).centerCrop().into(viewHolder.chat_room_friend_image);
            viewHolder.my_layout.setVisibility(View.GONE);
            viewHolder.friend_chat_id.setText(item.getMessege_send_id());
            viewHolder.friend_message.setText(item.getMessege_text());
            viewHolder.friend_message_time.setText(item.getMessege_time());
        }

        return convertView;
    }

    public class PersonViewHolder {
        public ImageView chat_room_friend_image;
        public TextView friend_chat_id;
        public TextView my_message, my_message_time, friend_message, friend_message_time;
        public LinearLayout friend_layout, my_layout;
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
