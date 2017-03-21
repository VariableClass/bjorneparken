package com.callumveale.bjorneparken.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.callumveale.bjorneparken.activities.HomeActivity;
import com.callumveale.bjorneparken.models.IModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by callum on 27/02/2017.
 */
public class GetImageTask extends AsyncTask<Void, Void, Void> {

    //region Properties

    private HomeActivity mActivity;
    private IModel mItem;

    //endregion Properties

    //region Constructors

    public GetImageTask(HomeActivity activity, IModel item) {

        mActivity = activity;
        mItem = item;
    }

    //endregion Constructors

    //region Methods

    //region AsyncTask<Void, Void, Image> Overridden Methods

    @Override
    protected void onPreExecute() {

        mActivity.updateProgress(false);
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {

            URL imageUrl = new URL(mItem.getImageUrl() + "=s128");

            InputStream inputStream = imageUrl.openStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Get image
            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            inputStream.close();
            byteArrayOutputStream.close();

            // Get image byte array
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Write byte array to file
            OutputStream os = new FileOutputStream(new File(mActivity.getFilesDir() + mItem.getClass().getSimpleName() + "-" + mItem.getId()), false);
            os.write(imageBytes);
            os.flush();
            os.close();

            // Load image into item
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            mItem.setImage(image);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void params) {

        mActivity.updateProgress(true);
    }

    //endregion AsyncTask<Void, Void, MainSpeciesListResponse> Overridden Methods

    //endregion Methods
}