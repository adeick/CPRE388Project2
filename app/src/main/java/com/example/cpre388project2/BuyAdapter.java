package com.example.cpre388project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388project2.towers.TowerTypes;

import java.lang.ref.WeakReference;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {
    private String[] purchasableDataSet; //all the purchasable items
    private BuyListener listener;

    public BuyAdapter(String[] dataSet, BuyListener newListener){
        listener = newListener;
        purchasableDataSet = dataSet;
    }

    //public interface Upgrade


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;
        private final Button upgrade;
        private final Button customize;
        private final Button buy;
        private WeakReference<BuyListener> listenerRef;

        public ViewHolder(View view, BuyListener listener){
            super(view);
            listenerRef = new WeakReference<>(listener);
            textView = (TextView) view.findViewById(R.id.textView);

            buy = (Button) view.findViewById(R.id.buyButton);
            buy.setOnClickListener(this);

            upgrade = (Button)view.findViewById(R.id.upgradeButton);
            upgrade.setOnClickListener(this);
            customize = (Button)view.findViewById(R.id.customizeButton);
            customize.setOnClickListener(this);

        }
        @Override
        public void onClick(View v){
            listenerRef.get().onPositionClicked(getAdapterPosition(), v.getResources().getResourceEntryName(v.getId()));
        }


        public TextView getTextView(){
            return textView;
        }
        public Button getUpgrade(){ return upgrade; }
        public Button getCustomize() { return customize; }
        public Button getBuy() { return buy; }


    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(purchasableDataSet[position]);
        if(purchasableDataSet[position].equals("Firewall") ){
            viewHolder.getBuy().setVisibility(View.GONE);
        }
        if(purchasableDataSet[position].equals("Auto Clicker") || purchasableDataSet[position].equals("Firewall")){
            viewHolder.getCustomize().setVisibility(View.GONE);
        }
        if(purchasableDataSet[position].equals("Auto Clicker")){
            viewHolder.getUpgrade().setVisibility(View.GONE);

        }

    }

    public int getItemCount(){
        return purchasableDataSet.length;
    }

}
