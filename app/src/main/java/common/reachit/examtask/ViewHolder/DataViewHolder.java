package common.reachit.examtask.ViewHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import common.reachit.examtask.Interface.OfferItemClickListener;
import common.reachit.examtask.R;

public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView name;
    private OfferItemClickListener offerItemClickListener;
    public DataViewHolder(View itemView) {
        super(itemView);

        name=(TextView) itemView.findViewById(R.id.dataText);

        itemView.setOnClickListener(this);
    }
    public void setOfferItemClickListener(OfferItemClickListener offerItemClickListener){
        this.offerItemClickListener=offerItemClickListener;
    }

    @Override
    public void onClick(View v) {

        offerItemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
