package weibo.model;

public class WeiboInfo {

    private String userName;
    private String date;
    private String userid;
    private String weiboid;
    private String weiboSentence;
    private String url;
    private int praisedNum;
    private int forwardNum;
    private int commentNum;

    public WeiboInfo() {

    }

    public WeiboInfo(String userName, String date, String userid, String weiboid, String weiboSentence,String url, int praisedNum, int forwardNum, int commentNum) {
        this.userName = userName;
        this.url = url;
        this.date = date;
        this.userid = userid;
        this.weiboid = weiboid;
        this.weiboSentence = weiboSentence;
        this.praisedNum = praisedNum;
        this.forwardNum = forwardNum;
        this.commentNum = commentNum;
    }

    @Override
    public String toString() {
        return userName + "," + userid +
                "," + date +
                "," + weiboid +
                ",@@" + weiboSentence + "@@" +
                "," + url +
                "," + praisedNum +
                "," + forwardNum +
                "," + commentNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getWeiboid() {
        return weiboid;
    }

    public void setWeiboid(String weiboid) {
        this.weiboid = weiboid;
    }

    public String getWeiboSentence() {
        return weiboSentence;
    }

    public void setWeiboSentence(String weiboSentence) {
        this.weiboSentence = weiboSentence;
    }

    public int getPraisedNum() {
        return praisedNum;
    }

    public int getForwardNum() {
        return forwardNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
