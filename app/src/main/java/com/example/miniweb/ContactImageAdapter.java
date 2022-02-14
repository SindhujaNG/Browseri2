package com.example.miniweb;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ContactImageAdapter extends ArrayAdapter<Website1>{
	 Context context;
	    int layoutResourceId;   
	   // BcardImage data[] = null;
	    ArrayList<Website1> data=new ArrayList<Website1>();
	    public ContactImageAdapter(Context context, int layoutResourceId, ArrayList<Website1> data) {
	        super(context, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View row = convertView;
	        ImageHolder holder = null;
	       
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResourceId, parent, false);
	           
	            holder = new ImageHolder();
	            holder.txtTitle = row.findViewById(R.id.txtTitle);
	            holder.txtUrl = row.findViewById(R.id.txtUrl);

	            row.setTag(holder);
	        }
	        else
	        {
	            holder = (ImageHolder)row.getTag();
	        }
	       
	        Website1 picture = data.get(position);
	        holder.txtTitle.setText(picture._title);
	        holder.txtUrl.setText(picture._url);
	        //convert byte to bitmap take from contact class
	        

	       return row;
	       
	    }
	   
	    static class ImageHolder
	    {
	        ImageView imgIcon;
	        TextView txtTitle;
	        TextView txtUrl;
	    }
	}
