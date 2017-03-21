package com.example.naresh.demoproject_1.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.fragments.ProfileFragment;
import com.example.naresh.demoproject_1.models.QuestionItem;
import com.example.naresh.demoproject_1.models.User;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 20/3/17.
 */

public class QuestionAdapter extends BaseAdapter {
    private String TAG = "Question Adapter";
    private Context context;
    private List<QuestionItem> questionItems;

    public QuestionAdapter(Context context) {
        this.context = context;
        this.questionItems = new ArrayList<>();
    }



    public void addItems(List<QuestionItem> arraylist) {
        questionItems.addAll(arraylist);
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        questionItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return questionItems.size();
    }

    @Override
    public QuestionItem getItem(int position) {
        return questionItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater mInflater =(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_qa_profile_fragment, null);
            viewHolder = new ViewHolder();

            viewHolder.imagePostType = (ImageView) convertView.findViewById(R.id.image_post_type);
            viewHolder.textPostTitle = (TextView) convertView.findViewById(R.id.text_post_title);
            viewHolder.textPostType = (TextView) convertView.findViewById(R.id.text_post_type);
            viewHolder.textPostDetail = (TextView) convertView.findViewById(R.id.text_post_body);
            viewHolder.textDetailNotFound = (TextView)convertView.findViewById(R.id.text_detail_not_available);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        QuestionItem questionItem = getItem(position);

        String mPostType = questionItem.getPostType();
        String postTitle = questionItem.getTitle();
        String postBody = questionItem.getBody();

            if (mPostType.equalsIgnoreCase(Constants.POST_TYPE_ANSWER)) {
                viewHolder.textPostType.setText("Answer");
                viewHolder.imagePostType.setImageResource(R.drawable.ic_answer_profile_fragment);
            } else if (mPostType.equalsIgnoreCase(Constants.POST_TYPE_QUESTION)) {
                viewHolder.textPostType.setText("Question");
                // holder.textPostType.setText(R.string.post_type_question_text);
                viewHolder.imagePostType.setImageResource(R.drawable.ic_question_profile_fragment);
            }
            viewHolder.textPostTitle.setText(Utility.convertTextToHTML(postTitle));
            viewHolder.textPostDetail.setText(Utility.convertTextToHTML(postBody));


        return convertView;
    }

    private class ViewHolder {

        ImageView imagePostType;
        TextView textPostType, textPostTitle, textPostDetail,textDetailNotFound;
    }
}
