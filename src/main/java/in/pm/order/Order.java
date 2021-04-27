package in.pm.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import in.pm.order.Model.OrderModel;

import static in.pm.order.ConstantOrder.MyPREFERENCES;
import static in.pm.order.ConstantOrder.UserNumber;

public class Order extends AppCompatActivity {

    String productId, productName, productPrice, productDetails, productImage, productRating;
    TextView OrderDetails, OrderName, OrderPrice;
    ImageView OrderImage, BackFromOrder, FavProduct;
    Button ProductOrder;

    private Boolean profileSet = false;
    String OrderId, coustomerNumber, orderDateTime;

    String storeType="Grocery", storeName="Prakash general store", status="Order";

    String discount="20%", coustomerAddress="Bilaspur, C.G", estimatedTime="within 1day";

    double latitude=22.001231, longitude=82.204569;
    DatabaseReference orderReference;
    List<OrderModel> orderList;

    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();

        db = FirebaseFirestore.getInstance();
        orderReference = FirebaseDatabase.getInstance().getReference("Orders");
        orderList = new ArrayList<>();

        SharedPreferences sh = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        coustomerNumber = sh.getString(UserNumber, "Anonymous");

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println(formatter.format(date));
        orderDateTime= formatter.format(date);

        Intent intent = getIntent();
        productId = (String) intent.getSerializableExtra("productId");
        productName = (String) intent.getSerializableExtra("productName");
        productPrice = (String)intent.getSerializableExtra("productPrice");
        productDetails = (String)intent.getSerializableExtra("productDetails");
        productImage = (String)intent.getSerializableExtra("productImage");
        productRating = (String)intent.getSerializableExtra("productRating");

        if (productImage.equals("default")){
            OrderImage.setImageResource(R.drawable.favorite_img_4);
        }else {
            Glide.with(this).load(productImage).into(OrderImage);
        }
        OrderDetails.setText(productDetails);
        OrderName.setText(productName);
        OrderPrice.setText(productPrice);
        OrderId= UUID.randomUUID().toString().replace("-","").substring(0,9);

        BackFromOrder.setOnClickListener(v-> {
            this.finish();
        });
        FavProduct.setOnClickListener(v->{
            FavProduct.setImageResource(R.drawable.ic_favorite_red_24dp);
        });

        ProductOrder.setOnClickListener(v-> {
            Cick();
        });
    }

    private void init() {
        OrderDetails =findViewById(R.id.orderDetails);
        OrderName =findViewById(R.id.orderName);
        OrderPrice =findViewById(R.id.orderPrice);

        OrderImage= findViewById(R.id.orderImage);
        BackFromOrder= findViewById(R.id.backFromOrder);
        FavProduct= findViewById(R.id.favProduct);

        ProductOrder= findViewById(R.id.productOrder);
    }

    public void Cick() {
        OrderSheet dialog = new OrderSheet(profileSet);
        dialog.showNow(getSupportFragmentManager(), OrderSheet.class.getSimpleName());
        dialog.setDialogClickListener(new OrderSheet.DialogClickListener() {
            @Override
            public void onDialogGalleryClick() {

                new SweetAlertDialog(Order.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Succesfully ordered")
                        .setContentText("Will connect with you as soon as possible, Your order has been placed you can cancel it from order section.\n-Your order id is-\n" +OrderId)
                        .show();
                //orderProduct();
                saveProduct();
            }

            @Override
            public void onDialogCameraClick() {
                new SweetAlertDialog(Order.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Ooopss")
                        .setContentText("We are only accept order from Cash on delivery we can add this later, If yo still want to pay then you can check out the seller bank details and pay there.\n"+ "Thank You.")
                        .show();
            }

            @Override
            public void onDialogRemoveClick() {
                //viewModel.removeProfilePicture(requireActivity().getApplicationContext());
            }
        });
    }

    private void orderProduct() {
        String a="0";
        if (a.equals("0")) {

            String id = orderReference.push().getKey();
            OrderModel profile = new OrderModel(OrderId, productId, coustomerNumber, productPrice, discount, status,
                    OrderId, latitude, longitude, coustomerAddress, estimatedTime, orderDateTime);
            orderReference.child(storeType).child(storeName).child(coustomerNumber).child(id).setValue(profile);

        } else {
            Toast.makeText(this, "Server error!", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProduct(){

        String a="0";
        if (a.equals("0")) {

            CollectionReference dbProducts = db.collection("Orders");

            CollectionReference dbProducts1 = db.collection("Orders11");
            OrderModel profile = new OrderModel(OrderId, productId, coustomerNumber, productPrice, discount, status,
                    OrderId, latitude, longitude, coustomerAddress, estimatedTime, orderDateTime);

            dbProducts.add(profile)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            dbProducts1.add(profile)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(Order.this, "Product Added", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }


}