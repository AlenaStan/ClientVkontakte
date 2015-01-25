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
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.alenastan.clientvkontakte.Api;
import com.github.alenastan.clientvkontakte.DetailsActivity;
import com.github.alenastan.clientvkontakte.MainActivity;
import com.github.alenastan.clientvkontakte.R;
import com.github.alenastan.clientvkontakte.bo.Friend;
import com.github.alenastan.clientvkontakte.bo.NoteGsonModel;
import com.github.alenastan.clientvkontakte.helper.DataManager;
import com.github.alenastan.clientvkontakte.image.ImageLoader;
import com.github.alenastan.clientvkontakte.processing.FriendArrayProcessor;
import com.github.alenastan.clientvkontakte.source.HttpDataSource;
import com.github.alenastan.clientvkontakte.source.VkDataSource;

import java.util.List;

/**
 * Created by lena on 25.01.2015.
 */

public class FragmentFriends extends Fragment implements DataManager.Callback<List<Friend>>  {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayAdapter mAdapter;
    private FriendArrayProcessor mFriendArrayProcessor = new FriendArrayProcessor();
    private ListView mListView;
    private TextView mError;
    private TextView mEmpty;
    private ProgressBar mProgressBar;
    private ImageLoader mImageLoader;
    public static final int COUNT = 50;

    public static FragmentFriends newInstance() {
        FragmentFriends fragment = new FragmentFriends();
        return fragment;
    }

    public FragmentFriends() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
        mListView = (ListView)root.findViewById(android.R.id.list);
        mProgressBar =(ProgressBar) root.findViewById(android.R.id.progress);
        mEmpty = (TextView)root.findViewById(android.R.id.empty);
        mError = (TextView)root.findViewById(R.id.error);
        return root;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImageLoader = ImageLoader.get(getActivity().getApplicationContext());
        final HttpDataSource dataSource = getHttpDataSource();
        final FriendArrayProcessor processor = getProcessor();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        update(dataSource, processor);
    }
    private FriendArrayProcessor getProcessor() {
        return mFriendArrayProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        //return VkDataSource.get(getActivity().getApplicationContext());
        return  new VkDataSource();
    }

    private void update(HttpDataSource dataSource, FriendArrayProcessor processor) {
        DataManager.loadData(this,
                getUrl(COUNT, 0),
                dataSource,
                processor);
    }
    private String getUrl(int count, int offset) {
        return Api.FRIENDS_GET + "&count="+count+"&offset="+offset;
    }
    @Override
    public void onDataLoadStart() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mEmpty.setVisibility(View.GONE);
    }

    private List<Friend> mData;
    private boolean isPagingEnabled = true;

    private View footerProgress;

    private boolean isImageLoaderControlledByDataManager = false;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<Friend> data) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mProgressBar.setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            mEmpty.setVisibility(View.VISIBLE);
        }
        //ListView listView = (ListView) findViewById(android.R.id.list);
        footerProgress = View.inflate(getActivity().getApplicationContext(), R.layout.view_footer_progress, null);
        refreshFooter();
        if (mAdapter == null) {
            mData = data;
            mAdapter = new ArrayAdapter<Friend>(getActivity().getApplicationContext(), R.layout.adapter_item, android.R.id.text1, data) {///////////

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getActivity().getApplicationContext(), R.layout.adapter_item, null);
                    }
                    Friend item = getItem(position);
                    TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
                    textView1.setText(item.getName());
                    TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
                    textView2.setText(item.getNickname());
                    convertView.setTag(item.getId());
                    final String url = item.getPhoto();
                    final ImageView imageView = (ImageView) convertView.findViewById(android.R.id.icon);
                    mImageLoader.loadAndDisplay(url, imageView);
                    return convertView;
                }

            };
            mListView.setFooterDividersEnabled(true);
            mListView.addFooterView(footerProgress, null, false);
            mListView.setAdapter(mAdapter);
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                private int previousTotal = 0;

                private int visibleThreshold = 5;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            if (!isImageLoaderControlledByDataManager) {
                                mImageLoader.resume();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            if (!isImageLoaderControlledByDataManager) {
                                mImageLoader.pause();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            if (!isImageLoaderControlledByDataManager) {
                                mImageLoader.pause();
                            }
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    ListAdapter adapter = view.getAdapter();
                    int count = getRealAdapterCount(adapter);
                    if (count == 0) {
                        return;
                    }
                    if (previousTotal != totalItemCount && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        previousTotal = totalItemCount;
                        isImageLoaderControlledByDataManager = true;
                        DataManager.loadData(new DataManager.Callback<List<Friend>>() {
                                                 @Override
                                                 public void onDataLoadStart() {
                                                     mImageLoader.pause();
                                                 }

                                                 @Override
                                                 public void onDone(List<Friend> data) {
                                                     updateAdapter(data);
                                                     refreshFooter();
                                                     mImageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }

                                                 @Override
                                                 public void onError(Exception e) {
                                                     FragmentFriends.this.onError(e);
                                                     mImageLoader.resume();
                                                     isImageLoaderControlledByDataManager = false;
                                                 }
                                             },
                                getUrl(COUNT, count),
                                getHttpDataSource(),
                                getProcessor());
                    }
                }

            });
           mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                    Friend item = (Friend) mAdapter.getItem(position);
                    NoteGsonModel note = new NoteGsonModel(item.getId(), item.getFirstName(), item.getLastName(),item.getPhoto());
                    intent.putExtra("item", note);
                    startActivity(intent);
                }
            });
            if (data != null && data.size() == COUNT) {
                isPagingEnabled = true;
            } else {
                isPagingEnabled = false;
            }
        } else {
            mData.clear();
            updateAdapter(data);
        }
        refreshFooter();
    }

    private void updateAdapter(List<Friend> data) {
        //ListView listView = (ListView) findViewById(android.R.id.list);
        if (data != null && data.size() == COUNT) {
            isPagingEnabled = true;
            mListView.addFooterView(footerProgress, null, false);
        } else {
            isPagingEnabled = false;
            mListView.removeFooterView(footerProgress);
        }
        if (data != null) {
            mData.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    public static int getRealAdapterCount(ListAdapter adapter) {
        if (adapter == null) {
            return 0;
        }
        int count = adapter.getCount();
        if (adapter instanceof HeaderViewListAdapter) {
            HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) adapter;
            count = count - headerViewListAdapter.getFootersCount() - headerViewListAdapter.getHeadersCount();
        }
        return count;
    }

    private void refreshFooter() {
        if (footerProgress != null) {
            if (isPagingEnabled) {
                footerProgress.setVisibility(View.VISIBLE);
            } else {
                footerProgress.setVisibility(View.GONE);
            }
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
        ((MainActivity) activity).onSectionAttached(1);
    }

}
