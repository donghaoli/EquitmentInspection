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
		
		// 指定选项卡上文字，图标
		first.setIndicator(createContent("点检", R.drawable.first_tab));
		second.setIndicator(createContent("设置", R.drawable.two_tab));
		
		// 绑定显示的页面
		first.setContent(new Intent(this, InspectionActivity.class));
		second.setContent(new Intent(this, SettingActivity.class));
		
		// 将选项卡加进TabHost
		tabhost.addTab(first);
		tabhost.addTab(second);
		//设定当前Tab
		tabhost.setCurrentTab(0);
		// 设置tabHost切换时动态更改图标
		tabhost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				tabChanged(tabId);
			}

		});
		
	}
	
	// 捕获tab变化事件
	private void tabChanged(String tabId) {
			// 当前选中项
			if (tabId.equals("first")) {
				tabhost.setCurrentTabByTag("点检");
			} else if (tabId.equals("second")) {
				tabhost.setCurrentTabByTag("设置");
			}
	}
	
	// 设置选项卡上的布局内容
	// 返回单个选项
	private View createContent(String text, int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.tabwidget, null,
				false);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		ImageView iv_icon = (ImageView) view.findViewById(R.id.img_name);
		tv_name.setText(text);
		iv_icon.setBackgroundResource(resid);
		return view;
	}
	
	//重写按键按下事件，如果是后退，两次才退出
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
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}

	}
}
