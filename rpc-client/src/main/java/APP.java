
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.rpc.client.RPC.RPC;
import com.rpc.client.pojo.Person;
import com.rpc.client.proxy.ProxyFactory;
import com.rpc.service.AppleService;
import com.rpc.service.PersonService;
import com.rpc.service.UserService;

public class APP {

	public static void main(String[] args) throws Exception {
		/**RPC.init("C:\\Users\\zyldo\\Desktop\\RPC\\rpc-client\\src\\main\\resources\\conf\\client.xml");
		UserService userService = ProxyFactory.create(UserService.class, "userService", RPC.serviceList.get("userService").getClassName());
		AppleService appleService = ProxyFactory.create(AppleService.class, "appleService", RPC.serviceList.get("appleService").getClassName());
		PersonService personService = ProxyFactory.create(PersonService.class, "personService", RPC.serviceList.get("personService").getClassName());
		Person person = new Person();
		person.setName("zhao");
		personService.say(person);
		**/
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		//lock.readLock().lock();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				lock.writeLock().lock();
				
			}
		}).start();
	}

}

