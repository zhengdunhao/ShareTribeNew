package com.atm.chatonline.bbs.commom;
/*
 * �������� ��ť������ĵ���ʱ״̬
 * 2015.7.26,atm--С��
 */
import android.os.CountDownTimer;
import android.widget.Button;


public class CountDownUtil {
		private TimeCount time;
		private Button but = null;
		
		//����һ�����εĹ��캯�����ò���ΪButton����
		public CountDownUtil(Button but){
			this.but = but;
		}
		
		public void action(){
			time = new TimeCount(60000,1000);//��ʱ��Ϊ60�룬��ʱ��ʱ����Ϊ1��
			time.start();//��ʼ��ʱ
			
		}

		/*����һ������ʱ���ڲ���*/
		class TimeCount extends CountDownTimer{
			//��д��������
			public TimeCount(long millisInFuture, long countDownInterval) {
				super(millisInFuture, countDownInterval);
				// TODO Auto-generated constructor stub
				//����һ��Ϊ��ʱ�����ͼ�ʱ��ʱ����
			}

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				//��ʱ������ʾ
				but.setClickable(false);//��ʱ���̰�ť�޷��ٴ���
				but.setText(millisUntilFinished/1000+"��");
				
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				//��ʱ���ʱ����
				but.setText("������֤");
				but.setClickable(true);
			}
			
		}
}
