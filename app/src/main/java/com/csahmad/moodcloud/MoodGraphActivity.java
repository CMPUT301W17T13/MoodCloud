package com.csahmad.moodcloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoodGraphActivity extends AppCompatActivity {

    private static final double minHeightPercent = 0.05d;
    private int maxHeight;

    private long angryCount;
    private long confusedCount;
    private long disgustedCount;
    private long scaredCount;
    private long happyCount;
    private long sadCount;
    private long ashamedCount;
    private long surprisedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_graph);

        Intent intent = this.getIntent();
        this.angryCount = intent.getLongExtra("ANGRY_COUNT", -1l);
        this.confusedCount = intent.getLongExtra("CONFUSED_COUNT", -1l);
        this.disgustedCount = intent.getLongExtra("DISGUSTED_COUNT", -1l);
        this.scaredCount = intent.getLongExtra("SCARED_COUNT", -1l);
        this.happyCount = intent.getLongExtra("HAPPY_COUNT", -1l);
        this.sadCount = intent.getLongExtra("SAD_COUNT", -1l);
        this.ashamedCount = intent.getLongExtra("ASHAMED_COUNT", -1l);
        this.surprisedCount = intent.getLongExtra("SURPRISED_COUNT", -1l);

        double totalCount = this.angryCount + this.confusedCount + this.disgustedCount +
                this.scaredCount + this.happyCount + this.sadCount + this.ashamedCount +
                this.surprisedCount;

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

        TextView angryCountView = (TextView) this.findViewById(R.id.angryCount);
        TextView confusedCountView = (TextView) this.findViewById(R.id.confusedCount);
        TextView disgustedCountView = (TextView) this.findViewById(R.id.disgustedCount);
        TextView scaredCountView = (TextView) this.findViewById(R.id.scaredCount);
        TextView happyCountView = (TextView) this.findViewById(R.id.happyCount);
        TextView sadCountView = (TextView) this.findViewById(R.id.sadCount);
        TextView ashamedCountView = (TextView) this.findViewById(R.id.ashamedCount);
        TextView surprisedCountView = (TextView) this.findViewById(R.id.surprisedCount);

        double angryPercent = (double) this.angryCount / totalCount;
        double confusedPercent = (double) this.angryCount / totalCount;
        double disgustedPercent = (double) this.angryCount / totalCount;
        double scaredPercent = (double) this.angryCount / totalCount;
        double happyPercent = (double) this.angryCount / totalCount;
        double sadPercent = (double) this.angryCount / totalCount;
        double ashamedPercent = (double) this.angryCount / totalCount;
        double surprisedPercent = (double) this.angryCount / totalCount;

        angryCountView.setText(Double.toString(angryPercent * 100.0d) + "%");
        confusedCountView.setText(Double.toString(confusedPercent * 100.0d) + "%");
        disgustedCountView.setText(Double.toString(disgustedPercent * 100.0d) + "%");
        scaredCountView.setText(Double.toString(scaredPercent * 100.0d) + "%");
        happyCountView.setText(Double.toString(happyPercent * 100.0d) + "%");
        sadCountView.setText(Double.toString(sadPercent * 100.0d) + "%");
        ashamedCountView.setText(Double.toString(ashamedPercent * 100.0d) + "%");
        surprisedCountView.setText(Double.toString(surprisedPercent * 100.0d) + "%");

        this.setBarHeight(angryBar, angryPercent);
        this.setBarHeight(confusedBar, confusedPercent);
        this.setBarHeight(disgustedBar, disgustedPercent);
        this.setBarHeight(scaredBar, scaredPercent);
        this.setBarHeight(happyBar, happyPercent);
        this.setBarHeight(sadBar, sadPercent);
        this.setBarHeight(ashamedBar, ashamedPercent);
        this.setBarHeight(surprisedBar, surprisedPercent);
    }

    private void setBarHeight(LinearLayout bar, double percent) {

        if (percent < minHeightPercent) percent = minHeightPercent;
        bar.getLayoutParams().height = (int) (percent * this.maxHeight);
    }
}
