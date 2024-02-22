package com.example.lab2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter {
    private ArrayList<Contact> data;
    private LayoutInflater inflater;
    private Activity context;
    public void setData(ArrayList<Contact> data) {this.data = data;}
    public Adapter(ArrayList<Contact> data, Activity activity){
        this.data = data;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null)
            v = inflater.inflate(R.layout.infor_layout, null);

        TextView txtName = v.findViewById(R.id.name);
        txtName.setText(data.get(position).getFullName());

        TextView txtPhone = v.findViewById(R.id.phone);
        txtPhone.setText(data.get(position).getPhoneNumber());

        CheckBox cb = v.findViewById(R.id.checkbox);
        cb.setChecked(data.get(position).isStatus());

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setStatus(isChecked);
            }
        });

        return v;
    }

    public List<Contact> getCheckedItems() {
        List<Contact> checkedItems = new ArrayList<>();
        for (Contact contact : data) {
            if (contact.isStatus()) {
                checkedItems.add(contact);
            }
        }
        return checkedItems;
    }

    public void addContact(Contact contact) {
        data.add(contact);
    }

}