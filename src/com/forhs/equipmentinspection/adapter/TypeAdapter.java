package com.forhs.equipmentinspection.adapter;

import java.util.ArrayList;

import com.forhs.equipmentinspection.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TypeAdapter extends BaseAdapter {
	private ArrayList<Integer> types;//泛型数组
	private LayoutInflater inflater; //Layout形成一个以view类实现成的对象  接收context的
	public TypeAdapter(ArrayList<Integer> types,Context context){
		this.types=types;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return types.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return types.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Integer resId=(Integer) getItem(position);
		View view=inflater.inflate(R.layout.type_gridview_item, null);
		ImageView iv_type=(ImageView) view.findViewById(R.id.iv_type_icon);
		iv_type.setImageResource(resId);
		return view;
	}

}
