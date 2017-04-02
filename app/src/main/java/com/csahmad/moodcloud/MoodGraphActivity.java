package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }}
        );

        List<Long> counts = Arrays.asList(this.angryCount, this.confusedCount, this.disgustedCount,
                this.scaredCount, this.happyCount, this.sadCount, this.ashamedCount,
                this.surprisedCount);

        double maxCount = Collections.max(counts);

        double totalCount = this.angryCount + this.confusedCount + this.disgustedCount +
                this.scaredCount + this.happyCount + this.sadCount + this.ashamedCount +
                this.surprisedCount;

        TextView angryCountView = (TextView) this.findViewById(R.id.angryCount);
        TextView confusedCountView = (TextView) this.findViewById(R.id.confusedCount);
        TextView disgustedCountView = (TextView) this.findViewById(R.id.disgustedCount);
        TextView scaredCountView = (TextView) this.findViewById(R.id.scaredCount);
        TextView happyCountView = (TextView) this.findViewById(R.id.happyCount);
        TextView sadCountView = (TextView) this.findViewById(R.id.sadCount);
        TextView ashamedCountView = (TextView) this.findViewById(R.id.ashamedCount);
        TextView surprisedCountView = (TextView) this.findViewById(R.id.surprisedCount);

        double angryPercent = (double) this.angryCount / totalCount;
        double confusedPercent = (double) this.confusedCount / totalCount;
        double disgustedPercent = (double) this.disgustedCount / totalCount;
        double scaredPercent = (double) this.scaredCount / totalCount;
        double happyPercent = (double) this.happyCount / totalCount;
        double sadPercent = (double) this.sadCount / totalCount;
        double ashamedPercent = (double) this.ashamedCount / totalCount;
        double surprisedPercent = (double) this.surprisedCount / totalCount;

        final double angryPercentOfMax = (double) this.angryCount / maxCount;
        final double confusedPercentOfMax = (double) this.confusedCount / maxCount;
        final double disgustedPercentOfMax = (double) this.disgustedCount / maxCount;
        final double scaredPercentOfMax = (double) this.scaredCount / maxCount;
        final double happyPercentOfMax = (double) this.happyCount / maxCount;
        final double sadPercentOfMax = (double) this.sadCount / maxCount;
        final double ashamedPercentOfMax = (double) this.ashamedCount / maxCount;
        final double surprisedPercentOfMax = (double) this.surprisedCount / maxCount;

        DecimalFormat format = new DecimalFormat("#.#");

        angryCountView.setText(format.format(angryPercent * 100.0d) + "%");
        confusedCountView.setText(format.format(confusedPercent * 100.0d) + "%");
        disgustedCountView.setText(format.format(disgustedPercent * 100.0d) + "%");
        scaredCountView.setText(format.format(scaredPercent * 100.0d) + "%");
        happyCountView.setText(format.format(happyPercent * 100.0d) + "%");
        sadCountView.setText(format.format(sadPercent * 100.0d) + "%");
        ashamedCountView.setText(format.format(ashamedPercent * 100.0d) + "%");
        surprisedCountView.setText(format.format(surprisedPercent * 100.0d) + "%");

        final LinearLayout parent = (LinearLayout) this.findViewById(R.id.barParent);

        ViewTreeObserver observer = parent.getViewTreeObserver();

        final MoodGraphActivity activity = this;

        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                activity.maxHeight = parent.getHeight();

                LinearLayout angryBar = (LinearLayout) activity.findViewById(R.id.angryBar);
                LinearLayout confusedBar = (LinearLayout) activity.findViewById(R.id.confusedBar);
                LinearLayout disgustedBar = (LinearLayout) activity.findViewById(R.id.disgustedBar);
                LinearLayout scaredBar = (LinearLayout) activity.findViewById(R.id.scaredBar);
                LinearLayout happyBar = (LinearLayout) activity.findViewById(R.id.happyBar);
                LinearLayout sadBar = (LinearLayout) activity.findViewById(R.id.sadBar);
                LinearLayout ashamedBar = (LinearLayout) activity.findViewById(R.id.ashamedBar);
                LinearLayout surprisedBar = (LinearLayout) activity.findViewById(R.id.surprisedBar);

                activity.setBarHeight(angryBar, angryPercentOfMax);
                activity.setBarHeight(confusedBar, confusedPercentOfMax);
                activity.setBarHeight(disgustedBar, disgustedPercentOfMax);
                activity.setBarHeight(scaredBar, scaredPercentOfMax);
                activity.setBarHeight(happyBar, happyPercentOfMax);
                activity.setBarHeight(sadBar, sadPercentOfMax);
                activity.setBarHeight(ashamedBar, ashamedPercentOfMax);
                activity.setBarHeight(surprisedBar, surprisedPercentOfMax);

                return true;
            }
        });
    }
    
    public void onAngryClicked(View v) {

        this.showCountString("Angry", this.angryCount);
    }

    public void onConfusedClicked(View v) {

        this.showCountString("Confused", this.confusedCount);
    }

    public void onDisgustedClicked(View v) {

        this.showCountString("Disgusted", this.disgustedCount);
    }

    public void onScaredClicked(View v) {

        this.showCountString("Scared", this.scaredCount);
    }

    public void onHappyClicked(View v) {

        this.showCountString("Happy", this.happyCount);
    }

    public void onSadClicked(View v) {

        this.showCountString("Sad", this.sadCount);
    }

    public void onAshamedClicked(View v) {

        this.showCountString("Ashamed", this.ashamedCount);
    }

    public void onSurprisedClicked(View v) {

        this.showCountString("Surprised", this.surprisedCount);
    }
    
    private void showCountString(String label, long count) {

        Toast.makeText(getApplicationContext(), countString(label, count),
                Toast.LENGTH_LONG).show();
    }
    
    private static String countString(String label, long count) {
        
        return label + " - " + Long.toString(count);
    }

    private void setBarHeight(LinearLayout bar, double percent) {

        if (percent < minHeightPercent) percent = minHeightPercent;
        LayoutParams params = bar.getLayoutParams();
        params.height = (int) (percent * (double) this.maxHeight);
        bar.setLayoutParams(params);
    }
}
