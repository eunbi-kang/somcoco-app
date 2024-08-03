package com.example.somcoco.tour;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.somcoco.R;

public class NFCActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String tagID;
    String cpsCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Intent inIntent = getIntent();
        cpsCode = inIntent.getStringExtra("cps");

        ImageView nfc = (ImageView) findViewById(R.id.NFC_tag);
        Glide.with(this).load(R.raw.nfc).into(nfc);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0 , intent, 0);
    }

    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
            tagID = toHexString(tagId);
        }
        Intent intent1 = new Intent(this, BuildingInfoActivity.class);
        intent1.putExtra("tagID", tagID);
        intent1.putExtra("cps", cpsCode);
        startActivity(intent1);
        finish();
    }
    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }
}
