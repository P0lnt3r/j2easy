package zy.pointer.j2easy.business.commons;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LockPoolHelper {
	
	private static Set<String> lockPool = new HashSet<String>();
	
	public static void lock(String[] keys,int retryTime){
		// 快速检查
		boolean quickCheck = true;
		quick : do{
			for(String key:keys){
				quickCheck = ! lockPool.contains(key);
				if(!quickCheck){
					try {
						Thread.sleep(retryTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue quick;
				}
			}
			
			// 上锁检查
			boolean lockCheck = true;
			synchronized (LockPoolHelper.class) {
				for(String key:keys){
					lockCheck = ! lockPool.contains(key);
					if(!lockCheck){
						continue quick;
					}
				}
				// 在上锁的基础上进行数据注册
				for(String key:keys){
					lockPool.add(key);
				}
				break quick;
			}
		}while(true);
	}
	
	public static void unlock(String[] keys){
		synchronized (LockPoolHelper.class) {
			for(String key:keys){
				lockPool.remove(key);
			}
		}
	}
	
	public static void lock(String key,int retryTime){
		boolean success = false;
		do{
			success = doLock(key);
			if(!success){
				try {
					Thread.sleep(retryTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}while(!success);
	}
	
	private static boolean doLock(String key){
		synchronized (LockPoolHelper.class) {
			if(lockPool.contains(key)){
				return false;
			}
			lockPool.add(key);
			return true;
		}
	}
	
	public static void unlock(String key){
		synchronized (LockPoolHelper.class) {
			lockPool.remove(key);
		}
	}
	
	
	public static Integer count = 0;
	
	public static void main(String[] args) {
		
		new Thread(){
			
			public void run() {
				
				int n = 0;
				
				while(true){
					int release = 100 + n;
					LockPoolHelper.lock("A", 50);
					try {
						Thread.sleep(500);
						System.out.println("===");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					LockPoolHelper.unlock("A");
					try {
						Thread.sleep(release);
						System.out.println("===");
						n++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			};
			
		}.start();
		
		for(int i = 1;i<=1000;i++){
			new Thread(){
				public void run() {
					LockPoolHelper.lock(new String[]{"A","B","C"}, 10);
					System.out.println( ++ count);
					LockPoolHelper.unlock(new String[]{"A","B","C"});
				};
			}.start();
		}
	}
	
	
}
