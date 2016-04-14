package com.surveyapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.surveyapp.Activities.ShowResultsActivity;
import com.surveyapp.CustomObjects.Survey;
import com.surveyapp.R;
import com.surveyapp.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Rahul Yadav on 17-03-2016.
 */
public class MySurveyAdapter  extends RecyclerView.Adapter<MySurveyAdapter.MySurveyViewHolder>{


    private List<Survey> mySurveyList;

    private Context context;
    private MaterialDialog dialog;
    public MySurveyAdapter(Context context , List<Survey> mySurveyList) {
        this.mySurveyList = mySurveyList;
        this.context = context;



    }

    @Override
    public int getItemCount() {
        return mySurveyList.size();
    }

    @Override
    public void onBindViewHolder(MySurveyViewHolder holder, final int position) {

        holder.surveyTitle.setText(mySurveyList.get(position).getSurveyTitle());
        holder.dateCreated.setText(mySurveyList.get(position).getDateCreated());

       holder.mainContainer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dialog = new  MaterialDialog.Builder(context)
                       .customView(getDialogView(position),false)
                       .build();

               dialog.show();
           }
       });


    }

    @Override
    public MySurveyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MySurveyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_survey_row,parent,false));
    }

    protected class MySurveyViewHolder extends RecyclerView.ViewHolder{

        private TextView surveyTitle;
        private TextView dateCreated;
        private LinearLayout mainContainer;

        public MySurveyViewHolder(View itemView) {
            super(itemView);

            surveyTitle = (TextView) itemView.findViewById(R.id.mySurveyTitle);
            dateCreated = (TextView) itemView.findViewById(R.id.mSurveyDateCreated);
            mainContainer = (LinearLayout) itemView.findViewById(R.id.mainContainer);

        }
    }

    public void changeList(List<Survey> mySurveyList ){
        this.mySurveyList = mySurveyList;
        notifyDataSetChanged();
    }

    public void addItems(List<Survey> newList){
        this.mySurveyList.addAll(newList);
        notifyDataSetChanged();
    }

    public void startShareIntentProcess(int position) {

            File imgFile = new  File( Environment.getExternalStorageDirectory().toString() + "/" + mySurveyList.get(position).getSurveyID() + ".jpg");

            if(imgFile.exists()){
                Log.e("image exists","1");

                Uri uri = Uri.parse(imgFile.toString());



                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://participateme.com/r/" + mySurveyList.get(position).getSurveyID()+"      (Because Your Opinion Matters)");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/*");
                context.startActivity(Intent.createChooser(shareIntent, "Share My Survey Via : "));



            }

        else{

                Log.e("image exists", "0");
            Uri path = Uri.parse("android.resource://com.surveyapp/" + R.drawable.share_image);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://participateme.com/r/" + mySurveyList.get(position).getSurveyID()+"      (Because Your Opinion Matters)");
            shareIntent.putExtra(Intent.EXTRA_STREAM, path);
            shareIntent.setType("image/*");

            context.startActivity(Intent.createChooser(shareIntent, "Share My Survey Via : "));}

    }




    private View getDialogView(final int position){

        View dialogView = View.inflate(context, R.layout.custom_dialog_survey_options, new LinearLayout(context));

        Button showSurveyDetails = (Button) dialogView.findViewById(R.id.dialogShowSurveyResults);
        final Button shareSurvey = (Button) dialogView.findViewById(R.id.dialogShareSurvey);
        Button cancelDialog = (Button) dialogView.findViewById(R.id.dialogCancelButton);

        showSurveyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startShowSurveyFragment(position);
                hideDialog();

            }
        });

        shareSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mySurveyList.get(position).getDateCreated().matches(context.getResources().getString(R.string.survey_taken_by_you_hint))){

                    hideDialog();
                    Utils.toastL(context,"You can't Share This Survey");
                    return;
                }


                startShareIntentProcess(position);
                hideDialog();


            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDialog();
            }
        });
        return dialogView;
    }

    public void showDialog(){
        if (dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    public void hideDialog(){
        if (dialog!=null && dialog.isShowing()){
            dialog.cancel();
        }
    }

    private void startShowSurveyFragment(int position){

        Bundle bundle = new Bundle();
        bundle.putString("surveyID",mySurveyList.get(position).getSurveyID());
        bundle.putString("surveyTitle",mySurveyList.get(position).getSurveyTitle());

        Intent showSurveyResult = new Intent(context, ShowResultsActivity.class);
        showSurveyResult.putExtras(bundle);
        context.startActivity(showSurveyResult);

    }
}
