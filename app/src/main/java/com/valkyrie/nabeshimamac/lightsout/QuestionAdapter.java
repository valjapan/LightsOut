package com.valkyrie.nabeshimamac.lightsout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by NabeshimaMAC on 16/08/23.
 */
public class QuestionAdapter extends ArrayAdapter<Question> {
    LayoutInflater mInflater;

    public QuestionAdapter(Context context) {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_question, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.textTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Question item = getItem(position);
        viewHolder.titleTextView.setText(item.title);
        return convertView;
    }

    class ViewHolder {
        TextView titleTextView;
    }

}
