import java.util.Queue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTest {

	public static void main(String[] args) {
		Queue<DelayTask> queue = new DelayQueue<>();
		for(int i = 0 ; i < 10 ; i++) {
			queue.add(new DelayTask(i * 1000, "延时任务" + i));
		}
		
		while(queue.size() > 0) {
			DelayTask task = queue.poll();
			if(task != null) {
				task.execute();
			}
		}

	}

}


class DelayTask implements Delayed {
	private long delayTime;
	private long expireTime;
	private String message;
	
	public DelayTask(long delayTime, String message) {
		this.delayTime = delayTime;
		this.expireTime = this.delayTime + System.currentTimeMillis();
		this.message = message;
	}
	@Override
	public int compareTo(Delayed o) {
		if(this.getDelay(TimeUnit.NANOSECONDS) < o.getDelay(TimeUnit.NANOSECONDS)) {
			return -1;
		}else if(this.getDelay(TimeUnit.NANOSECONDS) == o.getDelay(TimeUnit.NANOSECONDS)) {
			return 0;
		}else {
			return 1;
		}
		
	}

	public void execute() {
		System.out.println(message);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		
		return this.expireTime - System.currentTimeMillis();
	}
	
}