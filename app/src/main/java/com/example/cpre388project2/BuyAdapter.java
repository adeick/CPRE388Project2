package com.example.cpre388project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {
    private String[] purchasableDataSet; //all the purchasable items

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button upgrade;
        private final Button customize;
        private final Button buy;

        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //this will probably do something lol
                }
            });
            textView = (TextView) view.findViewById(R.id.textView);
            upgrade = (Button)view.findViewById(R.id.buttonUp);
            customize = (Button)view.findViewById(R.id.buttonCust);
            buy = (Button) view.findViewById(R.id.buttonBuy);
        }

        public TextView getTextView(){
            return textView;
        }
        public Button getUpgrade(){ return upgrade; }
        public Button getCustomize() { return customize; }
        public Button getBuy() { return buy; }


    }

    public BuyAdapter(String[] dataSet){
        purchasableDataSet = dataSet;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(purchasableDataSet[position]);
    }

    public int getItemCount(){
        return purchasableDataSet.length;
    }

}