package com.example.wanghf.smartwaistcoat.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by bss on 2016/2/17.
 */
public abstract class BroadcastUtil {
    private static final String TAG = "BroadcastUtil";

    public static final String ACTION_MAP = "org.gauto.media.ACTION_MAP";
    public static final String ACTION_VIDEO = "org.gauto.media.ACTION_VIDEO";
    public static final String ACTION_TRACK = "org.gauto.media.ACTION_TRACK";
    public static final String ACTION_TRACK_PLAY = "org.gauto.render.ACTION_TRACK";
    public static final String ACTION_SIMULATE_TRACK = "org.gauto.media.ACTION_SIMULATE_TRACK";
    public static final String ACTION_AS_LINE = "org.gauto.render.ACTION_AS_LINE";
    public static final String ACTION_TRACK_PREDICT = "org.gauto.render.ACTION_TRACK_PREDICT";
    public static final String ACTION_GENERAL_LINES = "org.gauto.render.ACTION_GENERAL_LINES";
    public static final String ACTION_LINE_MARK = "org.gauto.render.ACTION_LINE_MARK";
    public static final String ACTION_EXAM_CHANGE = "org.gauto.render.ACTION_EXAM_CHANGE";
    public static final String ACTION_ROAD_CHANGE = "org.gauto.render.ACTION_ROAD_CHANGE";
    public static final String ACTION_CLEAR_COLLISION_LINE = "org.gauto.render.ACTION_CLEAR_COLLISION_LINE";
    public static final String ACTION_COLLISION_LINE = "org.gauto.render.ACTION_COLLISION_LINE";
    public static final String ACTION_PORTRAIT = "org.gauto.render.ACTION_PORTRAIT";
    public static final String ACTION_NPC_CAR = "org.gauto.render.ACTION_NPC_CAR";
    public static final String ACTION_CAR_MARK = "org.gauto.render.ACTION_CAR_MARK";
    public static final String ACTION_ARROW_TO_BEGIN = "org.gauto.render.ACTION_ARROW_TO_BEGIN";
    public static final String ACTION_CLEAR_ARROW_TO_BEGIN = "org.gauto.render.ACTION_CLEAR_ARROW_TO_BEGIN";
    public static final String ACTION_SHOW_DISTANCE = "org.gauto.media.ACTION_SHOW_DISTANCE";
    public static final String ACTION_SHOW_DAOKU_ARROW = "org.gauto.media.ACTION_SHOW_DAOKU_ARROW";
    public static final String ACTION_SHOW_CORRECTION_LEVEL = "org.gauto.media.ACTION_SHOW_CORRECTION_LEVEL";
    public static final String ACTION_ZOOM_MAP = "org.gauto.media.ZOOM_MAP";
    public static final String ACTION_POST_RECORD = "org.gauto.media.POST_RECORD";
    public static final String ACTION_FINISH_UPDATE_RESOURCE = "org.gauto.media.FINISH_UPDATE_RESOURCE";
    public static final String ACTION_FINISH_CONFIG_OBD = "org.gauto.media.FINISH_CONFIG_OBD";
    public static final String ACTION_UPDATE_SUBJECT_NAME = "cc.gauto.assertion.UPDATE_SUBJECT_NAME";
    public static final String ACTION_PATH_PLANNING = "cc.gauto.assertion.PATH_PLANNING";
    public static final String ACTION_UNITY_JSON = "cc.gauto.render.UNITY_JSON";
    public static final String ACTION_LEARN_VEHICLE = "cc.gauto.assertion.LEARN_VEHICLE";
    public static final String ACTION_LEARN_VEHICLE_FINISH = "cc.gauto.subject2.LEARN_VEHICLE_FINISH";
    public static final String ACTION_SPEED_FOR_DISPLAY = "cc.gauto.assertion.SPEED_FOR_DISPLAY";
    public static final String ACTION_ORIGIN_POSITION = "cc.gauto.assertion.ORIGIN_POSITION";
    public static final String ACITON_CLEAR_ORIGIN_POSITION = "cc.gauto.assertion.CLEAR_ORIGIN_POSITION";
    public static final String ACITON_BRAKE_WORK_OFF = "cc.gauto.assertion.BRAKE_WORK_OFF";
    public static final String ACTION_SUBJECT_CHANGED = "cc.gauto.assertion.SUBJECT_CHANGED";
    public static final String ACTION_SIMULATION_BEGIN_AT_BEGIN = "cc.gauto.assertion.SIMULATION_BEGIN_AT_BEGIN";
    public static final String ACTION_QUIT = "cc.gauto.ACTION_QUIT";

    private BroadcastUtil() {
        throw new AssertionError();
    }

