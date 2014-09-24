package com.forhs.equipmentinspection.activity;


import com.forhs.equipmentinspection.R;
import com.forhs.equipmentinspection.activity.InspectionActivity;
import com.forhs.equipmentinspection.activity.SettingActivity;


import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.app.TabActivity;

import android.content.Intent;

public class MainActivity extends TabActivity {

	//donghao.li
	private TabHost tabhost;
	private TabHost.TabSpec first;
	private TabHost.TabSpec second;
	private TabHost.TabSpec third;
	private TabHost.TabSpec fourth;

	private long exitTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void init() {
		tabhost = this.getTabHost();

		first = tabhost.newTabSpec("first");
		second = tabhost.newTabSpec("second");		
		
		// ָ��ѡ������֣�ͼ��
		first.setIndicator(createContent("���", R.drawable.first_tab));
		second.setIndicator(createContent("����", R.drawable.two_tab));
		
		// ����ʾ��ҳ��
		first.setContent(new Intent(this, InspectionActivity.class));
		second.setContent(new Intent(this, SettingActivity.class));
		
		// ��ѡ��ӽ�TabHost
		tabhost.addTab(first);
		tabhost.addTab(second);
		//�趨��ǰTab
		tabhost.setCurrentTab(0);
		// ����tabHost�л�ʱ��̬����ͼ��
		tabhost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				tabChanged(tabId);
			}

		});
		
	}
	
	// ����tab�仯�¼�
	private void tabChanged(String tabId) {
			// ��ǰѡ����
			if (tabId.equals("first")) {
				tabhost.setCurrentTabByTag("���");
			} else if (tabId.equals("second")) {
				tabhost.setCurrentTabByTag("����");
			}
	}
	
	// ����ѡ��ϵĲ�������
	// ���ص���ѡ��
	private View createContent(String text, int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabwidget, null,
				false);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		ImageView iv_icon = (ImageView) view.findViewById(R.id.img_name);
		tv_name.setText(text);
		iv_icon.setBackgroundResource(resid);
		return view;
	}
	
	//��д���������¼�������Ǻ��ˣ����β��˳�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}

	}
}
