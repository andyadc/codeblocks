package com.andyadc.codeblocks.framework.idgen.snowflake;

import com.andyadc.codeblocks.framework.idgen.IDGen;
import com.andyadc.codeblocks.framework.idgen.Result;
import com.andyadc.codeblocks.framework.idgen.Status;
import com.andyadc.codeblocks.kit.net.IPUtil;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * andy.an
 */
public class SnowflakeIDGenImpl implements IDGen {

    private static final Logger logger = LoggerFactory.getLogger(SnowflakeIDGenImpl.class);

	private static final long twepoch = 1288834974657L;
	private static final long workerIdBits = 10L;
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);//最大能够分配的workerid =1023
	private static final long sequenceBits = 12L;
	private static final long workerIdShift = sequenceBits;
	private static final long timestampLeftShift = sequenceBits + workerIdBits;
	private static final long sequenceMask = -1L ^ (-1L << sequenceBits);
    public boolean initFlag = false;
    private long workerId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    /**
     * @param workerId workerId
     */
    public SnowflakeIDGenImpl(long workerId) {
        this.workerId = workerId;
    }

    /**
     * @param zkAddress zookeeper servers
     * @param port      local server port
     */
    public SnowflakeIDGenImpl(String zkAddress, int port) {
        SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(IPUtil.getLocalIp(), port + "", zkAddress);
        initFlag = holder.init();
        if (initFlag) {
            workerId = holder.getWorkerID();
            logger.info("START SUCCESS USE ZK WORKERID-{}", workerId);
        } else {
            throw new IllegalArgumentException("Snowflake Id Gen is not init ok");
        }
        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerID must gte 0 and lte 1023");
    }

    @Override
    public synchronized Result gen(String key) {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        return new Result(-1 + "", Status.EXCEPTION);
                    }
                } catch (InterruptedException e) {
                    logger.error("wait interrupted");
                    return new Result(-2 + "", Status.EXCEPTION);
                }
            } else {
                return new Result(-3 + "", Status.EXCEPTION);
            }
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                //seq 为0的时候表示是下一毫秒时间开始对seq做随机
                sequence = ThreadLocalRandom.current().nextInt(100);
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //如果是新的ms开始
            sequence = ThreadLocalRandom.current().nextInt(100);
        }
        lastTimestamp = timestamp;
        long id = ((timestamp - twepoch) << timestampLeftShift) | (workerId << workerIdShift) | sequence;
        return new Result(id + "", Status.SUCCESS);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public boolean init() {
        return true;
    }
}
