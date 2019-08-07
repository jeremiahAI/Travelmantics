package com.jeremiahvaris.travelmantics;

class TravelDeal {
    private String description;
    private String price;
    private String title;
    private String imageUrl;
    private String id;

    public TravelDeal() {
    }

    public TravelDeal(String description, String price, String title, String imageURL) {
        this.description = description;
        this.price = price;
        this.title = title;
        this.imageUrl = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
