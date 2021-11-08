package com.farmers.ownfarmer.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.farmers.ownfarmer.R;


public class SliderViewPagerAdapter extends PagerAdapter {

    private Context mcontext;
    LayoutInflater layoutInflater;
    int[] adsImageSliderModels;

    public SliderViewPagerAdapter(Context mcontext, int[] adsImageSliderModels) {
        this.mcontext = mcontext;
        this.adsImageSliderModels = adsImageSliderModels;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) mcontext.getSystemService(mcontext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_view,container,false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_slider);

        imageView.setImageResource(adsImageSliderModels[position]);

//        Glide.with(mcontext)
//                .load(mcontext.getResources().getString(R.string.image_url)+adsImageSliderModels.get(position).getImageUrl())
//                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
//                .apply(RequestOptions.skipMemoryCacheOf(false))
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
//                .apply(RequestOptions.centerCropTransform())
//                .into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return adsImageSliderModels.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (RelativeLayout)object);
    }
}
