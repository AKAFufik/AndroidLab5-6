package com.example.myecommerce;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ViewPager pager = null;
    private ViewAdapter pagerAdapter = null;
    public DataBase DBH;

    public class MyThread extends Thread {
        public void run(final int i, final SQLiteDatabase database) {
            int r = new Random().nextInt((5 - 3) + 1) + 3;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Cursor cursor = database.query(DataBase.TABLE_CONTACTS, null, null, null, null, null, null);

                    if (cursor.moveToPosition(i)) {
                        int idIndex = cursor.getColumnIndex(DBH.KEY_ID);
                        int nameIndex1 = cursor.getColumnIndex(DBH.COST);
                        int nameIndex2 = cursor.getColumnIndex(DBH.QUANTITY);
                        int nameIndex3 = cursor.getColumnIndex(DBH.NAME);

                        if (cursor.getInt(nameIndex2) != 0) {
                            LayoutInflater inflater = getLayoutInflater();
                            FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.fragment_page, null);
                            TextView tvName = (TextView) v0.findViewById(R.id.displayText);
                            TextView tvName1 = (TextView) v0.findViewById(R.id.displayid);
                            Button button = (Button) v0.findViewById(R.id.button2);
                            tvName.setText(cursor.getString(nameIndex3) + "\n" + cursor.getString(nameIndex1) + "\n" + cursor.getString(nameIndex2));
                            button.setText(cursor.getString(idIndex));
                            button.setOnClickListener(oclBtn1);
                            pagerAdapter.addView(v0, i);
                            pagerAdapter.notifyDataSetChanged();
                        }

                    } else
                        Log.d("mLog", "0 quanity");
                    cursor.close();
                }
            }, 1000 * r);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new ViewAdapter();
        pager.setAdapter(pagerAdapter);
        DBH = new DataBase(this);
        final SQLiteDatabase database = DBH.getWritableDatabase();
        //DBH.onUpgrade(database,3,4);


        Cursor cursor = database.query(DataBase.TABLE_CONTACTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBH.KEY_ID);
            int nameIndex1 = cursor.getColumnIndex(DBH.COST);
            int nameIndex2 = cursor.getColumnIndex(DBH.QUANTITY);
            int nameIndex3 = cursor.getColumnIndex(DBH.NAME);

            int i = 0;
            do {

                if (cursor.getInt(nameIndex2) != 0) {
                    LayoutInflater inflater = getLayoutInflater();
                    FrameLayout v0 = (FrameLayout) inflater.inflate(R.layout.fragment_page, null);
                    TextView tvName = (TextView) v0.findViewById(R.id.displayText);
                    TextView tvName1 = (TextView) v0.findViewById(R.id.displayid);
                    Button button = (Button) v0.findViewById(R.id.button2);
                    tvName.setText(cursor.getString(nameIndex3) + "\n" + cursor.getString(nameIndex1) + "\n" + cursor.getString(nameIndex2));
                    button.setText(cursor.getString(idIndex));
                    button.setOnClickListener(oclBtn1);
                    pagerAdapter.addView(v0, i);
                    pagerAdapter.notifyDataSetChanged();
                }
                i += 1;
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 quanity");
        cursor.close();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, BackFront.class);
                startActivity(intent);
                this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener oclBtn1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d("mLog1", Integer.toString(pager.getCurrentItem()));
            final SQLiteDatabase database = DBH.getWritableDatabase();
            final ContentValues contentValues = new ContentValues();
            Log.d("mLog2", Integer.toString(pager.getCurrentItem()));
            Button tvName1 = (Button) v.findViewById(R.id.button2);
            TextView tvName2 = (TextView) findViewById(R.id.displayText);
            Cursor cursor = database.query(DataBase.TABLE_CONTACTS, null, null, null, null, null, null);
            Log.d("mLog3", (tvName1.getText().toString()));
            if (cursor.moveToPosition((Integer.parseInt(tvName1.getText().toString())) - 1)) {
                int idIndex = cursor.getColumnIndex(DBH.KEY_ID);
                int nameIndex1 = cursor.getColumnIndex(DBH.COST);
                int nameIndex2 = cursor.getColumnIndex(DBH.QUANTITY);
                int nameIndex3 = cursor.getColumnIndex(DBH.NAME);
                Log.d("mLog4", Integer.toString(pager.getCurrentItem()));


                Log.d("mLog6", Integer.toString(pager.getCurrentItem()));
                ContentValues cv = new ContentValues();
                cv.put(DataBase.COST, (cursor.getString(nameIndex1)));
                cv.put(DataBase.QUANTITY, Integer.toString(cursor.getInt(nameIndex2) - 1));
                cv.put(DataBase.NAME, (cursor.getString(nameIndex3)));
                Log.d("mLog7", Integer.toString(pager.getCurrentItem()));
                database.update(DataBase.TABLE_CONTACTS, cv, "id = ?", new String[]{tvName1.getText().toString()});
                Log.d("mLog5", Integer.toString(pager.getCurrentItem()));
                if ((cursor.getInt(nameIndex2)) != 0) {
                    MyThread myThread = new MyThread();
                    myThread.run(pager.getCurrentItem(), database);
                }


                pagerAdapter.removeView(pager, pager.getCurrentItem());
                pagerAdapter.notifyDataSetChanged();


            } else
                Log.d("mLog", "0 quanity");
            cursor.close();


        }
    };
}

