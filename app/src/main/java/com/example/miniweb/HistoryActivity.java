package com.example.miniweb;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    public static final int RESULT_HistoryActivity = 1;
    History_helper db;
    ContactImageAdapter adapter;
    RelativeLayout history_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.history_layout);
        setTitle("History");

        history_layout = findViewById(R.id.history_layout);


        ArrayList<Website1> imageArry = new ArrayList<>();

        db = new History_helper(this);

        // display main List view bcard and contact name

        // Reading all contacts from database
        List<Website1> website1s = db.getAllWebsite();
        for (Website1 cn : website1s) {
            String log = "ID:" + cn.getID() + " Name: " + cn.getTitle() + " Url: " + cn.getUrl()
                    + "Date:" + cn.getDate() + " )";

            // Writing Contacts to log
            Log.d("Result: ", log);
            //add contacts data in arrayList
            imageArry.add(cn);

        }
        adapter = new ContactImageAdapter(this, R.layout.activity_view_record,
                imageArry);
        ListView listView = findViewById(R.id.list_view);
        adapter.notifyDataSetChanged();
        listView.setEmptyView(findViewById(R.id.empty));

        listView.setAdapter(adapter);


        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                TextView descTextView = view.findViewById(R.id.url);
                String historyUrl = descTextView.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("historyUrl", historyUrl);
                setResult(HistoryActivity.RESULT_HistoryActivity, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.delete_all) {

            final TextView mCreditsTitle = new TextView(this);
            mCreditsTitle.setText("");
            mCreditsTitle.setTypeface(Typeface.DEFAULT_BOLD);
            mCreditsTitle.setTextSize(20);
            mCreditsTitle.setGravity(Gravity.CENTER_HORIZONTAL);

            AlertDialog mCreditsDialog = new AlertDialog.Builder(this)
                    .setTitle("Are you sure to Delete All History")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteAll(db.TABLE_NAME);
                            HistoryActivity.this.recreate();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setView(mCreditsTitle)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }


    public class ContactImageAdapter extends ArrayAdapter<Website1> {
        Context context;
        int layoutResourceId;
        // BcardImage data[] = null;
        ArrayList<Website1> data = new ArrayList<>();

        public ContactImageAdapter(Context context, int layoutResourceId, ArrayList<Website1> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ImageHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new ImageHolder();
                holder.history_view = row.findViewById(R.id.history_view);
                holder.txtTitle = row.findViewById(R.id.title);
                holder.txtUrl = row.findViewById(R.id.url);
                holder.time = row.findViewById(R.id.record_item_time);
                holder.moreIcon=row.findViewById(R.id.deleteAction);
                row.setTag(holder);
            } else {
                holder = (ImageHolder) row.getTag();
            }

            Website1 picture = data.get(position);

            holder.txtTitle.setText(picture._title);
            holder.txtUrl.setText(picture._url);



            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());
            holder.time.setText(sdf.format(picture._date));

          holder.moreIcon.setOnClickListener(new View.OnClickListener() {
                //onClick method called when the view is clicked
                @Override
                public void onClick(View view) {
                    db.deleteURL(picture._url, db.TABLE_NAME);
                    adapter.notifyDataSetChanged();
                    HistoryActivity.this.recreate();
                }
            });
            //convert byte to bitmap take from contact class



            return row;

        }

        class ImageHolder {
            RelativeLayout history_view;
            ImageView imgIcon;
            TextView txtTitle;
            TextView txtUrl;
            TextView time;
            ImageView moreIcon;
        }
    }

}