package com.example.bookseller;

public class Product {
    String productName;
    int productUri;
    String productDetails;


    public Product(){};
    public Product(String productName, int productUri, String productDetails){
        this.productDetails = productDetails;
        this.productName = productName;
        this.productUri = productUri;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public void setProductUri(int productUri) {
        this.productUri = productUri;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductUri() {
        return productUri;
    }
}
