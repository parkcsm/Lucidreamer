package dream_diary;

import android.net.Uri;

/**
 * Created by 박종원 on 2018-05-07.
 */

public class dream_diary_item {

    public String time;
    public String content;


    // 안드로이드 현재시간을 표시해주는 변수를 이곳에 선언한다.


    public dream_diary_item(String time, String content) {
        this.time = time;
        this.content = content;
    }


    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }


}
