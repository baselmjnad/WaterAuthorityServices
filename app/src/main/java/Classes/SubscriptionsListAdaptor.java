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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Subscription subscription=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.subscription_item,parent,false);
        }

        TextView tvItemStatus=convertView.findViewById(R.id.tvItemStatus);
        TextView tvItemBarcode=convertView.findViewById(R.id.tvItemBarcode);
        TextView tvItemSubscritionNo=convertView.findViewById(R.id.tvItemSubscritionNo);
        TextView tvItemType=convertView.findViewById(R.id.tvItemType);
        TextView tvItemAdress=convertView.findViewById(R.id.tvItemAdress);

        tvItemBarcode.setText(subscription.consumerBarCode);
        tvItemSubscritionNo.setText(subscription.consumerSubscriptionNo);
        tvItemType.setText(subscription.subscriptionUsingType);
        tvItemAdress.setText(subscription.subscriptionAddress);
        if(subscription.subscriptionStatus.equals("active")){
            tvItemStatus.setBackgroundResource(R.color.Green);
        }else {
            tvItemStatus.setBackgroundResource(R.color.Red);
        }

        return super.getView(position, convertView, parent);
    }
}
