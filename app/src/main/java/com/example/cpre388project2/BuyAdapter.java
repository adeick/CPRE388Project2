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

/**
 * This class creates an adapter for the recycler view in the buy screen
 */
public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder> {
    private String[] purchasableDataSet; //all the purchasable items
    private BuyListener listener;

    /**
     * Method to instantiate a new buy adapter for the recycler view
     * @param dataSet list of data for the views in the recycler
     * @param newListener a listener for the buttons in the recyclerview
     */
    public BuyAdapter(String[] dataSet, BuyListener newListener){
        listener = newListener;
        purchasableDataSet = dataSet;
    }

    //public interface Upgrade


    /**
     * Holds the views for the recycler
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textView;
        private final Button upgrade;
        private final Button customize;
        private final Button buy;
        private WeakReference<BuyListener> listenerRef;

        /**
         * new instance of a ViewHolder
         * @param view
         * @param listener used to listen for button presses
         */
        private ViewHolder(View view, BuyListener listener){
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

        /**
         * when a button is clicked, the listener gets its position and name
         * @param v the view that was clicked
         */
        @Override
        public void onClick(View v){
            listenerRef.get().onPositionClicked(getAdapterPosition(), v.getResources().getResourceEntryName(v.getId()));
        }


        /**
         * @return this view's textView
         */
        public TextView getTextView(){
            return textView;
        }

        /**
         * @return this view's upgrade button
         */
        public Button getUpgrade(){ return upgrade; }

        /**
         * @return this view's custom button
         */
        public Button getCustomize() { return customize; }

        /**
         * @return this view's buy button
         */
        public Button getBuy() { return buy; }


    }

    /**
     * creates the viewHolder in onCreate
     * @param viewGroup
     * @param viewType
     * @return the new ViewHolder
     */
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    /**
     * Sets up information shown in the viewHolder when it is bound
     * @param viewHolder
     * @param position in the recycler view
     */
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

    /**
     * @return the length of the data set
     */
    public int getItemCount(){
        return purchasableDataSet.length;
    }

}
