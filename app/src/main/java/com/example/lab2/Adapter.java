package com.example.lab2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends BaseAdapter implements Filterable {
    private ArrayList<Contact> data;

    private ArrayList<Contact> databackup;

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

        ImageView avatar = v.findViewById(R.id.imageView);
        if(avatar != null) {
            Bitmap avt = data.get(position).getAvartar();
            avatar.setImageBitmap(avt);
        }



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

    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                //Backup dữ liệu: lưu tạm data vào databackup
                if(databackup==null)
                    databackup = new ArrayList<>(data);
                //Nếu chuỗi để filter là rỗng thì khôi phục dữ liệu
                if(charSequence==null || charSequence.length()==0)
                {
                    fr.count = databackup.size();
                    fr.values = databackup;
                }
                //Còn nếu không rỗng thì thực hiện filter
                else{
                    ArrayList<Contact> newdata = new ArrayList<>();
                    for(Contact c:databackup)
                        if(c.getFullName().toLowerCase().contains(
                                charSequence.toString().toLowerCase()))
                            newdata.add(c);
                    fr.count=newdata.size();
                    fr.values=newdata;
                }
                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = new ArrayList<Contact>();
                ArrayList<Contact> tmp =
                        (ArrayList<Contact>)filterResults.values;
                for(Contact c: tmp)
                    data.add(c);
                notifyDataSetChanged();
            }
        };
        return f;
    }
}