package com.valkyrie.nabeshimamac.lightsout.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.model.SharedQuestion;

/**
 * ListViewのAdapter
 */
public class SharedQuestionAdapter extends ArrayAdapter<SharedQuestion> {
    LayoutInflater inflater;

    public SharedQuestionAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SharedQuestionAdapter.class);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shared_question, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.textTitle);
            viewHolder.detailTextView = (TextView) convertView.findViewById(R.id.textDetail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SharedQuestion item = getItem(position);
        viewHolder.titleTextView.setText(item.title);
        int emptyCount = 0;
        for (int i = 0; i < item.board.length(); i++) {
            if (item.board.charAt(i) == '0') {
                emptyCount++;
            }
        }
        viewHolder.detailTextView.setText("・盤面のサイズ : " + item.width + "×" + item.height +
                " \n・空のマス : " + emptyCount);
        //盤面の情報
        return convertView;
    }

    class ViewHolder {
        TextView titleTextView;
        TextView detailTextView;
    }
}
