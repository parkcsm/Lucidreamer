package com.idealist.www.myapplication2.dream_friend;

import android.net.Uri;

/**
 * Created by 박종원 on 2018-04-18.
 */

public class dream_friend_item {

    String friend_photo,friend_id,friend_text;


    public dream_friend_item(String friend_photo, String friend_id, String friend_text) {
        this.friend_photo = friend_photo;
        this.friend_id = friend_id;
        this.friend_text = friend_text;
    }


    public Uri getFriend_photo() {
        if(friend_photo==null){
            return Uri.parse("content://media/external/images/media/21030");
        } else{
            return Uri.parse(friend_photo);
        }


    }
    public String getFriend_id() {
        return friend_id;
    }
    public String getFriend_text() {
        return friend_text;
    }

}

