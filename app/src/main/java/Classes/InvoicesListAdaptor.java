package Classes;

import android.content.Context;
import android.graphics.Color;
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

public class InvoicesListAdaptor extends ArrayAdapter<Invoice> {
    public InvoicesListAdaptor(Context context1, ArrayList<Invoice> invoices){
        super(context1, R.layout.invoice_item,invoices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view1, @NonNull ViewGroup parent1) {

        Invoice invoice=getItem(position);
        if(view1==null){
            view1= LayoutInflater.from(getContext()).inflate(R.layout.invoice_item,parent1,false);
        }

        ImageView ivItemImage1=view1.findViewById(R.id.ivItemImage1);
        TextView tvItemYear=view1.findViewById(R.id.tvItemYear);
        TextView tvItemCycle=view1.findViewById(R.id.tvItemCycle);
        TextView tvItemStatus=view1.findViewById(R.id.tvItemStatus);
        TextView tvItemValue=view1.findViewById(R.id.tvItemValue);

        tvItemYear.setText(invoice.invoiceYear.toString());
        tvItemCycle.setText(invoice.invoiceCycle.toString());
        tvItemValue.setText(invoice.invoiceValue.toString()+" S.P");

        if(invoice.invoiceStatus){
            tvItemStatus.setText("Paid");
            tvItemStatus.setTextColor(Color.GREEN);
            ivItemImage1.setImageResource(R.drawable.paid);

        }else {
            tvItemStatus.setText("Not Paid");
            ivItemImage1.setImageResource(R.drawable.notpaid);
            tvItemStatus.setTextColor(Color.RED);

        }

        return view1;
    }
}
