package com.atm.chatonline.bbs.commom;

import com.example.studentsystem01.R;

/**
 * ���ڴ洢ϵ��ѧУ��רҵ�ļ���
 * 2015.7.24��atm--��
 */


public class RegisterSet {
		//����ѧУϵ�𼯺�
		final int[] department={
				R.array.jr_school_dept
		};
		//����ѧԺ��ϵ��רҵ����
		final int[] countofJr={R.array.jr_it_major,R.array.jr_finance_major,R.array.jr_insurance_major,R.array.jr_economic_trade_major,R.array.jr_business_administration_major,
				R.array.jr_foreign_major,R.array.jr_law_major,R.array.jr_financial_media_major,R.array.jr_applied_mathematics_major,R.array.jr_public_administration_major
				};
		//��ҵ��ѧ��ϵ��רҵ����
//		final int[] countofGy={R.array.gy_jd_major};
		public int[] getDepartment() {
			return department;
		}
		public int[] getCountofjr() {
			return countofJr;
		}
//		public int[] getCountofgy() {
//			return countofGy;
//		}
		
}
