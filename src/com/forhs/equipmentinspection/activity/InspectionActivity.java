package com.forhs.equipmentinspection.activity;

import java.util.ArrayList;

import com.forhs.equipmentinspection.R;

import com.forhs.equipmentinspection.adapter.TypeAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class InspectionActivity extends Activity {
	private long exitTime = 0;
	private ImageView iv_map; //ͷ�������ĵ�ͼ
	private ImageView iv_search; //ͷ�������ĵ�ͼ
	private ScrollView sc; //������ͼ
	private LinearLayout globleLayout; //��������岼��
	private LayoutInflater inflater; // ���ּ�����
	private LinearLayout header;// ͷ������
	private int headerHeight; // ͷ�߶�
	private int lastHeaderPadding; // ���һ�ε���Move Header��Padding
	private TextView tv_text;//ͷ�����ֵ�TextView
	private ImageView iv_header_fresh_anim; //ͷ�����ֶ�����imageView ˢ��
	private AnimationDrawable ad;// ˢ�µĶ���
	private Animation anim; // ͷ������
	private ImageView iv_anim_first; //����Ӧ�õ���ImageView�ؼ�
	
	private int headerState = DONE; // ͷ��״̬
	static final private int RELEASE_To_REFRESH = 0; // �ͷ�ˢ��:һֱ������Ļʱ��ʾ
	static final private int PULL_To_REFRESH = 1; // ����ˢ�£��ſ���Ļ����ʾ
	static final private int REFRESHING = 2; // ����ˢ��
	static final private int DONE = 3; //��ʼ״̬
	
	private boolean isBack; // ��Release ת�� pull
	
	private GridView gv_type; //���
	private ArrayList<Integer> types; //��ӵ�GridView��
	private TypeAdapter typeAdapter; //����������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspection);
		initView();//��ʼ�����ؼ�
		initCategory();//��ʼ������
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * �˳�
	 */
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
	
	/**
	 * ��ʼ�����ؼ�
	 */
	public void initView() {
		// ��ͷ��������ImageView���õ���¼�����
		iv_map = (ImageView) findViewById(R.id.iv_map);
		iv_search = (ImageView)findViewById(R.id.iv_search);
		
		// ScrollView
		sc = (ScrollView) findViewById(R.id.sv_first_sc);
		// ���岼��
		globleLayout = (LinearLayout) findViewById(R.id.globleLayout);
		// ���ּ�����
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// ͷ������
		header = (LinearLayout) inflater.inflate(R.layout.first_header, null);
		tv_text = (TextView) header.findViewById(R.id.tv_first_refresh_text);
		iv_header_fresh_anim = (ImageView) header
				.findViewById(R.id.iv_header_anim);
		iv_header_fresh_anim.setBackgroundResource(R.drawable.frame);
		ad = (AnimationDrawable) iv_header_fresh_anim.getBackground();
		// ͷ������
		anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
		// ����Ӧ�õ��Ŀؼ�
		iv_anim_first = (ImageView) header.findViewById(R.id.iv_first_refresh);
		// ����ͷ���߶�
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		lastHeaderPadding = (-1 * headerHeight);
		header.setPadding(10, lastHeaderPadding, 0, 20);
		header.invalidate();
		// ���ͷ������
		globleLayout.addView(header, 0);
		anim.setFillAfter(true);// ���������󱣳ֶ���
		// ΪScrollView�󶨼���
		sc.setOnTouchListener(new OnTouchListener() {
			private int beginY; //Y�����

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					/**
					 * sc.getScrollY == 0 ScrollView ������ͷ�� lastHeaderPadding >
					 * (-1*headerHeight) ��ʾheader��û��ȫ��������ʱ headerState !=
					 * REFRESHING����ˢ��ʱ
					 */
					if ((sc.getScrollY() == 0 || lastHeaderPadding > (-1 * headerHeight))
							&& headerState != REFRESHING) {
						// �õ�������Y�����
						int interval = (int) (event.getY() - beginY);
						// �����»������������ϻ���
						if (interval > 0) {
							interval = interval / 2;// �»�����
							lastHeaderPadding = interval + (-1 * headerHeight);
							header.setPadding(10, lastHeaderPadding, 0, 20);
							if (lastHeaderPadding > 0) {
								// txView.setText("��Ҫˢ�¿�");
								headerState = RELEASE_To_REFRESH;
								// �Ƿ��Ѿ�������UI
								if (!isBack) {
									isBack = true; // ����Release״̬��������ػ�������pull����������
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
								// txView.setText("��������Ŷ");
								// sc.scrollTo(0, headerPadding);
							}
						}
					}
					break;
				case MotionEvent.ACTION_DOWN:
					// �����»�������ʵ�ʻ�������Ĳ���ֵ��
					beginY = (int) ((int) event.getY() + sc.getScrollY() * 1.5);
					break;
				case MotionEvent.ACTION_UP:
					if (headerState != REFRESHING) {
						switch (headerState) {
						case DONE:
							// ʲôҲ����
							break;
						case PULL_To_REFRESH:
							headerState = DONE;
							lastHeaderPadding = -1 * headerHeight;
							header.setPadding(10, lastHeaderPadding, 0, 0);
							changeHeaderViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; // ׼����ʼˢ�£���ʱ���������ػ���
							headerState = REFRESHING;
							changeHeaderViewByState();
							onRefresh();
							break;
						default:
							break;
						}
					}
					break;
				}
				// ���Header����ȫ�����ص�����ScrollView�������������¼���������Ļ�������¼�
				if (lastHeaderPadding > (-1 * headerHeight)
						&& headerState != REFRESHING) {
					return true;
				} else {
					return false;
				}

			}

		});
	}
	/**
	 * �õ�ͷ���߶�,onCreate����ò���
	 */
	private void measureView(View childView) {
		LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
	}
	/**
	 * ��ˢ��
	 */
	private void onRefresh() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				onRefreshComplete();
			}

		}.execute();
	}
	/**
	 * ˢ�����
	 */
	public void onRefreshComplete() {
		headerState = DONE;
		changeHeaderViewByState();
	}
	/**
	 * ͨ��״̬���ı�ͷ����ͼ
	 */
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) { // ������
				isBack = false;
				// ��������
				iv_anim_first.startAnimation(anim);
				ad.start();
				tv_text.setText("����ˢ��");
			}
			tv_text.setText("����ˢ��");
			break;
		case RELEASE_To_REFRESH: // �����ϣ�����ֻ���ұߵĽ��ȶ���
			iv_anim_first.setVisibility(View.VISIBLE);
			iv_header_fresh_anim.setVisibility(View.VISIBLE);
			tv_text.setVisibility(View.VISIBLE);
			iv_anim_first.startAnimation(anim); // �ұߵĽ��ȶ���
			tv_text.setText("����ˢ��");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			header.setPadding(10, lastHeaderPadding, 0, 20);
			header.invalidate();
			iv_header_fresh_anim.setVisibility(View.VISIBLE);
			iv_anim_first.setVisibility(View.VISIBLE);
			tv_text.setText("������...");
			ad.start();
			break;
		case DONE: // ������
			lastHeaderPadding = -1 * headerHeight;
			header.setPadding(10, lastHeaderPadding, 0, 20);
			header.invalidate();
			iv_header_fresh_anim.setVisibility(View.GONE);
			tv_text.setText("����ˢ��");
			break;
		default:
			break;
		}
	}
	
	/**
	 * ��ʼ������
	 */
	public void initCategory() {
		gv_type = (GridView) findViewById(R.id.gv_type);

		types = new ArrayList<Integer>();
		types.add(R.drawable.dianjian_bv);
		types.add(R.drawable.setting_bv);		

		typeAdapter = new TypeAdapter(types, this);

		gv_type.setAdapter(typeAdapter);
		gv_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent food = new Intent(InspectionActivity.this,
							MachineryRoomActivity.class);
					startActivity(food);
					break;
				case 1:
					Intent movie = new Intent(InspectionActivity.this,
							MachineryRoomActivity.class);
					startActivity(movie);
					break;
				default:
					break;
				}

			}
		});
	}
}
