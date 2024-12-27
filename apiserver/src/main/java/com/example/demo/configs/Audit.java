package com.example.demo.configs;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;

@Slf4j(topic = "audit")
public class Audit {


	public static void info(String message) {
		log.info(message);
	}

	/**
	 * @param target 리소스, 사용자 등 해당 행위의 대상 정보
	 * @param message 로그 메시지
	 */
	private static void info(@NonNull Target target, String message) {
		ThreadContext.put("X-TARGET-TYPE", target.type);
		ThreadContext.put("X-TARGET-ID", target.id);
		ThreadContext.put("X-TARGET-NAME", target.name);
		log.info(message);
	}

	/**
	 * @param target 리소스, 사용자 등 해당 행위의 대상 정보
	 * @param action 행위 정보
	 * @param message 로그 메시지
	 */
	private static void info(@NonNull Target target, @NonNull Action action, String message) {
		ThreadContext.put("X-ACTION-TYPE", action.type);
		info(target, message);
	}

	/**
	 * 정상적인 api 요청의 흐름에 의하면 이미 로그에 사용자 정보가 포함되어 있을 것이므로, 별도로 사용자 정보를 넣을 필요가 없습니다.
	 * @param target 리소스, 사용자 등 해당 행위의 대상 정보
	 * @param action 행위 정보
	 * @param status 상태 정보
	 * @param message 로그 메시지
	 */
	public static void info(@NonNull Target target, @NonNull Action action, @NonNull Status status, String message) {
		ThreadContext.put("X-STATUS", status.code);
		info(target, action, message);
	}

	/**
	 * api 요청이 아닌 상태의 이벤트 로그를 남길 때 사용합니다.
	 * 해당 컨텍스트에서는 Actor 정보가 기록됩니다.
	 * 이미 Actor 정보가 컨텍스트에 포함되어 있을 경우, 해당 정보는 덮어씌워지므로 주의 바랍니다.
	 * @param target 리소스, 사용자 등 해당 행위의 대상 정보
	 * @param action 행위 정보
	 * @param status 상태 정보
	 * @param actor 행위를 한 주체 정보
	 * @param message 로그 메시지
	 */
	public static void info(@NonNull Target target, @NonNull Action action, @NonNull Status status, @NonNull Actor actor, String message) {
		ThreadContext.put("X-ACTOR-TYPE", actor.type);
		ThreadContext.put("X-ACTOR-ID", actor.id);
		ThreadContext.put("X-ACTOR-NAME", actor.name);
		ThreadContext.put("X-ACTOR-IP", actor.ip);
		info(target, action, status, message);
	}

	/*
        <!-- ACTION - 행위 정보입니다. -->
        <Property name="ACTION_TYPE_KEY">X-ACTION-TYPE</Property>
        <!-- TARGET - 대상 정보입니다. -->
        <Property name="TARGET_TYPE_KEY">X-TARGET-TYPE</Property>
        <Property name="TARGET_ID_KEY">X-TARGET-ID</Property>
        <Property name="TARGET_NAME_KEY">X-TARGET-NAME</Property>
        <!-- STATUS - 상태 정보입니다. -->
        <Property name="STATUS_KEY">X-STATUS</Property>
	 */
	public static class Target {
		public String type;
		public String id;
		public String name;

		public static Target from(String type, String id, String name) {
			Target target = new Target();
			target.type = type;
			target.id = id;
			target.name = name;
			return target;
		}
	}

	public static class Action {
		public String type;

		public static Action from(String type) {
			Action action = new Action();
			action.type = type;
			return action;
		}

	}

	public static class Status {
//		public String name;
		public String code;

		public static Status from(String code) {
			Status status = new Status();
			status.code = code;
			return status;
		}
	}

	public static class Actor {
		public String type;
		public String id;
		public String name;
		public String ip;

		public static Actor from(String type, String id, String name, String ip) {
			Actor actor = new Actor();
			actor.type = type;
			actor.id = id;
			actor.name = name;
			actor.ip = ip;
			return actor;
		}
	}
}
