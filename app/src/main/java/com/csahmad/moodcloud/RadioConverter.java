package com.csahmad.moodcloud;

public class RadioConverter {
    public static int getMoodButtonId(int mood){

        switch (mood){
            case Mood.ANGRY:
                return R.id.angry_selected;
            case Mood.CONFUSED:
                return R.id.confused_selected;
            case Mood.DISGUSTED:
                return R.id.disgusted_selected;
            case Mood.ASHAMED:
                return R.id.ashamed_selected;
            case Mood.HAPPY:
                return R.id.happy_selected;
            case Mood.SAD:
                return R.id.sad_selected;
            case Mood.SCARED:
                return R.id.scared_selected;
            case Mood.SURPRISED:
                return R.id.surprised_selected;
            default:
                throw new RuntimeException();
        }
    }

    public static int getContextButtonId(int context){
        switch (context){
            case SocialContext.ALONE:
                return R.id.alone_selected;
            case SocialContext.WITH_CROWD:
                return R.id.crowd_selected;
            case SocialContext.WITH_GROUP:
                return R.id.group_selected;
            default:
                throw new RuntimeException();
        }
    }

}
