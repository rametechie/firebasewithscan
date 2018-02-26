package com.firebase.demo.model;

/**
 * Created by Juned on 7/25/2017.
 */

public class Products {


    public String category;
    public String image;
    public String name;
    public String price;
    public String productid;
    public Integer count;


     public Products() {
        // This is default constructor.
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String categoryData) {

        this.category = categoryData;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String imageData) {

        this.image = imageData;
    }

    public String getName() {

        return name;
    }

    public void setName(String nameData) {

        this.name = nameData;
    }


    public String getPrice() {

        return price;
    }

    public void setPrice(String priceData) {

        this.price = priceData;
    }
    public String getProductid() {

        return productid;
    }

    public void setProductid(String productidData) {

        this.productid = productidData;
    }

    public Integer getCount() {

        return count;
    }

    public void setCount(Integer countData) {

        this.count = countData;
    }

}