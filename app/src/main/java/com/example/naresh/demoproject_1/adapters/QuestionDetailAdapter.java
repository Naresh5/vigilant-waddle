package com.example.naresh.demoproject_1.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.naresh.demoproject_1.R;
import com.example.naresh.demoproject_1.models.OwnerItem;
import com.example.naresh.demoproject_1.models.QuestionDetailItem;
import com.example.naresh.demoproject_1.utils.Constants;
import com.example.naresh.demoproject_1.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naresh on 24/3/17.
 */


public class QuestionDetailAdapter extends BaseAdapter {
    private Context context;
    private List<QuestionDetailItem> questionDetailItemItems;

    public QuestionDetailAdapter(Context context) {
        this.context = context;
        this.questionDetailItemItems = new ArrayList<>();
    }

    public void addItems(List<QuestionDetailItem> items) {
        questionDetailItemItems.addAll(items);
        notifyDataSetChanged();
    }


    public void removeItems() {
        questionDetailItemItems.clear();
    }

    @Override
    public int getCount() {
        return questionDetailItemItems.size();
    }

    @Override
    public QuestionDetailItem getItem(int position) {
        return questionDetailItemItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_question_detail, null);
            holder = new ViewHolder();
            holder.imageScore = (ImageView) convertView.findViewById(R.id.image_score);
            holder.imageAnswer = (ImageView) convertView.findViewById(R.id.image_answer);
            holder.textQuestionTitle = (TextView) convertView.findViewById(R.id.text_question_title);
            holder.textQuestionCreationDate = (TextView) convertView.findViewById(R.id.text_question_creation_date);
            holder.textTotalAnswer = (TextView) convertView.findViewById(R.id.text_total_answer);
            holder.textUserName = (TextView) convertView.findViewById(R.id.text_user_name);
            holder.textScore = (TextView) convertView.findViewById(R.id.text_score);
            holder.linearLayoutScore = (LinearLayout) convertView.findViewById(R.id.linear_layout_score);
            holder.textTag = (TextView) convertView.findViewById(R.id.text_tag);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        QuestionDetailItem questionDetailItem = getItem(position);
        OwnerItem ownerItem = questionDetailItem.getOwnerItem();

        ArrayList<String> listTag = questionDetailItem.getTags();
        holder.textTag.setText(Utility.arrayToStringConvert(listTag));

        int conditionColor;
        if (questionDetailItem.isAnswered()) {
            conditionColor = R.color.colorIsAnswerTrue;
            holder.imageAnswer.setImageResource(R.drawable.ic_answer_green);
            holder.textTotalAnswer.setTextColor(ContextCompat.getColor(context, R.color.colorScore));
        } else {
            conditionColor = R.color.colorIsAnswerFalse;
            holder.imageAnswer.setImageResource(R.drawable.ic_answer_gray);
            holder.textTotalAnswer.setTextColor(ContextCompat.getColor(context, R.color.colorIsAnswerTotal));
        }
        holder.linearLayoutScore.setBackgroundColor(ContextCompat.getColor(context, conditionColor));
        holder.textQuestionTitle.setText(Utility.convertTextToHTML(questionDetailItem.getTitle()));
        holder.textUserName.setText(ownerItem.getDisplayName());

        long queCreationDate = (long) (questionDetailItem.getQuestionCreationDate());
        String questionCreation = Utility.getDate(queCreationDate,Constants.DATE_FORMAT_QUESTION_CREATION);
        holder.textQuestionCreationDate.setText(questionCreation);
        holder.textTotalAnswer.setText(String.valueOf(questionDetailItem.getAnswerCount()));
        String score = Utility.getQuestionScore(questionDetailItem.getScore());
        holder.textScore.setText(score);
        return convertView;
    }
    private class ViewHolder {
        ImageView imageScore, imageAnswer;
        TextView textScore, textTotalAnswer, textQuestionTitle, textTag,
                textQuestionCreationDate, textUserName;
        LinearLayout linearLayoutScore;
    }
}

