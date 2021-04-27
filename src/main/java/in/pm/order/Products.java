package in.pm.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jb.dev.progress_indicator.dotBounceProgressBar;

import java.util.ArrayList;
import java.util.List;

import in.pm.order.Adapter.MasterAdapter;
import in.pm.order.Model.MasterModel;

import static java.security.AccessController.getContext;


public class Products extends AppCompatActivity {
    DatabaseReference databaseReference;

    List<MasterModel> list = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;

    com.jb.dev.progress_indicator.dotBounceProgressBar dotBounceProgressBar;
    TextView EmptyView, HeaderShopName;
    ImageView EmptyImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);


        String shopId = this.getIntent().getStringExtra("shopId");
        String shopName = this.getIntent().getStringExtra("shopName");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_products);
        recyclerView.setHasFixedSize(true);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        dotBounceProgressBar = (dotBounceProgressBar) findViewById(R.id.dotBounce);
        EmptyView = (TextView) findViewById(R.id.empty_view);
        EmptyImageView = (ImageView) findViewById(R.id.empty_image_view);
        HeaderShopName= findViewById(R.id.headerName);

        HeaderShopName.setText(shopName);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child("Grocery").child(shopId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MasterModel grocery = dataSnapshot.getValue(MasterModel.class);
                    list.add(grocery);
                }
                adapter = new MasterAdapter(list, Products.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    EmptyView.setVisibility(View.VISIBLE);
                    EmptyImageView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    EmptyView.setVisibility(View.GONE);
                    EmptyImageView.setVisibility(View.GONE);
                }
                dotBounceProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dotBounceProgressBar.setVisibility(View.GONE);

            }
        });

    }
}