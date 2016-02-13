package com.surveyapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 09-02-2016.
 */
public class UserAccountListAdapter extends BaseAdapter {


    private List<UserAccountObject> userCredentialList;
    private Context context;

    public UserAccountListAdapter(Context context, List<UserAccountObject> userCredentialList) {

        this.context = context;
        this.userCredentialList = userCredentialList;
    }

    @Override
    public int getCount() {
        return userCredentialList.size();
    }

    @Override
    public Object getItem(int position) {
        return userCredentialList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user_account_list,parent,false);

        TextView tile = (TextView) rootView.findViewById(R.id.userCredentialsTile);
        TextView value = (TextView) rootView.findViewById(R.id.userCredentialsValue);

        tile.setText(userCredentialList.get(position).getTitle());
        value.setText(userCredentialList.get(position).getValue());


        return rootView;
    }

    public static class UserAccountObject {
        private String title;
        private String value;

        public UserAccountObject(String title, String value) {
            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
