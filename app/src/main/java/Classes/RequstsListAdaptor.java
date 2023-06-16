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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequstsListAdaptor extends ArrayAdapter<RequestVM> {
    public RequstsListAdaptor(Context context2, ArrayList<RequestVM> requests) {
        super(context2, R.layout.request_item, requests);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view1, @NonNull ViewGroup parent2) {

//        ServicesRequest request = getItem(position);
        RequestVM request=getItem(position);
        if (view1 == null) {
            view1 = LayoutInflater.from(getContext()).inflate(R.layout.request_item, parent2, false);
        }

        ImageView ivItemImage2 = view1.findViewById(R.id.ivItemImage2);
        TextView tvItemReqType = view1.findViewById(R.id.tvItemReqType);
        TextView tvItemReqDate = view1.findViewById(R.id.tvItemReqDate);
        TextView tvItemReqNo = view1.findViewById(R.id.tvItemReqNo);
        TextView tvReqBarcode = view1.findViewById(R.id.tvReqBarcode);
        TextView tvItemReqStatus = view1.findViewById(R.id.tvItemReqStatus);

        tvItemReqType.setText(request.request.requestType);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(request.request.requestDate);
        String ss=String.valueOf(calendar.get(calendar.DAY_OF_MONTH))+"-"+String.valueOf(calendar.get(calendar.MONTH))+"-"+String.valueOf(calendar.get(calendar.YEAR ));
        tvItemReqDate.setText(ss);
        if(request.request.subscription!=null){
            tvReqBarcode.setText(request.request.subscription.consumerBarCode);
        }else {
            tvReqBarcode.setText("No Barcode");
        }
        tvItemReqStatus.setText(request.request.requestStatus);
        tvItemReqNo.setText(String.valueOf(request.request.id));

        if (request.request.requestStatus.equals("completed")) {
            ivItemImage2.setImageResource(R.drawable.request_completed);
        }
        if (request.request.requestStatus.equals("onprogress")) {
            ivItemImage2.setImageResource(R.drawable.request_pending);
        }
        if (request.request.requestStatus.equals("rejected")) {
            ivItemImage2.setImageResource(R.drawable.request_rejected1);
        }


        return view1;
    }
}
