package com.example.somcoco.tour;

public class FacilityCarouselItem {
    String noticeImage;
    String imageLink;

    public FacilityCarouselItem(String noticeImage, String imageLink) {
        this.noticeImage = noticeImage;
        this.imageLink = imageLink;
    }

    public String getNoticeImage() {
        return noticeImage;
    }

    public void setNoticeImage(String noticeImage) {
        this.noticeImage = noticeImage;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
