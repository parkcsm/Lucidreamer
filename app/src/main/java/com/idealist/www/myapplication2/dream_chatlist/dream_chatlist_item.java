package com.idealist.www.myapplication2.dream_chatlist;

/**
 * Created by 박종원 on 2018-04-22.
 */

public class dream_chatlist_item {

    String chat_room_photo, chat_room_subject, chat_room_time, chat_room_message;
    int chat_room_number_of_member;

    public dream_chatlist_item(String chat_room_photo, String chat_room_subject, int chat_room_number_of_member, String chat_room_time, String chat_room_message) {
        this.chat_room_photo = chat_room_photo;
        this.chat_room_subject = chat_room_subject;
        this.chat_room_number_of_member = chat_room_number_of_member;
        this.chat_room_time = chat_room_time;
        this.chat_room_message = chat_room_message;
    }

    public String getChat_room_photo() {
        return chat_room_photo;
    }

    public String getChat_room_subject() {
        return chat_room_subject;
    }


    public int getChat_room_number_of_member() {
        return chat_room_number_of_member;
    }


    public String getChat_room_time() {
        return chat_room_time;
    }


    public String getChat_room_message() {
        return chat_room_message;
    }


}
