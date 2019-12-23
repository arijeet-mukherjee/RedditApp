package com.arijeet.newsapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
//import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 4/4/2017.
 */

public class CustomListAdapter  extends ArrayAdapter<post> {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        TextView author;
       TextView date_updated;
       ProgressBar progressBar;
         //String postURL;
        ImageView thumbnailURL;
        //LaterAdded
        ImageButton shareButton,saveButton;

    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public CustomListAdapter(Context context, int resource, ArrayList<post> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

        //sets up the image loader library
        setupImageLoader();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        //get the persons information
        String title = getItem(position).getTitle();
        final String imgUrl = getItem(position).getThumbnailURL();
        String author = getItem(position).getAuthor();
        String date_updated = getItem(position).getDate_updated();


        try{


            //create the view result for showing the animation
            final View result;

            //ViewHolder object
            final ViewHolder holder;

            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder= new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.thumbnailURL = (ImageView) convertView.findViewById(R.id.img);
                holder.author = (TextView) convertView.findViewById(R.id.author);
                holder.date_updated = (TextView) convertView.findViewById(R.id.updated);
                holder.progressBar= (ProgressBar) convertView.findViewById(R.id.progressBar);
                //laterAdded
                holder.shareButton=(ImageButton)convertView.findViewById(R.id.shareButton);
                result = convertView;

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }


//            Animation animation = AnimationUtils.loadAnimation(mContext,
//                    (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//            result.startAnimation(animation);

            lastPosition = position;

            holder.title.setText(title);
            holder.author.setText(author);
            holder.date_updated.setText(date_updated);

            //added

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        //String shareBody = "Hey, I'm using *Name of the Sticker App* for sending awesome Stickers on WhatsApp\n \nDownload it now: https://play.google.com/store/apps/details?id=com.yourappurlhere";
                        String shareBody = imgUrl;

                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        //change the app package id as your wish for sharing content to the specific one, WhatsApp's package id is com.whatsapp, and for facebook is com.facebook.katana
                        sharingIntent.setPackage("com.whatsapp");
                        mContext.startActivity(sharingIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Intent sharingIntent1 = new Intent(Intent.ACTION_SEND);
                        sharingIntent1.setType("text/plain");
                        String shareBody = "Hey, I'm using the best app for sending awesome Stickers called *Your App Name Here* \n \nDownload it now: https://play.google.com/store/apps/details?id=com.yourappurlhere";
                        String shareSubject = "Stickers Android App";
                        sharingIntent1.putExtra(Intent.EXTRA_TEXT, shareBody);
                        sharingIntent1.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                        mContext.startActivity(Intent.createChooser(sharingIntent1, "Share with friends"));
                    }


                }
            });

            //endaddded





            //create the imageloader object
            ImageLoader imageLoader = ImageLoader.getInstance();

            int defaultImage = mContext.getResources().getIdentifier("@drawable/reddit_alien",null,mContext.getPackageName());

            //create display options
            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(defaultImage)
                    .showImageOnFail(defaultImage)
                    .showImageOnLoading(defaultImage).build();

            //download and display image from url
            imageLoader.displayImage(imgUrl, holder.thumbnailURL, options , new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                }
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.progressBar.setVisibility(View.GONE);
                }
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressBar.setVisibility(View.GONE);
                }
                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    holder.progressBar.setVisibility(View.GONE);
                }

            });

            return convertView;
        }catch (IllegalArgumentException e){
            //Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage() );
            return convertView;
        }

    }

    /**
     * Required for setting up the Universal Image loader Library
     */
    private void setupImageLoader(){
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}

