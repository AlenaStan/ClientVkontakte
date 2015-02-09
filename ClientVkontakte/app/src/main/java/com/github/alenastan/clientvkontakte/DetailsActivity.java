package com.github.alenastan.clientvkontakte;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.alenastan.clientvkontakte.bo.Friend;
import com.github.alenastan.clientvkontakte.bo.NoteGsonModel;
import com.github.alenastan.clientvkontakte.helper.DataManager;
import com.github.alenastan.clientvkontakte.image.ImageLoader;

import java.util.List;

/**
 * Created by lena on 25.01.2015.
 */
public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<List<Friend>> {

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mImageLoader = ImageLoader.get(DetailsActivity.this);
        NoteGsonModel noteGsonModel = (NoteGsonModel) getIntent().getSerializableExtra("item");
        ImageView imageView = (ImageView)findViewById(R.id.photo);
        mImageLoader.loadAndDisplay(noteGsonModel.getUrl(), imageView);

    }
    @Override
    public void onDataLoadStart() {

    }
    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
    @Override
    public void onDone(List<Friend> data) {

    }


}