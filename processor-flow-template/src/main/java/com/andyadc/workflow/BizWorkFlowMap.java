package com.andyadc.workflow;

import com.andyadc.workflow.processor.BizProcessorProxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BizWorkFlowMap extends HashMap<String, BizProcessorProxy> {

	private static final long serialVersionUID = -6258217608107409511L;

	private final Map<String, BizProcessorProxy> workFlowMap = new HashMap<>();
	private List<Map<String, BizProcessorProxy>> parentWorkFlowList = new ArrayList<>();

	public BizWorkFlowMap(Map<String, BizProcessorProxy> map) {
		super(map);
		workFlowMap.putAll(map);
	}

	public Map<String, BizProcessorProxy> getWorkFlowMap() {
		return workFlowMap;
	}

	public void setParentWorkFlowList(List<Map<String, BizProcessorProxy>> parentWorkFlowList) {
		this.parentWorkFlowList = parentWorkFlowList;
		if (this.parentWorkFlowList != null && this.parentWorkFlowList.size() > 0) {
			for (Map<String, BizProcessorProxy> parentWorkFlowMap : this.parentWorkFlowList) {
				for (Entry<String, BizProcessorProxy> parentWorkFlowEntry : parentWorkFlowMap.entrySet()) {
					if (workFlowMap.containsKey(parentWorkFlowEntry.getKey())) {
						throw new RuntimeException("WorkFlow duplicated. key: [" + parentWorkFlowEntry.getKey() + "]");
					}
					this.workFlowMap.put(parentWorkFlowEntry.getKey(), parentWorkFlowEntry.getValue());
				}
			}
		}
	}
}
