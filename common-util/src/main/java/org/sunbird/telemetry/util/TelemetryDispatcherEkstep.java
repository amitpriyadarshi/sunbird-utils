package org.sunbird.telemetry.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sunbird.common.models.util.HttpUtil;
import org.sunbird.common.models.util.JsonKey;
import org.sunbird.common.models.util.LoggerEnum;
import org.sunbird.common.models.util.ProjectLogger;
import org.sunbird.common.models.util.PropertiesCache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by arvind on 9/1/18.
 */
public class TelemetryDispatcherEkstep implements TelemetryDispatcher {

	private static ObjectMapper mapper = new ObjectMapper();
	PropertiesCache propertiesCache = PropertiesCache.getInstance();

	@Override
	public boolean dispatchTelemetryEvent(List<String> eventList) {

		try {
			List<Map<String, Object>> jsonList = mapper.readValue(eventList.toString(),
					new TypeReference<List<Map<String, Object>>>() {
					});

			Map<String, Object> map = new HashMap<>();
			map.put("ets", System.currentTimeMillis());
			map.put(JsonKey.EVENTS, jsonList);
			ProjectLogger.log("Audit events count: " + jsonList.stream().filter(event -> "AUDIT".equals((String) event.get("eid"))).count(), LoggerEnum.INFO.name());
			String event = getTelemetryEvent(map);

			String response = HttpUtil.sendPostRequest(
					getCompleteUrl(JsonKey.EKSTEP_BASE_URL, JsonKey.EKSTEP_TELEMETRY_API_URL), event,
					getEkstepHeaders());
			ProjectLogger.log("Ekstep Telemetry flush response.", response, LoggerEnum.INFO.name());

		} catch (Exception ex) {
			ProjectLogger.log(ex.getMessage(), ex);
		}
		return false;
	}

	private static String getTelemetryEvent(Map<String, Object> map) {
		String event = "";
		try {
			event = mapper.writeValueAsString(map);
		} catch (Exception e) {
			ProjectLogger.log(e.getMessage(), e);
		}
		return event;
	}


	public String getCompleteUrl(String baseUrlKey , String uriKey){

		String baseSearchUrl = System.getenv(baseUrlKey);
		if (StringUtils.isBlank(baseSearchUrl)) {
			baseSearchUrl = propertiesCache.readProperty(baseUrlKey);
		}
		String uri = propertiesCache.readProperty(uriKey);
		return baseSearchUrl+uri;
	}

	public Map<String, String> getEkstepHeaders(){
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("accept", "application/json");
		headers.put(JsonKey.AUTHORIZATION, JsonKey.BEARER + System.getenv(JsonKey.EKSTEP_AUTHORIZATION));
		if (StringUtils.isBlank((String) headers.get(JsonKey.AUTHORIZATION))) {
			headers.put(JsonKey.AUTHORIZATION,
					PropertiesCache.getInstance().readProperty(JsonKey.EKSTEP_AUTHORIZATION));
		}
		return headers;
	}
}
