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
    NoteGsonModel mNoteGsonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mImageLoader = ImageLoader.get(DetailsActivity.this);
        NoteGsonModel mNoteGsonModel = (NoteGsonModel) getIntent().getSerializableExtra("item");
        ((TextView)findViewById(android.R.id.text1)).setText(mNoteGsonModel.getTitle());
        ((TextView)findViewById(android.R.id.text2)).setText(mNoteGsonModel.getContent());
        ImageView imageView = (ImageView)findViewById(android.R.id.icon);
        mImageLoader.loadAndDisplay(mNoteGsonModel.getUrl(), imageView);

    }
    @Override
    public void onDataLoadStart() {
//        if (!mSwipeRefreshLayout.isRefreshing()) {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//        mEmpty.setVisibility(View.GONE);
    }
    @Override
    public void onError(Exception e) {
        e.printStackTrace();
//        mProgressBar.setVisibility(View.GONE);
//        mEmpty.setVisibility(View.GONE);
//        //TextView errorView = (TextView) findViewById(R.id.error);
//        mError.setVisibility(View.VISIBLE);
//        mError.setText(mError.getText() + "\n" + e.getMessage());
    }
    @Override
    public void onDone(List<Friend> data) {
//        if (mSwipeRefreshLayout.isRefreshing()) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
//        mProgressBar.setVisibility(View.GONE);
//        if (data == null || data.isEmpty()) {
//            mEmpty.setVisibility(View.VISIBLE);
    }


}