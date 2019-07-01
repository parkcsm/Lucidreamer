package com.idealist.www.myapplication2.dream_post.dream_post_comment;

/**
 * Created by 박종원 on 2018-04-10.
 */

public class dream_post_comment_item {

    String id;
    String text;
    String time;


    public dream_post_comment_item(String id,String text,String time) {
        this.id = id;
        this.text = text;
        this.time = time;
    }

    public String getId() {return id;}
    public String getText() {return text;}
    public String  getTime() {return time;}



}
