package com.example.somcoco.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.somcoco.R;

public class EtcFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);

        LinearLayout notice = (LinearLayout) rootView.findViewById(R.id.notice);
        LinearLayout version = (LinearLayout) rootView.findViewById(R.id.version);
        LinearLayout faq = (LinearLayout) rootView.findViewById(R.id.faq);
        //LinearLayout cs = (LinearLayout)rootView.findViewById(R.id.cs);
        LinearLayout term = (LinearLayout)rootView.findViewById(R.id.term);
        LinearLayout location_term = (LinearLayout)rootView.findViewById(R.id.location_term);
        //LinearLayout use = (LinearLayout)rootView.findViewById(R.id.use);
        //LinearLayout calendar = (LinearLayout)rootView.findViewById(R.id.calendar);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Notice.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Faq.class);
                startActivity(intent);
            }
        });

//        cs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Cs.class);
//                startActivity(intent);
//            }
//        });

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Term.class);
                startActivity(intent);
            }
        });

        location_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Location_term.class);
                startActivity(intent);
            }
        });

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Version.class);
                startActivity(intent);
            }
        });

//        use.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Use.class);
//                startActivity(intent);
//            }
//        });

//        calendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Calendar.class);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }
}
