package com.jeremiahvaris.travelmantics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealsViewHolder> {

    ArrayList<TravelDeal> deals = new ArrayList<>();
    DatabaseReference databaseReference;
    private boolean isAdmin = false;

    public DealsAdapter() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(AdminActivity.DATABASE_REFERENCE_PATH);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                deal.setId(dataSnapshot.getKey());
                deals.add(deal);
                notifyItemInserted(deals.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deal_list_layout, parent, false);
        return new DealsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsViewHolder holder, int position) {
        holder.bind(deals.get(position));

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public void isAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    class DealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTV;
        TextView priceTV;
        TextView descriptionTV;


        DealsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title_tv);
            priceTV = itemView.findViewById(R.id.price_tv);
            descriptionTV = itemView.findViewById(R.id.description_tv);
            itemView.setOnClickListener(this);

        }

        void bind(TravelDeal deal) {
            titleTV.setText(deal.getTitle());
            priceTV.setText(deal.getPrice());
            descriptionTV.setText(deal.getDescription());
        }

        @Override
        public void onClick(View view) {
//            getAdapterPosition();
            // Todo: Do something

        }

    }
}
