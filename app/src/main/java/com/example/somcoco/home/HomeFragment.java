package com.example.somcoco.home;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.somcoco.R;
import com.example.somcoco.searchcampus.CampusMapActivity;
import com.example.somcoco.searchcampus.CampusSearchActivity;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import static android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH;

public class HomeFragment extends Fragment {

    TipsFragment1 fragment1;
    TipsFragment2 fragment2;
    TipsFragment3 fragment3;
    SpeechRecognizer recognizer;
    EditText inputData;
    final int PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);    // 홈 프래그먼트 화면 인플레이트


        // 인터넷과 음성녹음 퍼미션 요청
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        fragment1 = new TipsFragment1();
        fragment2 = new TipsFragment2();
        fragment3 = new TipsFragment3();
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.tips);
//        inputData = (EditText) rootView.findViewById(R.id.home_campus_keyword);
//        ImageView search = (ImageView) rootView.findViewById(R.id.home_search_btn);
//        ImageView mic = (ImageView) rootView.findViewById(R.id.home_mic);

//        inputData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                switch(actionId){
//                  case IME_ACTION_SEARCH :
//                      String keyword = inputData.getText().toString();
//
//                      if(keyword.equals("")){
//                          Toast.makeText(getActivity(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
//                      } else {
//                          Intent intent = new Intent(getActivity(), CampusSearchActivity.class);
//                          intent.putExtra("keyword", keyword);
//                          startActivity(intent);
//                      }
//                    break;
//
//                  default:
//                    break;
//                }
//                return true;
//            }
//        });

        TipsPagerAdapter adapter = new TipsPagerAdapter(getChildFragmentManager());


        adapter.addItem(fragment1);
        adapter.addItem(fragment2);
        adapter.addItem(fragment3);

        pager.setOffscreenPageLimit(adapter.getCount());        // 뷰페이지 개수만큼 미리 로딩
        pager.setSaveEnabled(false);                            // 뷰페이지 상태 저장여부
        pager.setSaveFromParentEnabled(false);
        pager.setAdapter(adapter);


        PageIndicatorView pageIndicatorView = (PageIndicatorView) rootView.findViewById(R.id.page_indicator_view);
        pageIndicatorView.setCount(3);          // 뷰페이지 인디케이터 개수 설정
        pageIndicatorView.setSelection(2);      // 뷰페이지 디폴트 순서 값

//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getActivity().getPackageName());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String keyword = inputData.getText().toString();
//
//                if(keyword.equals("")){
//                    Toast.makeText(getActivity(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(getActivity(), CampusSearchActivity.class);
//                    intent.putExtra("keyword", keyword);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        mic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
//                recognizer.setRecognitionListener(listener);
//                recognizer.startListening(intent);
//            }
//        });

        return rootView;
    }


    class TipsPagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public TipsPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }



//    private RecognitionListener listener = new RecognitionListener() {
//        @Override
//        public void onReadyForSpeech(Bundle params) {
//            Toast.makeText(getActivity(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onBeginningOfSpeech() {
//
//        }
//
//        @Override
//        public void onRmsChanged(float rmsdB) {
//
//        }
//
//        @Override
//        public void onBufferReceived(byte[] buffer) {
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//
//        }
//
//        @Override
//        public void onError(int error) {
//            String message;
//            switch (error) {
//                case SpeechRecognizer.ERROR_AUDIO:
//                    message = "오디오 에러";
//                    break;
//                case SpeechRecognizer.ERROR_CLIENT:
//                    message = "클라이언트 에러";
//                    break;
//                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                    message = "퍼미션 없음";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK:
//                    message = "네트워크 에러";
//                    break;
//                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                    message = "네트웍 타임아웃";
//                    break;
//                case SpeechRecognizer.ERROR_NO_MATCH:
//                    message = "찾을 수 없음";
//                    break;
//                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                    message = "RECOGNIZER가 바쁨";
//                    break;
//                case SpeechRecognizer.ERROR_SERVER:
//                    message = "서버가 이상함";
//                    break;
//                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                    message = "말하는 시간초과";
//                    break;
//                default:
//                    message = "알 수 없는 오류임";
//                    break;
//            }
//            Toast.makeText(getActivity(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onResults(Bundle results) {
//            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//
//            for (int i = 0; i < matches.size(); i++) {
//                inputData.setText(matches.get(i));
//            }
//        }
//
//        @Override
//        public void onPartialResults(Bundle partialResults) {
//
//        }
//
//        @Override
//        public void onEvent(int eventType, Bundle params) {
//
//        }
//    };
}
