package com.firebase.demo.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.HashMap;

// [START blog_user_class]
@IgnoreExtraProperties
public class Product implements Serializable {


    public String category;
    public String image;
    public String name;
    public String price;
    public String productid;
    public Integer count;
    public HashMap<String,Integer> stars =new HashMap<String,Integer>();



    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Product(String productType,String image,String productName,
                   String price,String productId,Integer productCount) {
        this.category = productType;
        this.image = image;

        this.name = productName;
        this.price = price;
         this.count =productCount;
         this.productid = productId;

    }

}
// [END blog_user_class]
