package com.surveyapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 05-03-2016.
 */
public class ChoicesAdapter extends RecyclerView.Adapter<ChoicesAdapter.ChoicesViewHolder> {

    private List<String> choicesList;


    public ChoicesAdapter(List<String> choicesList) {
        this.choicesList = choicesList;
    }

    @Override
    public void onBindViewHolder(ChoicesViewHolder holder, int position) {

        holder.choiceStatement.setText(choicesList.get(position));

    }

    public void addItemToList(String item){
        this.choicesList.add(item);
        this.notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return choicesList.size();
    }

    public List<String> getChoicesList(){
        return this.choicesList;
    }

    @Override
    public ChoicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChoicesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_choices_show,parent,false));
    }

    protected class ChoicesViewHolder extends RecyclerView.ViewHolder{

        private TextView choiceStatement;

        public ChoicesViewHolder(View itemView) {
            super(itemView);

            choiceStatement = (TextView) itemView.findViewById(R.id.choiceStatement);
        }
    }
}
