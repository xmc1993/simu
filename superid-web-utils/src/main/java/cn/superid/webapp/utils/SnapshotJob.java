package cn.superid.webapp.utils;

/**
 * Created by njutms on 16/6/8.
 */
public class SnapshotJob {
    private String id;
    private String userData;
    private String pipelineId;
    private String state;
    private String code;
    private String count;
    private String message;
    private String creationTime;
    private SnapshotJob.Input input;
    private SnapshotJob.SnapshotConfig snapshotConfig;

    public SnapshotJob() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserData() {
        return this.userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getPipelineId() {
        return this.pipelineId;
    }

    public void setPipelineId(String pipelineId) {
        this.pipelineId = pipelineId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCount() {
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public SnapshotJob.Input getInput() {
        return this.input;
    }

    public void setInput(SnapshotJob.Input input) {
        this.input = input;
    }

    public SnapshotJob.SnapshotConfig getSnapshotConfig() {
        return this.snapshotConfig;
    }

    public void setSnapshotConfig(SnapshotJob.SnapshotConfig snapshotConfig) {
        this.snapshotConfig = snapshotConfig;
    }

    public static class SnapshotConfig {
        private String time;
        private String interval;
        private String num;
        private SnapshotJob.SnapshotConfig.OutputFile outputFile;

        public SnapshotConfig() {
        }

        public String getTime() {
            return this.time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getInterval() {
            return this.interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getNum() {
            return this.num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public SnapshotJob.SnapshotConfig.OutputFile getOutputFile() {
            return this.outputFile;
        }

        public void setOutputFile(SnapshotJob.SnapshotConfig.OutputFile outputFile) {
            this.outputFile = outputFile;
        }

        public static class OutputFile {
            private String bucket;
            private String location;
            private String object;

            public OutputFile() {
            }

            public String getBucket() {
                return this.bucket;
            }

            public void setBucket(String bucket) {
                this.bucket = bucket;
            }

            public String getLocation() {
                return this.location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getObject() {
                return this.object;
            }

            public void setObject(String object) {
                this.object = object;
            }
        }
    }

    public static class Input {
        private String bucket;
        private String location;
        private String object;

        public Input() {
        }

        public String getBucket() {
            return this.bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getObject() {
            return this.object;
        }

        public void setObject(String object) {
            this.object = object;
        }
    }
}