    /**
     * 显示实时地图
     */
    public static void showMap(Context context) {
        Intent intent = new Intent(ACTION_MAP);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 播放视频
     */
    public static void playVideo(Context context, String name, boolean loop) {
        Intent intent = new Intent(ACTION_VIDEO);
        intent.putExtra("VIDEO_NAME", name);
        intent.putExtra("LOOP", loop);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 刹车解除
     */
    public static void brakeWorkOff(Context context) {
        Intent intent = new Intent(ACITON_BRAKE_WORK_OFF);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 轨迹回放，发送轨迹
     * 由评判模块调用
     */
    public static void sendTrack(Context context, double[] standardTrack, double[] userTrack) {
        Intent intent = new Intent(ACTION_TRACK);
        intent.putExtra("STANDARD_TRACK", standardTrack);
        intent.putExtra("USER_TRACK", userTrack);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 轨迹回放，播放轨迹
     * 由 UI 模块调用
     */
    public static void playTrack(Context context, double[] standardTrack, double[] userTrack) {
        Intent intent = new Intent(ACTION_TRACK_PLAY);
        intent.putExtra("STANDARD_TRACK", standardTrack);
        intent.putExtra("USER_TRACK", userTrack);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 模拟考试扣分的轨迹回放
     */
    public static void showSimulationTrack(Context context, double[] track, long roadId, int[] collisionLineId) {
        Intent intent = new Intent(ACTION_SIMULATE_TRACK);
        intent.putExtra("USER_TRACK", track);
        intent.putExtra("ROAD_ID", roadId);
        intent.putExtra("COLLISION_LINE_ID", collisionLineId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 教学要求的停车位置
     */
    public static void updateCarStopMark(Context context, float[] line, int subjectType, int markId) {
        Intent intent = new Intent(ACTION_CAR_MARK);
        intent.putExtra("LINE", line);
        intent.putExtra("SUBJECTTYPE", subjectType);
        intent.putExtra("MARKID", markId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 画到起点的箭头
     * track 从起点到终点，格式: xyrxyrxyr
     */
    public static void addArrowToBegin(Context context, float[] track) {
        Intent intent = new Intent(ACTION_ARROW_TO_BEGIN);
        intent.putExtra("TRACK", track);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 删除到起点的箭头
     */
    public static void clearArrowToBegin(Context context) {
        Intent intent = new Intent(ACTION_CLEAR_ARROW_TO_BEGIN);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 显示起点位置
     * @param xyr：起点坐标x,y,angle， 考场坐标系， m
     */
    public static void showOriginPosition(Context context, float[] xyr) {
        Intent intent = new Intent(ACTION_ORIGIN_POSITION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 清除起点位置
     */
    public static void clearOriginPosition(Context context) {
        Intent intent = new Intent(ACITON_CLEAR_ORIGIN_POSITION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 绘制警戒线
     * line = null 表示删除这条线
     */
    public static void addAsLine(Context context, double[] line, int id) {
        Intent intent = new Intent(ACTION_AS_LINE);
        intent.putExtra("LINE", line);
        intent.putExtra("ID", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 绘制普通的线
     * 考场坐标系，单位 m
     * line = null 表示删除这条线
     */
    public static void drawGeneralLines(Context context, double[] line, int id) {
        Intent intent = new Intent(ACTION_GENERAL_LINES);
        intent.putExtra("LINE", line);
        intent.putExtra("ID", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 更新列表的项目名
     */
    public static void updateSubjectName(Context context, String msg) {
        Intent intent = new Intent(ACTION_UPDATE_SUBJECT_NAME);
        intent.putExtra("SUBJECT", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * subject 发生变化
     */
    public static void subjectChanged(Context context, long subjectId) {
        Intent intent = new Intent(ACTION_SUBJECT_CHANGED);
        intent.putExtra("SUBJECT_ID", subjectId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 从起点开始模拟考试
     */
    public static void simulationBeginAtBegin(Context context) {
        Intent intent = new Intent(ACTION_SIMULATION_BEGIN_AT_BEGIN);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 列表显示文字
     */
    public static void addMessageToListView(Context context, String msg) {
        Intent intent = new Intent("org.gauto.listview.EVENTLIST");
        intent.putExtra("EVENTLIST", msg);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 显示预测轨迹
     */
    public static void updatePredictTrack(Context context, double[] track) {
        Intent intent = new Intent(ACTION_TRACK_PREDICT);
        intent.putExtra("TRACK", track);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 停车线，画在具体坐标
     */
    public static void updateLineMark(Context context, double[] xyr) {
        Intent intent = new Intent(ACTION_LINE_MARK);
        intent.putExtra("XYR", xyr);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 当考场发生改变时调用
     */
    public static void updateExamId(Context context, long examId) {
        Intent intent = new Intent(ACTION_EXAM_CHANGE);
        intent.putExtra("EXAM_ID", examId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 当车辆所在 road 发生改变时调用
     */
    public static void updateRoadId(Context context, long roadId) {
        Intent intent = new Intent(ACTION_ROAD_CHANGE);
        intent.putExtra("ROAD_ID", roadId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 清空碰撞线
     */
    public static void clearCollisionLine(Context context) {
        Intent intent = new Intent(ACTION_CLEAR_COLLISION_LINE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 显示碰撞线，传索引
     */
    public static void updateCollisionLine(Context context, long roadId, int[] collisionLineId) {
        Intent intent = new Intent(ACTION_COLLISION_LINE);
        intent.putExtra("ROAD_ID", roadId);
        intent.putExtra("LINE_ID", collisionLineId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 设定头像
     */
    public static void updatePortrait(Context context, Bitmap bitmap) {
        Intent intent = new Intent(ACTION_PORTRAIT);
        intent.putExtra("PORTRAIT", bitmap);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * NPC 车辆
     */
    public static void updateNpcCar(Context context, String deviceId, String carName, float x, float y, float angle) {
        Intent intent = new Intent(ACTION_NPC_CAR);
        intent.putExtra("DEVICE_ID", deviceId);
        intent.putExtra("CAR_NAME", carName);
        intent.putExtra("X", x);
        intent.putExtra("Y", y);
        intent.putExtra("ANGLE", angle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
    * 停车时距离边线距离的显示
    * 参数: 1. context; 2. 车身距离边线最近的点(x, y); 3. 对应边线上的点(x, y); 4. 相应的距离 m; 5. 项目rotateDirection
    * 说明: 若设置量参数中的一个为null,表示停止该距离的显示, 根据数组大小确定要显示的线段条数
    */
    public static void showDistance(Context context, float[] posOnCar, float[] posOnLine, float[] distance, long roadId) {
        Intent intent = new Intent(ACTION_SHOW_DISTANCE);
        intent.putExtra("POS_ON_CAR", posOnCar);
        intent.putExtra("POS_ON_LINE", posOnLine);
        intent.putExtra("DISTANCE", distance);
        intent.putExtra("ROAD_ID", roadId);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 显示箭头
     * @param context
     * @param roadId
     * @param subjectType
     * tyepOfDaoku: 倒库的类型, 只在倒库时设置, 规定0为非倒库的项目, 1为右倒库, 2为左倒库
     * 规定设置roadId为-1时,不显示箭头
     */
    public static void showArrow(Context context, long roadId, int subjectType, int typeOfDaoku) {
        Intent intent = new Intent(ACTION_SHOW_DAOKU_ARROW);
        intent.putExtra("ROAD_ID", roadId);
        intent.putExtra("SUBJECT_TYPE", subjectType);
        intent.putExtra("TYPE_OF_DAOKU", typeOfDaoku);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    // 重载上面的方法, 默认 type 为 0
    public static void showArrow(Context context, long roadId, int subjectType) {
        showArrow(context, roadId, subjectType, 0);
    }

    /**
     * 播放level视频
     */
    public static void showCorrectionLevel(Context context){
        Intent intent = new Intent(ACTION_SHOW_CORRECTION_LEVEL);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 入库成功后, 放大距离
     *  设置zoom 为true即放大, false 恢复原先的水平
     */
    public static void zoomMapAfterSucceed(Context context, boolean zoom) {
        Intent intent = new Intent(ACTION_ZOOM_MAP);
        intent.putExtra("ZOOM", zoom);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 提交学习记录
     */
    public static void postRecord(Context context, int missType, int segment, int level, int time , int mode){
        Intent intent = new Intent(ACTION_POST_RECORD);
        intent.putExtra("MISSTYPE",missType);
        intent.putExtra("SUBJECTTYPE",segment);
        intent.putExtra("LEVEL",level);
        intent.putExtra("TIME",time);
        intent.putExtra("MODE",mode);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 资源更新完成
     */
    public static void finishUpdateResource(Context context, boolean allSucceed) {
        Intent intent = new Intent(ACTION_FINISH_UPDATE_RESOURCE);
        intent.putExtra("ALL_SUCCEED", allSucceed);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * OBD 配置完成
     */
    public static void finishConfigObd(Context context, boolean succeed) {
        Intent intent = new Intent(ACTION_FINISH_CONFIG_OBD);
        intent.putExtra("SUCCEED", succeed);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


    /**
     * 完成路径规划，切模式
     */
    public static void pathPlanningAccomplish(Context context) {
        updatePredictTrack(context, null);
        Intent intent = new Intent(ACTION_PATH_PLANNING);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 完成认识车辆某个环节
     */
    public static void updateLearnVehicle(Context context) {
        Intent intent = new Intent(ACTION_LEARN_VEHICLE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 认识车辆，结束
     */
    public static void finishLearnVehicle(Context context) {
        Intent intent = new Intent(ACTION_LEARN_VEHICLE_FINISH);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 向 Unity 发送信息
     */
    public static void sendUnityJson(Context context, String message) {
        Intent intent = new Intent(ACTION_UNITY_JSON);
        intent.putExtra("MESSAGE", message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 显示速度
     * 测试用
     */
    public static void showCarSpeed(Context context, double speed) {
        Intent intent = new Intent(ACTION_SPEED_FOR_DISPLAY);
        intent.putExtra("SPEED", speed);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 请求退出
     */
    public static void quit(Context context) {
        Intent intent = new Intent(ACTION_QUIT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
