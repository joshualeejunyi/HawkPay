package cxa.overclockedtoaster.hawkpay;

import java.sql.Time;
import java.sql.Timestamp;

public class Reward {
    private Integer userid;
    private Timestamp rewardtimestamp;
    private Boolean approved;

    public Reward(Integer userid, Timestamp rewardtimestamp, Boolean approved) {
        this.userid = userid;
        this.rewardtimestamp = rewardtimestamp;
        this.approved = approved;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Timestamp getRewardtimestamp() {
        return rewardtimestamp;
    }

    public void setRewardtimestamp(Timestamp rewardtimestamp) {
        this.rewardtimestamp = rewardtimestamp;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
