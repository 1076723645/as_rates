package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Activity_Record extends AppCompatActivity {

    private EditText eSearch;
    private ImageView ivDeleteText;
    private Database database;
    private ListView listView;
    private TextView norecord;
    private Handler myhandler = new Handler();
    private List<Record> recordList = new ArrayList<Record>();
    private List<Record> selectList = new ArrayList<Record>();
    private RecordAdapter adapter;
    public static final String tablename = "record";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__record);
        listView = (ListView)findViewById(R.id.record_list);
        eSearch = (EditText)findViewById(R.id.etSearch);
        ivDeleteText = (ImageView)findViewById(R.id.ivDeleteText);
        setTitle("历史记录");
        database = new Database(this,"RATES.db",null,1);
        database.getWritableDatabase();
        initActionBar();
        initRecord();
        set_eSearch_TextChanged();//文字改变监听
        set_ivDeleteText_OnClick();//设置叉叉的监听器
        adapter = new RecordAdapter(Activity_Record.this,R.layout.currencie_item,selectList);
        listView.setAdapter(adapter);
    }


    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = eSearch.getText().toString();

            selectList.clear();//先要清空，不然会叠加

            getData(data);//获取更新数据

            adapter.notifyDataSetChanged();//更新

        }
    };

    /**
     * 获得根据搜索框的数据data来从元数据筛选，筛选出来的数据放入mDataSubs里
     */

    private void getData(String data)
    {
        int length = recordList.size();
        for(int i = 0; i < length; ++i){
            if(recordList.get(i).getFor().contains(data) || recordList.get(i).getForCode().contains(data)
                    ||recordList.get(i).getHom().contains(data)||recordList.get(i).getHomCode().contains(data)){

               selectList.add(recordList.get(i));
            }
        }
    }


    private void initRecord(){
        Cursor cursor = database.query(tablename);
        if(cursor.getCount()!=0) {
            norecord = (TextView)findViewById(R.id.norecord);
            norecord.setVisibility(GONE);
            if (cursor.moveToFirst()) {
                do {
                    String a1 = cursor.getString(cursor.getColumnIndex("start"));
                    String a2 = cursor.getString(cursor.getColumnIndex("start_mHom"));
                    String a3 = cursor.getString(cursor.getColumnIndex("final"));
                    String a4 = cursor.getString(cursor.getColumnIndex("final_mFor"));
                    Record item = new Record(a1, a2,a3,a4);
                    recordList.add(item);
                } while (cursor.moveToNext());
            }
        }
        else {
            eSearch.setFocusable(false);
        }
        selectList.addAll(recordList);
    }

    public  class Record{
        private String Hom;
        private String HomCode;
        private String For;
        private String ForCode;

        public String getHom() {
            return Hom;
        }

        public void setHom(String hom) {
            Hom = hom;
        }

        public String getHomCode() {
            return HomCode;
        }

        public void setHomCode(String homCode) {
            HomCode = homCode;
        }

        public String getFor() {
            return For;
        }

        public void setFor(String aFor) {
            For = aFor;
        }

        public String getForCode() {
            return ForCode;
        }

        public void setForCode(String forCode) {
            ForCode = forCode;
        }

        public Record(String Hom, String HomCode, String For, String Forcode){
            this.Hom = Hom;
            this.For = For;
            this.HomCode = HomCode;
            this.ForCode = Forcode;
        }
    }

    public class RecordAdapter extends ArrayAdapter<Record> {
        private int resourceId;

        public RecordAdapter(Context context, int textViewResourceId, List<Record> objects) {
            super(context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Record record = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.currencie_item, null);
            TextView a1 = (TextView) view.findViewById(R.id.a1);
            TextView a2 = (TextView) view.findViewById(R.id.a2);
            TextView a3 = (TextView) view.findViewById(R.id.a3);
            TextView a4 = (TextView) view.findViewById(R.id.a4);
            a1.setText(record.getHom());
            a2.setText(record.getHomCode());
            a3.setText(record.getFor());
            a4.setText(record.getForCode());
            return view;
        }
    }

    private void set_ivDeleteText_OnClick() {
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                eSearch.setText("");
                selectList.clear();
                selectList.addAll(recordList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void set_eSearch_TextChanged() {

        eSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                if(s.length() == 0){
                    ivDeleteText.setVisibility(GONE);//当文本框为空时，则叉叉消失
                }
                else {
                    ivDeleteText.setVisibility(VISIBLE);//当文本框不为空时，出现叉叉
                    myhandler.post(eChanged);
                }
            }
        });

    }

    private void clean(){
        database.del(tablename);
        recordList.clear();
        selectList.clear();
        norecord.setVisibility(VISIBLE);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // set action bar title
        actionBar.setDisplayUseLogoEnabled(false);
        // set background color
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFF2C477F));
        // remove the shadow line
        // show back navigation icon
        actionBar.setDisplayHomeAsUpEnabled(true);
        // show action bar
        actionBar.show();
    }//初始化actionbar

    public boolean onCreateOptionsMenu(Menu menu) {
        //新建的xml文件
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.mnu_delete:
                clean();
                break;
            case R.id.mnu_fenx:
                Intent intent = new Intent(Activity_Record.this,Activity_share.class);
                startActivity(intent);
                break;
            case R.id.mnu_exit:
                finish();
                break;
            case android.R.id.home:// 点击返回图标事件
                finish();
                break;
        }
        return true;
        // return super.onPrepareOptionsMenu(item);
    }
}
