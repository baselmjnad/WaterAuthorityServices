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

public class InvoicesListAdaptor extends ArrayAdapter<Invoice> {
    public InvoicesListAdaptor(Context context, ArrayList<Invoice> invoices){
        super(context, R.layout.invoice_item,invoices);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        Invoice invoice=getItem(position);
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.invoice_item,parent,false);
        }

        ImageView ivItemImage1=view.findViewById(R.id.ivItemImage1);
        TextView tvItemYear=view.findViewById(R.id.tvItemYear);
        TextView tvItemCycle=view.findViewById(R.id.tvItemCycle);
        TextView tvItemStatus=view.findViewById(R.id.tvItemStatus);
        TextView tvItemValue=view.findViewById(R.id.tvItemValue);

        tvItemYear.setText(invoice.invoiceYear);
        tvItemCycle.setText(invoice.invoiceCycle);
        if(invoice.invoiceStatus){
            tvItemStatus.setText("Paid");
        }else {
            tvItemStatus.setText("Not Paid");
        }
        tvItemValue.setText(invoice.invoiceValue);
        if(invoice.invoiceStatus){
            ivItemImage1.setImageResource(R.drawable.paid);
        }else {
            ivItemImage1.setImageResource(R.drawable.notpaid);
        }

        return view;
    }
}
