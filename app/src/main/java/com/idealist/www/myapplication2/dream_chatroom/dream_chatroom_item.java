package com.idealist.www.myapplication2.dream_chatroom;

/**
 * Created by 박종원 on 2018-04-22.
 */

public class dream_chatroom_item {

    String messege_send_id;
    String messege_time;
    String messege_text;

    public dream_chatroom_item(String messege_send_id, String messege_time, String messege_text) {
        this.messege_send_id = messege_send_id;
        this.messege_time = messege_time;
        this.messege_text = messege_text;
    }

    public String getMessege_send_id() {
        return messege_send_id;
    }

    public String getMessege_time() {
        return messege_time;
    }

    public String getMessege_text() {
        return messege_text;
    }

}
