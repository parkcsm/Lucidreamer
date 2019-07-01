package com.idealist.www.myapplication2.dream_post;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment;
import com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment_item;
import com.idealist.www.myapplication2.friend_info;
import com.idealist.www.myapplication2.lucidmain;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

/**
 * Created by 박종원 on 2018-04-10.
 */

public class dream_post_adapter extends BaseAdapter {

    int position;
    public CheckBox cbLike;
    public ImageView ivImg, iv_change, iv_delete, iv_comment;
    public TextView time;
    public TextView tvLikeCount, tvWriterName, tvWriterText, tvUserName, tvUserComment;

    public TextView comment_number;

    public int Count;
    int commentCount;
    boolean check = true;

    private Activity mContext;
    private ArrayList<dream_post_item> listitems;
    static ArrayList<dream_post_comment_item> listItem = new ArrayList<>();


    public dream_post_adapter(Context context, ArrayList<dream_post_item> listItem) {

        mContext = (Activity) context;
        listitems = listItem;

    }

    @Override
    public int getCount() {
        return listitems.size();
    }

    @Override
    public Object getItem(int position) {

        return listitems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        final dream_post_item item = (dream_post_item) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_dream_post, null);
        }

        ivImg = convertView.findViewById(R.id.iv_img);
        time = convertView.findViewById(R.id.post_time);
        iv_change = convertView.findViewById(R.id.iv_edit);
        iv_delete = convertView.findViewById(R.id.iv_delete);

        if(lucidmain.id.equals(item.getId())){
            iv_change.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
        } else{
            iv_change.setVisibility(View.INVISIBLE);
            iv_delete.setVisibility(View.INVISIBLE);
        }



        tvWriterName = convertView.findViewById(R.id.tv_writer);
        tvWriterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent = new Intent(mContext, friend_info.class);
                myintent.putExtra("FRIENDID", item.getId());
                mContext.startActivity(myintent);
            }
        });

        tvWriterText = convertView.findViewById(R.id.tv_writercoment);
        cbLike = convertView.findViewById(R.id.cb_like);
        tvLikeCount = convertView.findViewById(R.id.tv_like_count);
        iv_comment = convertView.findViewById(R.id.iv_comment);


        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WriteComment(position);
            }
        });

        cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {
                    String Uri_String = item.getUriString();
                    String id = item.getId();
                    String postText = item.getPostText();
                    int commentCount = item.getCommentCount();
                    String time = item.getTime();
                    boolean Like = true;
                    Count = item.getPostLikeCount();
                    Count++;

                    dream_post_item newitem = new dream_post_item(Uri_String, id, postText, Like, Count, commentCount, time);
                    //어차피 아이디별로 하트 on & off는 다르기때문에 게시판별로 저장하는 이 Like는 의미없음
                    listitems.remove(position);
                    listitems.add(position, newitem);

                    //SAVE
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(id + postText + time, "true");
                    editor.commit();                                          //접속한 id가 좋아요 표시한 정보 저장


                    notifyDataSetChanged();

                } else {
                    String Uri_String = item.getUriString();
                    String id = item.getId();
                    String postText = item.getPostText();
                    int commentCount = item.getCommentCount();
                    String time = item.getTime();
                    boolean Like = false;
                    Count = item.getPostLikeCount();
                    Count--;

                    dream_post_item newitem = new dream_post_item(Uri_String, id, postText, Like, Count, commentCount, time);
                    //어차피 아이디별로 하트 on & off는 다르기때문에 게시판별로 저장하는 이 Like는 의미없음
                    listitems.remove(position);
                    listitems.add(position, newitem);


                    //SAVE
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(id + postText + time, "false");
                    editor.commit();                                          //접속한 id가 좋아요 취소한 글 저장


                    notifyDataSetChanged();
                }


                //SAVE
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(listitems);
                editor.putString("postlist", json);
                editor.commit();

            }
        });


        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lucidmain.id.equals(item.getId())) {
                    show(position);
                } else {
                    Toast.makeText(mContext, "글을 삭제할 수 있는 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lucidmain.id.equals(item.getId())) {
                    change(position);
                } else {

                    Toast.makeText(mContext, "글을 수정할 수 있는 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Glide.with(mContext).load(item.getUri()).centerCrop().into(ivImg);
        time.setText(item.getTime());
        tvWriterName.setText(item.getId());
        tvWriterText.setText(item.getPostText());


        SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
        // id에 따라서 이미 하트를 체크했으면 다음에 체크할수 없게 세팅
        if (sharedPreferences.getString(item.getId() + item.getPostText() + item.getTime(), "false").equals("true")) {
            item.isUserLike = true;
        } else {
            item.isUserLike = false;
        }

        cbLike.setChecked(item.getisUserLike());

        tvLikeCount.setText(String.valueOf(item.getPostLikeCount()));





        /*if (item.getId().equals(lucidmain.id)) {
        } else {
            iv_change.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
        }*/
        // 다른 아이디로 로그인시, 글 수정과 삭제가 안보이게 했음!

        comment_number = convertView.findViewById(R.id.comment_number);

        if (item.getCommentCount() == 999) {

            comment_number.setText("999+");

        } else {
            comment_number.setText(item.getCommentCount() + "");
        }
        return convertView;
    }


    private void delete(int position) {

        listitems.remove(position);
        notifyDataSetChanged();

        //SAVE
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listitems);
        editor.putString("postlist", json);
        editor.commit();
        Toast.makeText(mContext, "글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private void change(int position) {

        final dream_post_item item = (dream_post_item) getItem(position); //전역으로 할수도 있지만 그냥 안에다가 여러번선언

        Intent myintent = new Intent(mContext, dream_post_change.class);
        myintent.putExtra("ID", item.getId());
        myintent.putExtra("TEXT", item.getPostText());
        myintent.putExtra("IMAGE", item.getUriString());
        myintent.putExtra("TIME", item.getTime());
        myintent.putExtra("POSITION", position);
        this.mContext.startActivity(myintent);
    }

    private void WriteComment(int position) {
        final Intent myintent;

        myintent = new Intent(mContext, dream_post_comment.class);


        final dream_post_item item = (dream_post_item) getItem(position);
//전역으로 할수도 있지만 그냥 안에다가 여러번선언
        myintent.putExtra("ID", item.getId());
        myintent.putExtra("TEXT", item.getPostText());
        myintent.putExtra("IMAGE", item.getUriString());
        myintent.putExtra("LIKECOUNT", item.getPostLikeCount());
        myintent.putExtra("POSITION", position);
        myintent.putExtra("TIME", item.getTime());
        this.mContext.startActivity(myintent);
        //this가 핵심이었네!!

    }

    void show(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("해당 글을 삭제하시겠습니까?");
        builder.setMessage("삭제된 정보는 복구불가능합니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();


    }


    private void comment_number() {

        final dream_post_item item = (dream_post_item) getItem(position); //전역으로 할수도 있지만 그냥 안에다가 여러번선언
        // 글쓴사람, 글쓴 것 불러오기위해 선언

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        Gson gson = new Gson();
        String json = sharedPreferences.getString(item.getId() + item.getPostText(), null);
        Type type = new TypeToken<ArrayList<dream_post_comment_item>>() {
        }.getType();
        listItem = gson.fromJson(json, type);
        if (listItem == null) {
            listItem = new ArrayList<>();
        }

        // 해당 글에 댓글이 얼마만큼 달렸는지 표시해주기위해서 기존 dream_post_comment의 load를 불러왔음

        if (listItem.size() >= 1000) {
            commentCount = 999;
        } else {
            commentCount = listItem.size();
        }


        dream_post_item items = new dream_post_item(item.getUriString(), item.getId(), item.getPostText(), false, 0, commentCount
                , item.getTime());
        listitems.remove(position);
        listitems.add(position, items);

        //SAVE
        SharedPreferences sharedPre = mContext.getSharedPreferences("dream_post", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPre.edit();
        Gson gsons = new Gson();
        String jsons = gsons.toJson(listItem);
        editor.putString("postlist", jsons);
        editor.commit();

    }
}


