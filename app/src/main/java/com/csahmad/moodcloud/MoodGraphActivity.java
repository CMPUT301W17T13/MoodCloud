package com.csahmad.moodcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoodGraphActivity extends AppCompatActivity {

    private int maxHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_graph);

        Intent intent = this.getIntent();
        long angryCount = intent.getLongExtra("ANGRY_COUNT", -1l);
        long confusedCount = intent.getLongExtra("CONFUSED_COUNT", -1l);
        long disgustedCount = intent.getLongExtra("DISGUSTED_COUNT", -1l);
        long scaredCount = intent.getLongExtra("SCARED_COUNT", -1l);
        long happyCount = intent.getLongExtra("HAPPY_COUNT", -1l);
        long sadCount = intent.getLongExtra("SAD_COUNT", -1l);
        long ashamedCount = intent.getLongExtra("ASHAMED_COUNT", -1l);
        long surprisedCount = intent.getLongExtra("SURPRISED_COUNT", -1l);
        double totalCount = angryCount + confusedCount + disgustedCount + scaredCount + happyCount +
                sadCount + ashamedCount + surprisedCount;

        TextView angryCountView = (TextView) this.findViewById(R.id.angryCount);
        TextView confusedCountView = (TextView) this.findViewById(R.id.confusedCount);
        TextView disgustedCountView = (TextView) this.findViewById(R.id.disgustedCount);
        TextView scaredCountView = (TextView) this.findViewById(R.id.scaredCount);
        TextView happyCountView = (TextView) this.findViewById(R.id.happyCount);
        TextView sadCountView = (TextView) this.findViewById(R.id.sadCount);
        TextView ashamedCountView = (TextView) this.findViewById(R.id.ashamedCount);
        TextView surprisedCountView = (TextView) this.findViewById(R.id.surprisedCount);

        angryCountView.setText(Long.toString(angryCount));
        confusedCountView.setText(Long.toString(confusedCount));
        disgustedCountView.setText(Long.toString(disgustedCount));
        scaredCountView.setText(Long.toString(scaredCount));
        happyCountView.setText(Long.toString(happyCount));
        sadCountView.setText(Long.toString(sadCount));
        ashamedCountView.setText(Long.toString(ashamedCount));
        surprisedCountView.setText(Long.toString(surprisedCount));

        LinearLayout angryBar = (LinearLayout) this.findViewById(R.id.angryBar);
        LinearLayout confusedBar = (LinearLayout) this.findViewById(R.id.confusedBar);
        LinearLayout disgustedBar = (LinearLayout) this.findViewById(R.id.disgustedBar);
        LinearLayout scaredBar = (LinearLayout) this.findViewById(R.id.scaredBar);
        LinearLayout happyBar = (LinearLayout) this.findViewById(R.id.happyBar);
        LinearLayout sadBar = (LinearLayout) this.findViewById(R.id.sadBar);
        LinearLayout ashamedBar = (LinearLayout) this.findViewById(R.id.ashamedBar);
        LinearLayout surprisedBar = (LinearLayout) this.findViewById(R.id.surprisedBar);

        LinearLayout parent = (LinearLayout) angryBar.getParent();
        this.maxHeight = parent.getLayoutParams().height;

        this.setBarHeight(angryBar, (double) angryCount / totalCount);
        this.setBarHeight(confusedBar, (double) confusedCount / totalCount);
        this.setBarHeight(disgustedBar, (double) disgustedCount / totalCount);
        this.setBarHeight(scaredBar, (double) scaredCount / totalCount);
        this.setBarHeight(happyBar, (double) happyCount / totalCount);
        this.setBarHeight(sadBar, (double) sadCount / totalCount);
        this.setBarHeight(ashamedBar, (double) ashamedCount / totalCount);
        this.setBarHeight(surprisedBar, (double) surprisedCount / totalCount);
    }

    private void setBarHeight(LinearLayout bar, double percent) {

        bar.getLayoutParams().height = (int) (percent * this.maxHeight);
    }
}
