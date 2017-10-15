package stream.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by maoxiangyi on 2016/4/26.
 */
public class NumberSpout extends BaseRichSpout {
    SpoutOutputCollector collector;
    Random random;
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("content"));
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        this.random = new Random();
    }

    @Override
    public void nextTuple() {
        String[] sentences = new String[]{"111 222 333 4444 555 666",
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666", "111 222 333 4444 555 666"};
        String sentence = sentences[random.nextInt(sentences.length)];
        String messageId = UUID.randomUUID().toString().replace("-", "");
        collector.emit(new Values(sentence), messageId);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
