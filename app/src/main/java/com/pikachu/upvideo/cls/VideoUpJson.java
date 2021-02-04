/**
 * 项目结构
 * 用于传输。创建视频，服务器端构建
 *
 */
package com.pikachu.upvideo.cls;

import android.annotation.SuppressLint;

import com.amap.api.location.AMapLocation;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class VideoUpJson implements Serializable {



    //项目名
    private String projectName;
    //项目创建时间
    private String projectTime;
    //项目创建地址/
    private String projectAddress;

    //项目状态 - > 0 本地项目（未上传的，位同步的，包括修改服务器项目但未上传的）， 1 已同步的项目（已上传的，本地也有的） ，2 服务器项目（本地没有，纯服务器）
    private int projectType;

    //分段模式 -> 0不分段 , 1 手动  ，2 距离（经纬度），3 时间（分钟）
    private int projectMode;
    //分段模式   记录（时间，距离）
    private long projectModeInfo;

    //视频比例 用在子视频上也要有。这里的优先级低于子视频上的  0（4：3） 1（16：9） 2...
    private int projectVideoWh;
    //视频清晰度 用在子视频上也要有。这里的优先级低于子视频上的  0 (720P) 1()
    private int projectVideoPx;
    //子项目
    private List<SonProject> listSon;




    public VideoUpJson(String projectName, String projectTime,
                       String projectAddress, int projectType,
                       int projectMode, long projectModeInfo,
                       int projectVideoWh, int projectVideoPx,
                       List<SonProject> listSon) {
        this.projectName = projectName;
        this.projectTime = projectTime;
        this.projectAddress = projectAddress;
        this.projectType = projectType;
        this.projectMode = projectMode;
        this.projectModeInfo = projectModeInfo;
        this.projectVideoWh = projectVideoWh;
        this.projectVideoPx = projectVideoPx;
        this.listSon = listSon;
    }




    //保存子项目
    public static class SonProject implements Serializable {

        //子项目名
        private String sonProjectName;
        //子项目备注
        private String sonProjectMsg;
        //子项目开始地点数据
        private MapInfo sonProjectStartMapInfo; // 动态更新
        private MapInfo sonProjectEndMapInfo; // 动态更新
        //历史视频
        private List<ListHisVideo> sonProjectVideo;

        //子视频
        public static class ListHisVideo implements Serializable {
            //视频名
            private String videoName;
            //拍摄时间
            private String videoTime;
            //状态 - > 0 本地项目（未上传的）， 1 服务器项目（已上传的） ，2 服务器项目修改但未同步
            private int videoType;
            //视频比例 。这里的优先级高于项目  0跟随项目  1（4：3） 2（16：9） 3...
            private int videoWh;
            //视频清晰度。这里的优先级高于项目 0跟随项目， 1 (720P) 2()
            private int videoPx;
            //视频地点数据
            private MapInfo videoMapInfo;

            public String getVideoName() {
                return videoName;
            }

            public void setVideoName(String videoName) {
                this.videoName = videoName;
            }

            public String getVideoTime() {
                return videoTime;
            }

            public void setVideoTime(String videoTime) {
                this.videoTime = videoTime;
            }

            public int getVideoType() {
                return videoType;
            }

            public void setVideoType(int videoType) {
                this.videoType = videoType;
            }

            public int getVideoWh() {
                return videoWh;
            }

            public void setVideoWh(int videoWh) {
                this.videoWh = videoWh;
            }

            public int getVideoPx() {
                return videoPx;
            }

            public void setVideoPx(int videoPx) {
                this.videoPx = videoPx;
            }

            public MapInfo getVideoMapInfo() {
                return videoMapInfo;
            }

            public void setVideoMapInfo(MapInfo videoMapInfo) {
                this.videoMapInfo = videoMapInfo;
            }
        }

        public String getSonProjectName() {
            return sonProjectName;
        }

        public void setSonProjectName(String sonProjectName) {
            this.sonProjectName = sonProjectName;
        }

        public String getSonProjectMsg() {
            return sonProjectMsg;
        }

        public void setSonProjectMsg(String sonProjectMsg) {
            this.sonProjectMsg = sonProjectMsg;
        }

        public MapInfo getSonProjectStartMapInfo() {
            return sonProjectStartMapInfo;
        }

        public void setSonProjectStartMapInfo(MapInfo sonProjectStartMapInfo) {
            this.sonProjectStartMapInfo = sonProjectStartMapInfo;
        }

        public List<ListHisVideo> getSonProjectVideo() {
            return sonProjectVideo;
        }

        public void setSonProjectVideo(List<ListHisVideo> sonProjectVideo) {
            this.sonProjectVideo = sonProjectVideo;
        }

        public MapInfo getSonProjectEndMapInfo() {
            return sonProjectEndMapInfo;
        }

        public void setSonProjectEndMapInfo(MapInfo sonProjectEndMapInfo) {
            this.sonProjectEndMapInfo = sonProjectEndMapInfo;
        }

        //地址详细数据
        public static class MapInfo implements Serializable {
            private int locationType; //定位类型
            private double latitude; //纬度
            private double longitude; //经度
            private float accuracy; //精度信息(单位：米)
            private double altitude; // 海拔高度(单位：米)
            private float bearing; // 方向 度 取值范围：【0，360】，其中0度表示正北方向，90度表示正东，180度表示正南，270度表示正西
            private String address;//地址
            private String country; //国家信息
            private String province; //省信息
            private String city; // 城市信息
            private String district; // 城区信息
            private String street; // 街道信息
            private String streetNum; // 街道门牌号
            private String cityCode; // 城市编码
            private String adCode; // 地区编码
            private String aoiName; // 定位点的AOI信息
            private String buildingId; // 室内定位的建筑物Id
            private String floor; // 室内定位的楼层
            private int gpsAccuracyStatus; // GPS的状态
            private int satellites ; //当前可用卫星数量
            private float speed; // 当前速度(单位：米/秒)
            private long time; // 定位时间


            public MapInfo(int locationType, double latitude, double longitude,
                           float accuracy, double altitude, float bearing, String address,
                           String country, String province, String city,
                           String district, String street, String streetNum,
                           String cityCode, String adCode, String aoiName,
                           String buildingId, String floor, int gpsAccuracyStatus,
                           int satellites, float speed, long time) {
                this.locationType = locationType;
                this.latitude = latitude;
                this.longitude = longitude;
                this.accuracy = accuracy;
                this.altitude = altitude;
                this.bearing = bearing;
                this.address = address;
                this.country = country;
                this.province = province;
                this.city = city;
                this.district = district;
                this.street = street;
                this.streetNum = streetNum;
                this.cityCode = cityCode;
                this.adCode = adCode;
                this.aoiName = aoiName;
                this.buildingId = buildingId;
                this.floor = floor;
                this.gpsAccuracyStatus = gpsAccuracyStatus;
                this.satellites = satellites;
                this.speed = speed;
                this.time = time;
            }

            public String getBearingStr(float bearing){
                if (bearing ==  0 || bearing == 360) return "正北";
                else if (bearing > 0 && bearing < 90)  return "北偏东" + bearing  +"度";
                else if (bearing == 90 )  return "正东";
                else if (bearing > 90 && bearing < 180 )  return "东偏南" + (bearing - 90) + "度";
                else if (bearing == 180 )  return "正南";
                else if (bearing > 180 && bearing < 270 )  return "东偏南" + (bearing - 180) + "度";
                else if (bearing == 270 )  return "正西";
                else return "西偏北" + (bearing - 270) + "度";
            }



            @SuppressLint("SimpleDateFormat")
            @Override
            public String toString() {
                return "定位类型: " + locationType + "\n" +
                        "纬度: " + latitude + "\n" +
                        "经度: " + longitude + "\n" +
                        "精度信息(单位：米): " + accuracy + "\n" +
                        "海拔高度(单位：米) : "  + altitude + "\n" +
                        "方向: "  + getBearingStr(bearing)  + "\n" +
                        "地址: " + address + "\n" +
                        "国家: " + country + "\n" +
                        "省信: " + province + "\n" +
                        "城市: " + city + "\n" +
                        "城区: " + district + "\n" +
                        "街道: " + street + "\n" +
                        "街道门牌号: " + streetNum + "\n" +
                        "城市编码: " + cityCode + "\n" +
                        "地区编码: " + adCode + "\n" +
                        "定位点的AOI信息: " + aoiName + "\n" +
                        "室内定位的建筑物Id: " + buildingId + "\n" +
                        "室内定位的楼层: " + floor + "\n" +
                        "GPS的状态: " + gpsAccuracyStatus + "\n" +
                        "当前可用卫星数量: "  + satellites  + "\n" +
                        "当前速度(单位：米/秒): "  + speed + "\n" +
                        "定位时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(time));
            }


            public int getLocationType() {
                return locationType;
            }

            public void setLocationType(int locationType) {
                this.locationType = locationType;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public float getAccuracy() {
                return accuracy;
            }

            public void setAccuracy(float accuracy) {
                this.accuracy = accuracy;
            }

            public double getAltitude() {
                return altitude;
            }

            public void setAltitude(double altitude) {
                this.altitude = altitude;
            }

            public float getBearing() {
                return bearing;
            }

            public void setBearing(float bearing) {
                this.bearing = bearing;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreetNum() {
                return streetNum;
            }

            public void setStreetNum(String streetNum) {
                this.streetNum = streetNum;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getAdCode() {
                return adCode;
            }

            public void setAdCode(String adCode) {
                this.adCode = adCode;
            }

            public String getAoiName() {
                return aoiName;
            }

            public void setAoiName(String aoiName) {
                this.aoiName = aoiName;
            }

            public String getBuildingId() {
                return buildingId;
            }

            public void setBuildingId(String buildingId) {
                this.buildingId = buildingId;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public int getGpsAccuracyStatus() {
                return gpsAccuracyStatus;
            }

            public void setGpsAccuracyStatus(int gpsAccuracyStatus) {
                this.gpsAccuracyStatus = gpsAccuracyStatus;
            }

            public int getSatellites() {
                return satellites;
            }

            public void setSatellites(int satellites) {
                this.satellites = satellites;
            }

            public float getSpeed() {
                return speed;
            }

            public void setSpeed(float speed) {
                this.speed = speed;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }
        }
    }


    public static SonProject.MapInfo aMapLocation2MapInfo(AMapLocation aMapLocation) {
        return new SonProject.MapInfo(
                aMapLocation.getLocationType(),
                aMapLocation.getLatitude(),
                aMapLocation.getLongitude(),
                aMapLocation.getAccuracy(),
                aMapLocation.getAltitude(),
                aMapLocation.getBearing(),
                aMapLocation.getAddress(),
                aMapLocation.getCountry(),
                aMapLocation.getProvince(),
                aMapLocation.getCity(),
                aMapLocation.getDistrict(),
                aMapLocation.getStreet(),
                aMapLocation.getStreetNum(),
                aMapLocation.getCityCode(),
                aMapLocation.getAdCode(),
                aMapLocation.getAoiName(),
                aMapLocation.getBuildingId(),
                aMapLocation.getFloor(),
                aMapLocation.getGpsAccuracyStatus(),
                aMapLocation.getSatellites(),
                aMapLocation.getSpeed(),
                aMapLocation.getTime()
        );
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(String projectTime) {
        this.projectTime = projectTime;
    }

    public String getProjectAddress() {
        return projectAddress;
    }

    public void setProjectAddress(String projectAddress) {
        this.projectAddress = projectAddress;
    }

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public int getProjectMode() {
        return projectMode;
    }

    public void setProjectMode(int projectMode) {
        this.projectMode = projectMode;
    }

    public long getProjectModeInfo() {
        return projectModeInfo;
    }

    public void setProjectModeInfo(long projectModeInfo) {
        this.projectModeInfo = projectModeInfo;
    }

    public int getProjectVideoWh() {
        return projectVideoWh;
    }

    public void setProjectVideoWh(int projectVideoWh) {
        this.projectVideoWh = projectVideoWh;
    }

    public int getProjectVideoPx() {
        return projectVideoPx;
    }

    public void setProjectVideoPx(int projectVideoPx) {
        this.projectVideoPx = projectVideoPx;
    }

    public List<SonProject> getListSon() {
        return listSon;
    }

    public void setListSon(List<SonProject> listSon) {
        this.listSon = listSon;
    }
}
