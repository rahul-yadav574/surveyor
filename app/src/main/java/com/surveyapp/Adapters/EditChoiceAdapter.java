package com.surveyapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.CustomObjects.Question;
import com.surveyapp.R;

import java.util.List;

/**
 * Created by Rahul Yadav on 07-03-2016.
 */
public class EditChoiceAdapter extends RecyclerView.Adapter<EditChoiceAdapter.EditChoiceViewHolder> {

    private List<String> choiceList;
    private Context context;

    public EditChoiceAdapter() {
    }

    public EditChoiceAdapter(Context context,List<String> choiceList) {
        this.choiceList=choiceList;
        this.context = context;
    }

    @Override
    public EditChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditChoiceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_edit_choice_row,parent,false));
    }

    @Override
    public int getItemCount() {
        return choiceList.size();
    }

    @Override
    public void onBindViewHolder(EditChoiceViewHolder holder, final int position) {
        holder.choiceStatement.setText(choiceList.get(position));

        holder.editChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEditingChoiceProcess(position);
            }
        });
    }

    protected class EditChoiceViewHolder extends RecyclerView.ViewHolder{

        private TextView choiceStatement;
        private ImageButton editChoice;

        public EditChoiceViewHolder(View itemView) {
            super(itemView);

            choiceStatement = (TextView) itemView.findViewById(R.id.choiceEditStatement);
            editChoice = (ImageButton) itemView.findViewById(R.id.choiceEditButton);
        }
    }

    public List<String> getChoiceList(){
        return this.choiceList;
    }

    public void addChoiceToList(String newChoice){
        choiceList.add(newChoice);
        notifyDataSetChanged();
    }

    private void startEditingChoiceProcess(final int position){
        new MaterialDialog.Builder(context)
                .content("Enter Updated Choice")
                .input(null, null, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        choiceList.remove(position);
                        choiceList.add(position,input.toString());
                        notifyDataSetChanged();
                        dialog.cancel();
                    }
                })
                .show();
    }
}
