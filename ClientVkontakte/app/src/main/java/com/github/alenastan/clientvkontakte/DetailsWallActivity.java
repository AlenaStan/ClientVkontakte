package com.github.alenastan.clientvkontakte;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.alenastan.clientvkontakte.bo.NoteGsonModel;
import com.github.alenastan.clientvkontakte.bo.Wall;
import com.github.alenastan.clientvkontakte.helper.DataManager;
import com.github.alenastan.clientvkontakte.image.ImageLoader;

import java.util.List;

/**
 * Created by lena on 26.01.2015.
 */
public class DetailsWallActivity extends ActionBarActivity implements DataManager.Callback<List<Wall>> {
    private ImageLoader mImageLoader;
    NoteGsonModel mNoteGsonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_details);
        mImageLoader = ImageLoader.get(DetailsWallActivity.this);
        NoteGsonModel mNoteGsonModel = (NoteGsonModel) getIntent().getSerializableExtra("item");
        ((TextView)findViewById(R.id.text)).setText(mNoteGsonModel.getTitle());
        ((TextView)findViewById(R.id.date)).setText(mNoteGsonModel.getContent());
        ImageView imageView = (ImageView)findViewById(R.id.content);
        mImageLoader.loadAndDisplay(mNoteGsonModel.getUrl(), imageView);

    }
    @Override
    public void onDataLoadStart() {
    }
    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
    @Override
    public void onDone(List<Wall> data) {

    }


}

