package dream_diary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.idealist.www.myapplication2.R;
import com.idealist.www.myapplication2.dream_post.dream_post_item;

import java.util.ArrayList;

/**
 * Created by 박종원 on 2018-05-07.
 */


public class dream_diary_adapter extends BaseAdapter {

    TextView tv_time;
    TextView tv_content;
    private Activity mContext;
    private ArrayList<dream_diary_item> listitems;

    public dream_diary_adapter(Context context, ArrayList<dream_diary_item> listItem) {

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
    public View getView(int position, View convertView, ViewGroup parent) {

        final dream_diary_item item = (dream_diary_item) getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_dream_post, null);
        }

        tv_time = convertView.findViewById(R.id.tv_time);
        tv_content = convertView.findViewById(R.id.tv_content);

        tv_time.setText(item.getTime());
        tv_content.setText(item.getContent());

        return convertView;
    }
}
