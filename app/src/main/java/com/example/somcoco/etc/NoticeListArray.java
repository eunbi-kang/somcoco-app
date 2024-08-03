package com.example.somcoco.etc;

import java.util.ArrayList;

public class NoticeListArray {
    ArrayList<NoticeItem> noticeList = new ArrayList<NoticeItem>();
    ArrayList<NoticeItem> faqList = new ArrayList<NoticeItem>();

    public NoticeItem noticeItem(int position) {
        return noticeList.get(position);
    }

    public NoticeItem faqItem(int position) {
        return faqList.get(position);
    }
}
