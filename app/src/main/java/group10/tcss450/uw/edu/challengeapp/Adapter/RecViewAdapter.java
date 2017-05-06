/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.Adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import group10.tcss450.uw.edu.challengeapp.BrewTour.TopBrewery;
import group10.tcss450.uw.edu.challengeapp.R;

import static group10.tcss450.uw.edu.challengeapp.BrewTour.TopBrewery.brewery;

/**
 * An adapter class to coordinate the recycler view
 */
public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ViewHolder> {

    /** The list of TopBrewery objects that need to be added to the recycler view.*/
    private ArrayList<TopBrewery> mDataset;
    /** Resources to use string resources */
    private Resources mResources;

    /**
     * A View Holder used to change the elements inside the recycler view.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        /**The ImageView for the mBrewery logo to go into.*/
        ImageView mImageView;
        /** The TextView to display the name of the mBrewery.*/
        TextView mBreweryName;
        /** The TextView to display the hours of the mBrewery.*/
        TextView mHours;
        /** The TextView to display the distance from your location*/
        TextView mDist;


        /**
         * Constructor class. Initialize all fields.
         * @param cardView the Cardview being passed.
         */
        ViewHolder(CardView cardView) {
            super(cardView);
            mImageView = (ImageView) cardView.findViewById(R.id.brew_pic);
            mBreweryName = (TextView) cardView.findViewById(R.id.brewery_name);
            mHours = (TextView) cardView.findViewById(R.id.hours);
            mDist = (TextView) cardView.findViewById(R.id.dist);
        }
    }

    /**
     * Recycler view adapter constructor.
     * @param myDataset the desired dataset for the recycler view.
     */
    public RecViewAdapter (ArrayList<TopBrewery> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public RecViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.brew_tour_card_view,parent, false);
        mResources = parent.getContext().getResources();
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        TopBrewery topBrewery = mDataset.get(position);
        String name = "";
        String hours = topBrewery.getHoursOfOperation();
        String distance;
        brewery brewery = topBrewery.getBrewery();
        ImageView imageView = holder.mImageView;
        if (brewery != null) {
            TopBrewery.images images = brewery.getImages();
            name = brewery.getName();

            if(images != null) {
                new DownloadImageTask(imageView).execute(images.getSquareLarge());
            } else {
                holder.mImageView.setImageResource(R.drawable.stout);
            }
        } else {
            holder.mImageView.setImageResource(R.drawable.stout);
        }
        if (name == null || name.equals("")) name = "Brewery";
        if (hours == null) hours = "not available";
        if (topBrewery.getDistance() == null) distance = "unavailable";
        else distance = "" + mDataset.get(position).getDistance();
        holder.mBreweryName.setText(mResources.getString(R.string.text_view_brewery) + name);
        holder.mDist.setText(mResources.getString(R.string.text_view_distance) + distance);
        holder.mHours.setText(mResources.getString(R.string.text_view_hours) + hours);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Helper class used to download and return an image.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView bmImage;

        /**
         * Constructor
         * @param bmImage The ImageView to be updated.
         */
        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        /**
         * Makes a connection to the URL, downloads the image, and saves it as bitmap
         * @param urls the URLs to check for an image.
         * @return a Bitmap image from the url.
         */
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
