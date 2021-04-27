package in.pm.order.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import in.pm.order.Model.MasterModel;
import in.pm.order.Order;
import in.pm.order.R;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder>{
    private List<MasterModel> shops;
    private Context mContext;

    public MasterAdapter(List<MasterModel> lists, Context mContext) {
        this.shops = lists;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MasterModel sp=shops.get(position);
        holder.ProductName.setText(sp.getProductName());
        holder.ProductPrice.setText(sp.getProductPrice());

        if (sp.getProductImage().equals("default")){
            holder.ProductImage.setImageResource(R.drawable.favorite_img_4);
        }else {
            Glide.with(mContext).load(sp.getProductImage()).into(holder.ProductImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mContext, Order.class);
                intent.putExtra("productId", sp.getProductId());
                intent.putExtra("productName", sp.getProductName());
                intent.putExtra("productPrice", sp.getProductPrice());
                intent.putExtra("productDetails", sp.getProductDetails());
                intent.putExtra("productImage", sp.getProductImage());
                intent.putExtra("productRating", sp.getRating());
                /*intent.putExtra("", sp.);
                intent.putExtra("", sp.);
                intent.putExtra("", sp.);*/
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView ProductName, ProductPrice;
        private ImageView ProductImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ProductName=(TextView)itemView.findViewById(R.id.productName);
            ProductPrice=(TextView)itemView.findViewById(R.id.productPrice);
            ProductImage=(ImageView)itemView.findViewById(R.id.productImage);
        }
    }
}
