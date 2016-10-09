package com.valkyrie.nabeshimamac.lightsout.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.valkyrie.nabeshimamac.lightsout.model.Question;
import com.valkyrie.nabeshimamac.lightsout.R;
import com.valkyrie.nabeshimamac.lightsout.activity.MakeActivity;

import java.text.SimpleDateFormat;

/**
 * ListViewのAdapter
 */
public class QuestionAdapter extends ArrayAdapter<Question> implements View.OnClickListener {
    LayoutInflater inflater;

    public QuestionAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, QuestionAdapter.class);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_question, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.textTitle);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.textDate);
            viewHolder.detailTextView = (TextView) convertView.findViewById(R.id.textDetail);
            viewHolder.deleteImageView = (ImageView) convertView.findViewById(R.id.imageDelete);
            viewHolder.editImageView = (ImageView) convertView.findViewById(R.id.imageEdit);
            //Idの関連付け

            viewHolder.editImageView.setOnClickListener(this);
            viewHolder.deleteImageView.setOnClickListener(this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Question item = getItem(position);
        viewHolder.titleTextView.setText(item.title);
        int emptyCount = 0;
        for (int i = 0; i < item.board.length(); i++) {
            if (item.board.charAt(i) == '0') {
                emptyCount++;
            }
        }

        viewHolder.deleteImageView.setTag(position);
        viewHolder.editImageView.setTag(position);
        viewHolder.detailTextView.setText("・盤面のサイズ : " + item.width + "×" + item.height +
                " \n・空のマス : " + emptyCount);
        //盤面の情報
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm'('EEE')'"); // 日
        if (item.createdAt != null) {
            viewHolder.dateTextView.setText(simpleDateFormat.format(item.createdAt));
        }
        //日付の情報
        return convertView;
    }

    @Override
    public void onClick(View v) {
        final int position = (Integer) v.getTag();
        final Question question = getItem(position);
        switch (v.getId()) {
            case R.id.imageEdit:
                // Editボタンが押された時の処理
                // 作成画面に問題のIDを渡す
                final Intent intent = new Intent(getContext(), MakeActivity.class);
                intent.putExtra("question_id", question.getId());
                getContext().startActivity(intent);
                break;
            case R.id.imageDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(false);
                builder.setTitle("本当に削除しますか？");
                builder.setMessage("セーブ" +
                        "データは戻ってきません！！");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        question.delete();
                        remove(question);
                    }
                });
                builder.setNegativeButton("Cansel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                break;
        }
        // Deleteボタンが押された時の処理
    }

    class ViewHolder {
        TextView titleTextView;
        TextView detailTextView;
        TextView dateTextView;
        ImageView editImageView;
        ImageView deleteImageView;
    }
}
