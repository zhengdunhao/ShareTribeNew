package com.atm.chatonline.chat.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atm.chatonline.bbs.commom.CircleImageView;
import com.atm.chatonline.chat.info.ChatMessage;
import com.atm.chatonline.chat.util.Config;
import com.atm.chatonline.chat.util.ExpressionUtil;
import com.atm.chatonline.chat.util.FileUtil;
import com.atm.chatonline.chat.util.TimeUtil;
import com.example.studentsystem01.R;




public class ChatAdapter extends BaseAdapter{

	protected static final String tag = "ChattingAdapter";
	private Context context;
	private List<ChatMessage> chatMessages;
	private String lastTime="";
	

	public ChatAdapter(Context context, List<ChatMessage> messages) {
		super();
		this.context = context;
		this.chatMessages = messages;
	}

	public int getCount() {
		return chatMessages.size();
	}

	public Object getItem(int position) {
		return chatMessages.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		ChatMessage message = chatMessages.get(position);
		int direction=message.getDirection();
		int type=message.getType();
		if (convertView == null
				|| (holder = (ViewHolder) convertView.getTag()).flag != message
						.getDirection()) {
			holder = new ViewHolder();
			if (direction == Config.MESSAGE_FROM) {
				if(type==Config.MESSAGE_TEXT){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatfrom_list_view, null);
				}else if(type==Config.MESSAGE_IMG){
					convertView = LayoutInflater.from(context).inflate(R.layout.chatfrom_list_img, null);
				}
			} else if(direction == Config.MESSAGE_TO){
				if(type==Config.MESSAGE_TEXT){
				convertView = LayoutInflater.from(context).inflate(
						R.layout.chatto_list_view, null);
				}else if(type==Config.MESSAGE_IMG){
					convertView = LayoutInflater.from(context).inflate(R.layout.chatto_list_img, null);
				}
			}
			
			
			convertView.setTag(holder);
			holder.head=(CircleImageView)convertView.findViewById(R.id.chat_head_pic);
			holder.time=(TextView)convertView
					.findViewById(R.id.chat_time);//������ʾ����
			
			holder.text = (TextView) convertView
					.findViewById(R.id.chat_content);//������ʾʱ��
			
			holder.img = (ImageView)convertView.findViewById(R.id.chat_img);//���췢��ͼƬ
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		String time=message.getTime();
		
		
//		Log.i(TAG, "time="+time);
		if(time!=null && !"".equals(time)){
			String content=message.getContent();
			if(message.equals(chatMessages.get(0))){
				Log.i(tag, "chatMessage�ǵ�һ�����ҵ�������:"+content);
				String relativeTime=TimeUtil.compareTime(time,time,1);
				holder.time.setText(relativeTime);
				Log.i(tag, "��Ҫ��ʾʱ��:");
				Log.i(tag, "chatMessage�ǵ�һ��������Զ�ߵ���1");
				Log.i(tag, "relativeTime="+relativeTime+" ʱ��");
			}else{
					lastTime=chatMessages.get(chatMessages.size()-2).getTime();
					Log.i(tag, "chatMessage���ǵ�һ��,�ҵ�������:"+content);
					String relativeTime=TimeUtil.compareTime(time,lastTime,message.getShowTime());
					
					Log.i(tag, "relativeTime="+relativeTime+" ʱ��");
					holder.time.setText(relativeTime);
					if(message.getShowTime()==0){
					if(relativeTime.equals("")){
						message.setShowTime(-1);
						Log.i(tag, "chatMessage���ǵ�һ��������Զ�ߵ���-1");
					}
					else{
						message.setShowTime(1);
						Log.i(tag, "chatMessage���ǵ�һ��������Զ�ߵ���1");
					}
					}else{
						Log.i(tag, "�ҵ�������:"+content+"�ҽ��������������ߣ��ҵ�showTime��:"+message.getShowTime());
					}
					
				
			}
		}
		Log.i(tag, "ChatAdapter-----���ڱ仯");
		Log.i(tag, "��Ҫ��ʾ����:"+message.getContent());
		if(direction==Config.MESSAGE_FROM){
		holder.head.setImageBitmap(FileUtil.getBitmap(FileUtil.APP_PATH+"/friend"+"/"+message.getSelfID()+"/"+message.getFriendID()+".jpg"));
		Log.i(tag, "��ȡ"+message.getFriendID()+"��ͷ��");
		}else{
			holder.head.setImageBitmap(FileUtil.getBitmap(Environment.getExternalStorageDirectory()+"/ATM/userhead/"+message.getSelfID()+".jpg"));
			Log.i(tag, "��ȡ"+message.getSelfID()+"��ͷ��");
		}
		
		//�ж�message �� type ���Ӷ���������ʾ���ֻ���ͼƬ
		if(type==Config.MESSAGE_TEXT){
		String str = message.getContent();														//��Ϣ��������
		Log.i(tag, "str:::"+str);
		String zhengze = "\\#(exp\\d{2})";											//������ʽ�������ж���Ϣ���Ƿ��б���
		SpannableString spannableString = ExpressionUtil.getExpressionString(context, str, zhengze);//�ڷ�������Ϳ�ʼ���������ֺ�������ʽ�ȶ�
		
		holder.text.setText(spannableString);
		}else if(type==Config.MESSAGE_IMG){
			String filePath = message.getContent();
			try{
				Bitmap bitmap = getSmallPic(filePath,100);
				holder.img.setLayoutParams(new LinearLayout.LayoutParams(180,240));
				holder.img.setScaleType(ScaleType.FIT_XY);
				holder.img.setImageBitmap(bitmap);
			}catch(FileNotFoundException e){
				
			}
		}
		return convertView;
	}
	
	private Bitmap getSmallPic(String filePath,int width)throws FileNotFoundException{
		BitmapFactory.Options options = new BitmapFactory.Options();
		//�����ʹ��һ��BitmapFactory.Options���󣬲��Ѹö����inJustDecodeBounds��������Ϊtrue��
		//decodeResource()�����Ͳ�������Bitmap���󣬶������Ƕ�ȡ��ͼƬ�ĳߴ��������Ϣ��
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int be = (int)(options.outWidth/(float)width);
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}

	// �Ż�listview��Adapter
	static class ViewHolder {
		CircleImageView head;
		
		TextView time;
		TextView text;
		ImageView img;
		int flag;
	}
	

}
