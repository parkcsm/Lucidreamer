package com.idealist.www.myapplication2.dream_post;

import android.net.Uri;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 박종원 on 2018-02-25.
 */

public class dream_post_item {


    String uriString;
    String id;
    String postText;
    String time;

   public boolean isUserLike;
   public int postLikeCount;
   public int commentCount;


   // 안드로이드 현재시간을 표시해주는 변수를 이곳에 선언한다.




    public dream_post_item(String uriString, String id, String postText, boolean isUserLike , int postLikeCount, int commentCount,
                           String time) {
        this.uriString = uriString;
        this.id = id;
        this.postText = postText;
        this.isUserLike = isUserLike;
        this.postLikeCount = postLikeCount;
        this.commentCount = commentCount;
        this.time = time;
    }




    public Uri getUri() {return Uri.parse(uriString);}
    public String getUriString() {return uriString;}
    public String getId() {
        return id;
    }
    public String getPostText() {
        return postText;
    }
    public boolean getisUserLike() {
        return isUserLike;
    }
    public int getPostLikeCount() {
        return postLikeCount;
    }

    public void setPostchecked(boolean ischecked){ this.isUserLike = ischecked;}

    public int getCommentCount(){ return commentCount;}
    public String getTime(){return time;}
}
