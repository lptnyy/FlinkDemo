package com.fasterar.smart.server.flink.entity.input;

import java.util.List;
import lombok.Data;

@Data
public class RelationshipItem {
  String rootId;
  List<RelationshipNode> nodes;
  List<RelationshipLink> links;
}
