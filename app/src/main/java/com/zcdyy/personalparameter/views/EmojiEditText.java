package com.zcdyy.personalparameter.views;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiEditText extends EditText {

	public EmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public String getEmojiContent(){
		String emoji = getText().toString();
		return filterEmoji(emoji);
	}
	public static String filterEmoji(String source) {
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern. CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            while (emojiMatcher.find()) {
            	String key = emojiMatcher.group();
            	source = source.replace(key, URLEncoder.encode(key));
			}
        return source;
       }
       return source;  
    }
}
