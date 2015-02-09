package com.github.alenastan.clientvkontakte.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.alenastan.clientvkontakte.Api;
import com.github.alenastan.clientvkontakte.DetailsNewsActivity;
import com.github.alenastan.clientvkontakte.MainActivity;
import com.github.alenastan.clientvkontakte.R;
import com.github.alenastan.clientvkontakte.auth.VkOAuthHelper;
import com.github.alenastan.clientvkontakte.bo.News;
import com.github.alenastan.clientvkontakte.bo.NoteGsonModel;
import com.github.alenastan.clientvkontakte.helper.DataManager;
import com.github.alenastan.clientvkontakte.image.ImageLoader;
import com.github.alenastan.clientvkontakte.processing.NewsArrayProcessor;
import com.github.alenastan.clientvkontakte.source.HttpDataSource;
import com.github.alenastan.clientvkontakte.source.VkDataSource;

import java.util.List;

/**
 * Created by lena on 25.01.2015.
 */
public class FragmentNews extends Fragment implements DataManager.Callback<List<News>>{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayAdapter mAdapter;
    private NewsArrayProcessor mNewsArrayProcessor;
    private AbsListView mListView;
    private TextView mError;
    private TextView mEmpty;
    private ProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    private static final int KEY = 3;


    public static FragmentNews newInstance() {
        FragmentNews fragment = new FragmentNews();
        return fragment;
    }

    public FragmentNews() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container,false);
        mImageLoader = ImageLoader.get(getActivity().getApplicationContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        mListView = (AbsListView)root.findViewById(android.R.id.list);
        mProgressBar =(ProgressBar) root.findViewById(android.R.id.progress);
        mEmpty = (TextView)root.findViewById(android.R.id.empty);
        mError = (TextView)root.findViewById(R.id.error);
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final HttpDataSource dataSource = getHttpDataSource();
        final NewsArrayProcessor processor = getProcessor();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }

        });
        update(dataSource, processor);

    }
    private NewsArrayProcessor getProcessor() {
        mNewsArrayProcessor = new NewsArrayProcessor(getActivity().getApplicationContext());
        return mNewsArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return new VkDataSource();
    }
    private void update(HttpDataSource dataSource, NewsArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        String url = Api.NEWS_GET ;
        String signUrl = VkOAuthHelper.sign(url);
        return signUrl;
    }

    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mEmpty.setVisibility(View.GONE);
    }
    private List<News> mData;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<News> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            mEmpty.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mData = data;
            mAdapter = new ArrayAdapter<News>(getActivity().getApplicationContext(), R.layout.news_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity().getApplicationContext(), R.layout.wall_item, null);
                    }
                    News item = getItem(position);
                    TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                    textView1.setText(item.getText());
                    TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                    textView2.setText(item.getText());
                    convertView.setTag(item.getPostId());
                    final String url = item.getPhoto();
                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon1);
                    mImageLoader.loadAndDisplay(url, imageView);

                    return convertView;
                }

            };

            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailsNewsActivity.class);
                   News item = (News) mAdapter.getItem(position);
                    //NoteGsonModel note = new NoteGsonModel(item.getId(), item.getFirstName(), item.getLastName());
                    intent.putExtra("item", item );
                    startActivity(intent);
                }
            });

        } else {
            mData.clear();
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        mProgressBar.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mError.setText(mError.getText() + "\n" + e.getMessage());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(KEY);
    }

}
