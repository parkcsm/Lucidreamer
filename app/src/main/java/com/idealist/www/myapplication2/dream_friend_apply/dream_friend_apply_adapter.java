package com.idealist.www.myapplication2.dream_friend_apply;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idealist.www.myapplication2.MainActivity;
import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_friend.dream_friend_adapter;
import com.idealist.www.myapplication2.dream_friend.dream_friend_item;
import com.idealist.www.myapplication2.dream_post.dream_post;
import com.idealist.www.myapplication2.dream_post.dream_post_item;
import com.idealist.www.myapplication2.lucidmain;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 박종원 on 2018-04-18.
 */

public class dream_friend_apply_adapter extends BaseAdapter {

    String profilephoto, profilepr;
    public TextView friend_apply_name, friend_accept, friend_reject;
    private Activity mContext;
    private ArrayList<dream_friend_apply_item> listitem;
    private ArrayList<dream_friend_item> friend_listItem;
    private ArrayList<dream_friend_item> friend_listItem2;


    public dream_friend_apply_adapter(Context context, ArrayList<dream_friend_apply_item> listItem) {

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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {


        final dream_friend_apply_item items = (dream_friend_apply_item) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_friend_apply_list, null);
        }


        load(items.getFriend_apply_name());
        friend_apply_name = convertView.findViewById(R.id.friend_apply_name);
        friend_apply_name.setText(items.getFriend_apply_name());

