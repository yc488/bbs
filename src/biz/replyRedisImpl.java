package biz;

import bean.Reply;
import redis.clients.jedis.Jedis;

public class replyRedisImpl {
	//����
	@SuppressWarnings("resource")
	public Long glkreply(Reply reply) {
		Jedis jedis = new Jedis("localhost", 6379);
		//�ж�redids���Ƿ���  1�Ƿ��ڼ� Ϊtopic1��. 
		if (jedis.sismember("topic" + reply.getTopicid(), reply.getUid() + "") == false) {// �ж�ֵ�Ƿ�����ڼ���
			jedis.sadd("topic" + reply.getTopicid(), reply.getUid() + "");
		} else {
			jedis.smove("topic" + reply.getTopicid(), "trash", reply.getUid() + "");// ��ֵ�Ӽ���ȡ��
																					// ������һ����
		}
		Long glk = jedis.scard("topic" + reply.getTopicid());
		return glk;
	}
	//��ȡĳ�����ӵĵ�����
	@SuppressWarnings("resource")
	public Long gettimes(Reply reply) {
		Jedis jedis = new Jedis("localhost", 6379);
		Long glk = jedis.scard("topic" + reply.getTopicid());
		return glk;
	}
}
