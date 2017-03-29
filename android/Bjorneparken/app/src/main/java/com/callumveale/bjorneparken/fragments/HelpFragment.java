package com.callumveale.bjorneparken.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.callumveale.bjorneparken.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HelpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends Fragment {

    //region Constructors

    public HelpFragment() {
        // Required empty public constructor
    }

    //endregion Constructors

    //region Methods

    public static HelpFragment newInstance() {

        return new HelpFragment();
    }

    //region Fragment Overridden Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        // Add telephone onclick listener
        RelativeLayout telephone = (RelativeLayout) view.findViewById(R.id.help_tel);
        telephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getString(R.string.help_tel)));
                startActivity(intent);
            }
        });

        RelativeLayout email = (RelativeLayout) view.findViewById(R.id.help_email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getString(R.string.help_email) });
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
            }
        });

        return view;
    }

    //endregion Fragment Overridden Methods

    //endregion Methods
}
