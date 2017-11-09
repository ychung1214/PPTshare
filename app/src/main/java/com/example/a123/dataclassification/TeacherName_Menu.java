package com.example.a123.dataclassification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherName_Menu extends AppCompatActivity {
    private ListView listView;
    private UnitAdapter adapter;
    ArrayList<String> data;
    Bundle bundle;
    boolean isTeacher = false;
    DisplayMetrics metrics = new DisplayMetrics();
    HashMap<String, DataSnapshot> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_choose);

        getSupportActionBar().hide(); //隱藏標題
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        map = new HashMap<String, DataSnapshot>();
        listView = (ListView) this.findViewById(R.id.list);
        SharedPreferences Position = getSharedPreferences("Position", MODE_PRIVATE);
        if (Position.equals(isTeacher)) {
            isTeacher = true;
        }

        data = new ArrayList<>();
        getdataFromFirebase();
    }

    private void getdataFromFirebase() {
        Intent intent = this.getIntent();
        DatabaseReference reference_contacts = FirebaseDatabase.getInstance().getReference("Teach").child("Share");
        reference_contacts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot temp : dataSnapshot.getChildren()) {
                    map.put(temp.getKey().toString(), temp);
                    Log.e("DEBUG", "Line 62 map " + temp + "");
//
                }

                setListView(new ArrayList<String>(map.keySet()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    boolean first = true;

    private void setListView(ArrayList data) {
        adapter = new UnitAdapter(data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (first) {
                    first = false;
                } else {
                    return;//應跳轉回menuChoose
                }
                TextView textView = (TextView) view.findViewById(R.id.textView2);
                ArrayList<String> data = new ArrayList<String>();
                DataSnapshot result = map.get(textView.getText().toString());
                Log.e("DEBUG", "ON item result : " + result);
                for (DataSnapshot temp : result.getChildren()) {
                    data.add(temp.getKey().toString());
                    //String UnitName = getString(getResources().getIdentifier(temp.getKey(),"string",getPackageName()));
                    Log.e("DEBUG", "UnitName  " + temp);
//                    for (DataSnapshot temp2 : temp.getChildren()) {
//                        data.add(UnitName + ":" + temp2.getValue());
//                    }
                }
                setListView(data);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String selected = listView.getItemAtPosition(position).toString();
                //Intent intent =   new Intent(getApplicationContext(), TextView.class);
                Intent intent = new Intent();
                intent.setClass(TeacherName_Menu.this,TeachMaterial.class);
                intent.putExtra("selected",selected);
                startActivity(intent);
            }
        });
    }

    private class UnitAdapter extends BaseAdapter {
        ArrayList<String> list;

        private UnitAdapter(ArrayList<String> data) {
            this.list = data;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            View view1 = LayoutInflater.from(TeacherName_Menu.this).inflate(R.layout.unit_choose_item, null);
            TextView textView = (TextView) view1.findViewById(R.id.textView2);
            textView.setText(list.get(i));
            textView.setTextSize(metrics.widthPixels / 60);
            return view1;
        }


    }

}