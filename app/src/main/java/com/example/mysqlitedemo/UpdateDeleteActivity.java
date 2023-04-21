package com.example.mysqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mysqlitedemo.dal.SQLiteHelper;
import com.example.mysqlitedemo.model.Item;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner sp;
    private EditText eTitle, ePrice, eDate;
    private Button btUpdate, btRemove, btBack;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        btRemove.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btBack.setOnClickListener(this);
        eDate.setOnClickListener(this);

        //lay item dc gui tu FragmentHome sang
        Intent intent = getIntent();
        item = (Item)intent.getSerializableExtra("item");

        //lay dc item roi thi cho vao cai editview,sp,..
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        int p = 0;
        for (int i = 0; i<sp.getCount(); i++){
            if(sp.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())){
                p = i;
                break;
            }
        }
        sp.setSelection(p);
    }

    private void initView() {
        sp=findViewById(R.id.spCategory);
        eTitle=findViewById(R.id.etTitle);
        ePrice=findViewById(R.id.etPrice);
        eDate=findViewById(R.id.etDate);
        btUpdate=findViewById(R.id.btUpdate);
        btRemove=findViewById(R.id.btRemove);
        btBack=findViewById(R.id.btBack);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spiner,
                getResources().getStringArray(R.array.category)));
    }

    @Override
    public void onClick(View view) {
        SQLiteHelper db = new SQLiteHelper(this);
        if(view==eDate){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date ="";
                    if(m>8){
                        date = d+"/"+(m+1)+"/"+y;
                    }else date = d+"/0"+(m+1)+"/"+y;
                    eDate.setText(date);
                }
            },year,month,day);
            dialog.show();
        }
        if (view==btBack){
            finish();
        }
        if (view==btUpdate){
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = sp.getSelectedItem().toString();
            String d = eDate.getText().toString();
            if(!t.isEmpty() && p.matches("\\d+")){
                int id = item.getId();
                Item item = new Item(id,t,c,p,d);
                db.update(item);
                finish();
            }
        }
        if (view==btRemove){
            int id = item.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thong bao xoa");
            builder.setMessage("Ban co chac muon xoa '"+item.getTitle()+"' khong?");
            builder.setIcon(R.drawable.remove_icon);
            builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.delete(id);
                    finish();
                }
            });
            builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //ko lm gi
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}