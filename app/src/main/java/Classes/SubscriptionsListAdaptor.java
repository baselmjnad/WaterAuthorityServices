package Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.waterauthorityservices.R;

import java.util.ArrayList;

public class SubscriptionsListAdaptor extends ArrayAdapter<Subscription> {
    public SubscriptionsListAdaptor(Context context, ArrayList<Subscription> subscriptions){
        super(context, R.layout.subscription_item,subscriptions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        Subscription subscription=getItem(position);
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.subscription_item,parent,false);
        }

        TextView tvItemStatus=view.findViewById(R.id.tvItemStatus);
        TextView tvItemBarcode=view.findViewById(R.id.tvItemBarcode);
        TextView tvItemSubscritionNo=view.findViewById(R.id.tvItemSubscritionNo);
        TextView tvItemType=view.findViewById(R.id.tvItemType);
        TextView tvItemAdress=view.findViewById(R.id.tvItemAdress);

        tvItemBarcode.setText(subscription.consumerBarCode);
        tvItemSubscritionNo.setText(subscription.consumerSubscriptionNo);
        tvItemType.setText(subscription.subscriptionUsingType);
        tvItemAdress.setText(subscription.subscriptionAddress);
        if(subscription.subscriptionStatus.equals("active")){
            tvItemStatus.setBackgroundResource(R.color.Green);
            tvItemStatus.setText("Active");
        }else {
            tvItemStatus.setBackgroundResource(R.color.Red);
            tvItemStatus.setText("Not Active");
        }

        return view;
    }
}