        friend_accept = convertView.findViewById(R.id.friend_accept);
        friend_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences SharedPref = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                boolean Check_Friendlist = SharedPref.getBoolean(lucidmain.id + items.getFriend_apply_name(), false);
                if (Check_Friendlist == true) {
                    Toast.makeText(mContext, "이미 상대방이 나의 친구 요청을 수락하여 친구가 되었습니다.", Toast.LENGTH_SHORT).show();
                    listitem.remove(position);
                    notifyDataSetChanged();

                    //SAVE - friend_apply 리스트에서 보이지 않게 // 삭제한 것을 반영
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gsons = new Gson();
                    String jsons = gsons.toJson(listitem);
                    editor.putString("friend_apply_list", jsons);
                    editor.commit();

                } else {
                    friend_accept();
                    friend_list_add();
                }
            }

            private void friend_accept() {
                Toast.makeText(mContext, "친구요청을 수락했습니다. 꿈 친구 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show();

                listitem.remove(position);
                notifyDataSetChanged();

                //SAVE - friend_apply 리스트에서 보이지 않게 // 삭제한 것을 반영
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gsons = new Gson();
                String jsons = gsons.toJson(listitem);
                editor.putString("friend_apply_list", jsons);
                editor.commit();


            }

            private void friend_list_add() {

                //이부분은 친구목록 item파일에 넣을 프사, 상메를 불러오는 부분
                SharedPreferences shared = mContext.getSharedPreferences(items.getFriend_apply_name(), MODE_PRIVATE);
                profilephoto = shared.getString("profilephoto", null);
                profilepr = shared.getString("profilepr", null);
                //불러온 목록의 가장 위로 아이템을 추가한다.
                dream_friend_item item = new dream_friend_item(profilephoto, items.getFriend_apply_name(), profilepr);
                friend_listItem.add(0, item);
                //SAVE
                //여기에 보여지는 친구목록에 친구이름을 저장해준다.
                SharedPreferences sharedPref = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPref.edit();
                Gson gsonss = new Gson();
                String jsonss = gsonss.toJson(friend_listItem);
                edit.putString("friend_list", jsonss);
                edit.commit();
                //여기까지 내 아이디 친구리스트에 해당친구 저장하기

                //여기부터는 상대 아이디 친구리스트에 내 아이디 저장하기
                SharedPreferences shared2 = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                profilephoto = shared2.getString("profilephoto", null);
                profilepr = shared2.getString("profilepr", null);
                //불러온 목록의 가장 위로 아이템을 추가한다.
                dream_friend_item item2 = new dream_friend_item(profilephoto, lucidmain.id, profilepr);
                friend_listItem2.add(0, item2);
                //여기에 보여지는 친구목록에 친구이름을 저장해준다.
                SharedPreferences sharedPref2 = mContext.getSharedPreferences(items.getFriend_apply_name(), MODE_PRIVATE);
                SharedPreferences.Editor edit2 = sharedPref2.edit();
                Gson gson2 = new Gson();
                String json2 = gson2.toJson(friend_listItem2);
                edit2.putString("friend_list", json2);
                edit2.commit();
                //여기까지 내 친구의 친구 리스트에 내아이디 저장하기


                ///////////////////////////////  여기부터는 아이디에따라 친구신청제한거는 부분
                //SAVE
                //친구목록에 있으면 true로 바꿔준다. 이거는 로그인아이디 + 친구아이디 / 친구아이디 + 로그인아이디 한쌍이 아니라
                // 로그인아이디 + 친구아이디 한쌍만 취급한다. 왜냐하면, 이미 친구면 누가 로그인하든 친구신청이 되면 안되기 때문이다.
                SharedPreferences SharedPref = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor edits = SharedPref.edit();
                edits.putBoolean(lucidmain.id + items.getFriend_apply_name(), true);
                edits.commit();

                SharedPreferences SharedPref2 = mContext.getSharedPreferences(items.getFriend_apply_name(), MODE_PRIVATE);
                SharedPreferences.Editor edits2 = SharedPref2.edit();
                edits2.putBoolean(items.getFriend_apply_name() + lucidmain.id, true);
                edits2.commit();

                //SAVE
                //거절했으면 신청확인을 false로 표시해준다.
                //false면 friend_info에서 다시한번 신청할 수 있다. (true면 걸리는 구조)
                SharedPreferences SharedPrefss = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor editsss = SharedPrefss.edit();
                editsss.putBoolean(lucidmain.id + items.getFriend_apply_name() + "apply", false); //내가 보낼 수 있다.
                editsss.putBoolean(items.getFriend_apply_name() + "apply" + lucidmain.id, false); //내가 친추 받을 수 있다.
                //사실 여기서 false로 굳이 안바꿔도 되지만, 나중에 친구삭제했을때 다시 친추 가능하게 하기 위해 false로 바꿨다.
                editsss.commit();


            }
        });

        friend_reject = convertView.findViewById(R.id.friend_reject);
        friend_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences SharedPref = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                boolean Check_Friendlist = SharedPref.getBoolean(lucidmain.id + items.getFriend_apply_name(), false);
                if (Check_Friendlist == true) {
                    Toast.makeText(mContext, "이미 상대방이 나의 친구 요청을 수락하여 친구가 되었습니다.", Toast.LENGTH_SHORT).show();
                    listitem.remove(position);
                    notifyDataSetChanged();

                    //SAVE - friend_apply 리스트에서 보이지 않게 // 삭제한 것을 반영
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gsons = new Gson();
                    String jsons = gsons.toJson(listitem);
                    editor.putString("friend_apply_list", jsons);
                    editor.commit();

                } else {
                    friend_reject();
                }
            }

            private void friend_reject() {
                Toast.makeText(mContext, "친구요청을 거절했습니다.", Toast.LENGTH_SHORT).show();
                listitem.remove(position);
                notifyDataSetChanged();

                //SAVE
                //여기에 보여지는 친구목록에 친구이름을 저장해준다.
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gsons = new Gson();
                String jsons = gsons.toJson(listitem);
                editor.putString("friend_apply_list", jsons);
                editor.commit();

                //SAVE
                //거절했으면 신청확인을 false로 표시해준다.
                //false면 friend_info에서 다시한번 신청할 수 있다. (true면 걸리는 구조)
                //저장소 위치로 items.getFriend_apply_name();
                SharedPreferences SharedPrefss = mContext.getSharedPreferences(items.getFriend_apply_name(), MODE_PRIVATE);
                SharedPreferences.Editor edit = SharedPrefss.edit();
                edit.putBoolean(items.getFriend_apply_name() + lucidmain.id + "apply", false);

                // 내가 친추를 거절했기때문에 상대방만 다시 친추 걸 수 있다. 내가 친추를 다시 걸 수 있는지의 여부는 상관없다.
                // 내가 친추를 거절했더라도, 내가 그에게 다시 친추할수있는 여부와는 상관이 없다.
                // 왜냐하면, 내가 친추를 거는 부분은, 상대방 친추창에 내 아이디가 있느냐여부와 관련이 있기 때문이다.
                edit.commit();


            }
        });


        return convertView;
    }


    private void load(String friend_apply_name) {

        // 내아이디에 상대친구 저장하기 -> 내 친구목록 로드부분
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences share = mContext.getSharedPreferences(lucidmain.id, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson = new Gson();
        String json = share.getString("friend_list", null);
        Type type = new TypeToken<ArrayList<dream_friend_item>>() {
        }.getType();
        friend_listItem = gson.fromJson(json, type);
        if (friend_listItem == null) {
            friend_listItem = new ArrayList<>();
        }
        // 상대아이디에 내 아이디 저장하기 -> 상대 친구목록 로드부분
        // 저장하기위해서는 기존 목록을 불러와야한다.
        SharedPreferences share2 = mContext.getSharedPreferences(friend_apply_name, MODE_PRIVATE);  // 친구하고 싶은사람 id에
        Gson gson2 = new Gson();
        String json2 = share2.getString("friend_list", null);
        Type type2 = new TypeToken<ArrayList<dream_friend_item>>() {
        }.getType();
        friend_listItem2 = gson2.fromJson(json2, type2);
        if (friend_listItem2 == null) {
            friend_listItem2 = new ArrayList<>();
        }
    }

}
