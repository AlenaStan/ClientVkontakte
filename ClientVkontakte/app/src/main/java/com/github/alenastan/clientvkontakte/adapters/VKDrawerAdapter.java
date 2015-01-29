package com.github.alenastan.clientvkontakte.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.alenastan.clientvkontakte.R;

/**
 * Created by lena on 25.01.2015.
 */
public class VKDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mTitles;
    private int[] mImages;
    private LayoutInflater mInflater;
    private int[] mSelectedPosition;

    public VKDrawerAdapter(Context context, String[] titles, int[] images,
                           int[] selectedPosition) {
        mContext = context;
        mTitles = titles;
        mImages = images;
        mInflater = LayoutInflater.from(this.mContext);
        mSelectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Object getItem(int position) {
         return position;
    }

    @Override
    public long getItemId(int position) {
         return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_list, null);
            mViewHolder = new ViewHolder();
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mTvTitle = (TextView) convertView.findViewById(R.id.section_text);
        mViewHolder.mIvIcon = (ImageView) convertView
                .findViewById(R.id.section_image);

        mViewHolder.mTvTitle.setText(mTitles[position]);
        mViewHolder.mIvIcon.setImageResource(mImages[position]);
        if (position == mSelectedPosition[0]) {
            mViewHolder.mTvTitle.setTextColor(Color.BLACK);
            mViewHolder.mTvTitle.setActivated(true);
        } else {
            mViewHolder.mTvTitle.setTextColor(Color.WHITE);
            mViewHolder.mTvTitle.setActivated(false);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mTvTitle;
        ImageView mIvIcon;
    }
}
