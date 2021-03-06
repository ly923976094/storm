package cn.itcast.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
  <dependencies>
        <dependency>
            <groupId>org.apache.storm</groupId>
            <artifactId>storm-core</artifactId>
            <version>0.9.3</version>
        </dependency>
    </dependencies>
 * Created by maoxiangyi on 2016/4/27.
 */
public class WordCountTopologMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        //1????????TopologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout",new MySpout(),2);
        topologyBuilder.setBolt("mybolt1",new MySplitBolt(),2).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("mybolt2",new MyCountBolt(),4).fieldsGrouping("mybolt1", new Fields("word"));
//        topologyBuilder.setBolt("mybolt2",new MyCountBolt(),4).shuffleGrouping("mybolt1");
        //  config.setNumWorkers(2);
        /**
         * i
         * am
         * lilei
         * love
         * hanmeimei
         */


        //2?????????configuration????????????topology ?????worker??????
        Config config =  new Config();
        config.setNumWorkers(2);

        //3????????  -----?????? ????????????
//        StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
