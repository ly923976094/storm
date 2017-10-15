package stream;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import stream.bolt.SaveDataBolt;
import stream.bolt.SplitBolt;
import stream.spout.NumberSpout;
import stream.spout.SignSpout;
import stream.spout.StringSpout;
import stream.streamBolt.NumberStreamBolt;
import stream.streamBolt.SignStreamBolt;
import stream.streamBolt.StringStreamBolt;

/**
 * Created by maoxiangyi on 2016/4/26.
 */
public class MyStreamMain {

    public static void main(String[] args) throws Exception{
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        //set spouts ��
        topologyBuilder.setSpout("NumberSpout", new NumberSpout(), 1);
        topologyBuilder.setSpout("StringSpout", new StringSpout(), 1);
        topologyBuilder.setSpout("SignSpout", new SignSpout(), 1);

        //set bolts ��
        topologyBuilder.setBolt("SplitBolt", new SplitBolt(), 1)
                .shuffleGrouping("NumberSpout")
                .shuffleGrouping("StringSpout")
                .shuffleGrouping("SignSpout");

        //set bolts ��
        topologyBuilder.setBolt("StringStreamBolt", new StringStreamBolt(), 1).shuffleGrouping("SplitBolt", "string-stream");
        topologyBuilder.setBolt("NumberStreamBolt", new NumberStreamBolt(), 1).shuffleGrouping("SplitBolt", "number-stream");
        topologyBuilder.setBolt("SignStreamBolt", new SignStreamBolt(), 1).shuffleGrouping("SplitBolt", "sign-stream");

        //set bolt ��
        topologyBuilder.setBolt("SaveDataBolt", new SaveDataBolt(), 3)
                .fieldsGrouping("StringStreamBolt", new Fields("type"))
                .fieldsGrouping("NumberStreamBolt", new Fields("type"))
                .fieldsGrouping("SignStreamBolt", new Fields("type"));

        Config conf = new Config();
        String name = MyStreamMain.class.getSimpleName();
        if (args != null && args.length > 0) {
            String nimbus = args[0];
            conf.put(Config.NIMBUS_HOST, nimbus);
            conf.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(name, conf, topologyBuilder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, topologyBuilder.createTopology());
            Thread.sleep(60 * 60 * 1000);
            cluster.shutdown();
        }
    }
}
