package com.idealist.www.myapplication2.dream_post.dream_post_comment;

import
        android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_post.dream_post_change;
import com.idealist.www.myapplication2.dream_post.dream_post_item;
import com.idealist.www.myapplication2.lucidmain;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment.listItem;
import static com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment.text;
import static com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment.time;
import static com.idealist.www.myapplication2.dream_post.dream_post_comment.dream_post_comment.writer;
import static com.idealist.www.myapplication2.lucidmain.id;

/**
 * Created by 박종원 on 2018-04-10.
 */

public class dream_post_comment_adapter extends BaseAdapter {

    String id;

    int position;

    private Activity mContext;
    static ArrayList<dream_post_comment_item> listItem = new ArrayList<>();
    private ArrayList<dream_post_comment_item> listitems;
    TextView cvrv_id;
    TextView cvrv_tv;
    TextView time;

    ImageView comment_change;
    ImageView comment_delete;

    public dream_post_comment_adapter(android.content.Context context, ArrayList<dream_post_comment_item> listitem) {

        mContext = (Activity) context;
        listitems = listitem;

        SharedPreferences sharepref = mContext.getSharedPreferences("Operation", MODE_PRIVATE);
        id = sharepref.getString("Operation", null);

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

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_dream_post_comment, null);
        }

        cvrv_id = convertView.findViewById(R.id.cvrv_id);
        cvrv_tv = convertView.findViewById(R.id.cvrv_tv);
        time = convertView.findViewById(R.id.comment_time);

        final dream_post_comment_item item = (dream_post_comment_item) getItem(position);

        cvrv_id.setText(item.getId());
        cvrv_tv.setText(item.getText());
        time.setText(item.getTime());

        comment_change = convertView.findViewById(R.id.comment_change);
        comment_delete = convertView.findViewById(R.id.comment_delete);

        comment_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                final View mView = mContext.getLayoutInflater().inflate(R.layout.custom_dream_post_comment_change,null);
                final EditText edt_comment_change = mView.findViewById(R.id.edt_comment_change);
                edt_comment_change.setText(item.getText());
                Button btn_comment_change = mView.findViewById(R.id.btn_comment_change);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btn_comment_change.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        dream_post_comment_item items = new dream_post_comment_item(id, edt_comment_change.getText().toString(),item.getTime());

                        listitems.remove(position);
                        listitems.add(position, items);
                        notifyDataSetChanged();


                        dialog.dismiss();

                        Toast.makeText(mContext, "댓글이 수정되었습니다.", Toast.LENGTH_SHORT).show();


                        //SAVE
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(listitems);
                        editor.putString(writer + text+time, json); //postnumber로 구분을 해준다!
                        editor.commit();

                    }
                });


            }
        });

        comment_delete.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show(position);
            }
        });



        if (cvrv_id.getText().toString().equals(lucidmain.id)) {
        } else{
            comment_change.setVisibility(View.INVISIBLE);
            comment_delete.setVisibility(View.INVISIBLE);
        }

        // 다른 아이디로 로그인시 댓글 수정삭제가 불가능하게 설정했음

        return convertView;
    }


    private void delete(int position) {

        listitems.remove(position);
        notifyDataSetChanged();

        //SAVE
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listitems);
        editor.putString(writer + text+time, json);
        editor.commit();
        Toast.makeText(mContext, "댓글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

    }



    void show(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("해당 댓글을 삭제하시겠습니까?");
        builder.setMessage("삭제된 정보는 복구불가능합니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(position);
                        comment_number();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();


    }

    public void comment_number() {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("dream_post_comment", MODE_PRIVATE); // 로그인한 id 저장소를 불러오기, 이 때 불려와지는 friend list는 각각 다름
        Gson gson = new Gson();
        String json = sharedPreferences.getString(writer + text+time, null);
        Type type = new TypeToken<ArrayList<dream_post_comment_item>>() {
        }.getType();
        listItem = gson.fromJson(json, type);
        if (listItem == null) {
            listItem = new ArrayList<>();
        }

        int commentCount;

        if (listItem.size() >= 1000) {
            commentCount = 999;
        } else {
            commentCount = listItem.size();
        }


        dream_post_item items = new dream_post_item(dream_post_comment.image_string, dream_post_comment.writer, dream_post_comment.text, false, dream_post_comment.likecount, commentCount,dream_post_comment.time);
        dream_post_comment.listitems.remove(dream_post_comment.position);
        dream_post_comment.listitems.add(dream_post_comment.position, items);

        //SAVE
        SharedPreferences sharedPre = mContext.getSharedPreferences("dream_post", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPre.edit();
        Gson gsons = new Gson();
        String jsons = gsons.toJson(dream_post_comment.listitems);
        editor.putString("postlist", jsons);
        editor.commit();

    }

}
