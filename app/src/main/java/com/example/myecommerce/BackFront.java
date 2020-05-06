package com.example.myecommerce;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class BackFront extends AppCompatActivity {
Button button;
EditText editTextID, editTextCol, editTextPrice, editTextName;
RadioButton radioButtonAdd, radioButtonRed;
TextView textView;
DataBase dataBase;
    public class MyThread extends Thread {
        public void run(final ContentValues cv, final SQLiteDatabase database,final String str) {
            int r=new Random().nextInt((5 - 3) + 1) + 3;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    cv.put(DataBase.COST,(editTextCol.getText().toString()));
                    cv.put(DataBase.QUANTITY,(editTextPrice.getText().toString()));
                    cv.put(DataBase.NAME,(editTextName.getText().toString()));
                    database.update(DataBase.TABLE_CONTACTS,cv,"id = ?",new String[] { (str)});
                    Log.d("mLog","re");
                }
            }, 1000*r);


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBase= new DataBase(this);
        final SQLiteDatabase database = dataBase.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        setContentView(R.layout.activity_back_front);
        editTextID=(EditText) findViewById(R.id.editTextID);
        editTextCol=(EditText) findViewById(R.id.editTextCol);
        editTextPrice=(EditText) findViewById(R.id.editTextPrice);
        editTextName=(EditText) findViewById(R.id.editTextName);
        button=(Button) findViewById(R.id.button);
        radioButtonAdd=(RadioButton)findViewById(R.id.radioButtonAdd);
        radioButtonRed=(RadioButton)findViewById(R.id.radioButtonRed);
        radioButtonRed.setOnClickListener(radioButtonClickListener);
        radioButtonAdd.setOnClickListener(radioButtonClickListener);
        textView=(TextView) findViewById(R.id.textView);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv=new ContentValues();
                if (radioButtonAdd.isChecked())
                {
                cv.put(DataBase.COST,(editTextCol.getText().toString()));
                cv.put(DataBase.QUANTITY,(editTextPrice.getText().toString()));
                cv.put(DataBase.NAME,(editTextName.getText().toString()));
                database.insert(DataBase.TABLE_CONTACTS,null,cv);
                    Log.d("mLog","+1");
                }
                else
                {
                    cv.put(DataBase.COST,(editTextCol.getText().toString()));
                    cv.put(DataBase.QUANTITY,(editTextPrice.getText().toString()));
                    cv.put(DataBase.NAME,(editTextName.getText().toString()));
                    MyThread myThread = new MyThread();
                    myThread.run(cv, database, editTextID.getText().toString());
                }

            }
        };
        button.setOnClickListener(oclBtnOk);
    }
    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.radioButtonAdd: editTextID.setVisibility(View.INVISIBLE);textView.setVisibility(View.INVISIBLE);
                    break;
                case R.id.radioButtonRed:editTextID.setVisibility(View.VISIBLE);textView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings2:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
