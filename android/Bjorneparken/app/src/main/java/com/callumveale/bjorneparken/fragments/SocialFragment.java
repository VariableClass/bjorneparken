package com.callumveale.bjorneparken.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.callumveale.bjorneparken.R;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    //region Constants

    private static final String ARG_INTERNET = "internet-available";

    public static final int FACEBOOK = 1;
    public static final int TWITTER = 2;
    public static final int INSTAGRAM = 3;
    public static final int OTHER = 4;

    private static final String TWITTER_PACKAGE = "com.twitter";
    private static final String INSTAGRAM_PACKAGE = "com.instagram";

    //endregion Constants

    //region Properties

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private String mCurrentPhotoPath;
    private boolean mInternetAvailable;

    //endregion Properties

    //region Constructors

    public SocialFragment() {
        // Required empty public constructor
    }

    //endregion Constructors

    //region Methods

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SocialFragment.
     */
    public static SocialFragment newInstance(boolean internetAvailable) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_INTERNET, internetAvailable);
        fragment.setArguments(args);
        return fragment;
    }

    //region Fragment Overridden Methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mInternetAvailable = getArguments().getBoolean(ARG_INTERNET);
        }

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        ImageView facebookShare = (ImageView) view.findViewById(R.id.share_facebook);
        ImageView twitterShare = (ImageView) view.findViewById(R.id.share_twitter);
        ImageView instagramShare = (ImageView) view.findViewById(R.id.share_instagram);
        ImageView otherShare = (ImageView) view.findViewById(R.id.share_other);

        facebookShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(FACEBOOK);
            }
        });

        twitterShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(TWITTER);
            }
        });

        instagramShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(INSTAGRAM);
            }
        });

        otherShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(OTHER);
            }
        });

        LikeView likeButton = (LikeView) view.findViewById(R.id.like_button);
        likeButton.setObjectIdAndType(
                "https://www.facebook.com/bjorneparken",
                LikeView.ObjectType.PAGE);

        // If internet is available
        if (mInternetAvailable) {

            // Show the TripAdvisor webview
            WebView tripadvisor = (WebView) view.findViewById(R.id.trip_advisor);
            tripadvisor.getSettings().setJavaScriptEnabled(true);
            tripadvisor.loadData(getString(R.string.trip_advisor_widget), "text/html", null);
            tripadvisor.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //endregion Fragment Overridden Methods

    public void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    getActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    private File createImageFile() throws IOException {

        // Check storage permissions are have been granted
        verifyStoragePermissions();

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri uri = Uri.fromFile(new File(mCurrentPhotoPath));

            try {

                Bitmap photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                switch (requestCode){

                    case FACEBOOK:
                        shareToFacebook(photo);
                        break;

                    case TWITTER:
                        shareToTwitter();
                        break;

                    case INSTAGRAM:
                        shareToInstagram();
                        break;

                    case OTHER:
                        shareToOther();
                        break;

                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shareToFacebook(Bitmap photo){

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(photo)
                .build();

        ShareHashtag hashtag = new ShareHashtag.Builder()
                .setHashtag(getString(R.string.hashtag))
                .build();

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .setShareHashtag(hashtag)
                    .setPlaceId(getString(R.string.facebook_place_id))
                    .build();

            shareDialog.show(content);
        }
    }

    private void shareToTwitter(){

        shareToSpecificApp(TWITTER_PACKAGE);
    }

    private void shareToInstagram(){

        shareToSpecificApp(INSTAGRAM_PACKAGE);
    }

    private void shareToSpecificApp(String appPackage){

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mCurrentPhotoPath));

        // Narrow down to official Twitter app, if available:
        List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {

            if (info.activityInfo.packageName.toLowerCase().startsWith(appPackage)) {

                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);
    }

    private void shareToOther(){

        Intent shareToOther = new Intent(Intent.ACTION_SEND);
        shareToOther.setType("image/jpeg");
        shareToOther.putExtra(Intent.EXTRA_STREAM, Uri.parse(mCurrentPhotoPath));
        startActivity(Intent.createChooser(shareToOther, getString(R.string.share_photo)));
    }

    //endregion Methods

    //region Interfaces

    public interface SocialListener {

        void takePhoto(int requestCode);
    }

    //endregion Interfaces
}
