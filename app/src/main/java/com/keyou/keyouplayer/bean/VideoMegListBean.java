package com.keyou.keyouplayer.bean;


import java.util.List;

public class VideoMegListBean {
    private List<Integer> aid;
    private List<Integer> cid;
    private List<Integer> typeid;
    private List<String> play;
    private List<String> review;
    private List<Integer> video_review;
    private List<String> favorites;
    private List<Integer> mid;
    private List<String> fans;
    private List<String> credit;
    private List<String> coins;
    private List<String> danmaku;
    private List<String> typename;
    private List<String> title;
    private List<String> subtitle;
    private List<String> author;
    private List<String> create;
    private List<String> description;
    private List<String> pic;
    private List<String> duration;
    private List<String> ava;





    public VideoMegListBean(){

    }

    public List<Integer> getAid() {
        return aid;
    }

    public void setAid(List<Integer> aid) {
        this.aid = aid;
    }

    public List<Integer> getTypeid() {
        return typeid;
    }

    public void setTypeid(List<Integer> typeid) {
        this.typeid = typeid;
    }

    public List<String> getPlay() {
        return play;
    }

    public void setPlay(List<String> play) {
        this.play = play;
    }

    public List<String> getReview() {
        return review;
    }

    public void setReview(List<String> review) {
        this.review = review;
    }

    public List<Integer> getVideo_review() {
        return video_review;
    }

    public void setVideo_review(List<Integer> video_review) {
        this.video_review = video_review;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public List<Integer> getMid() {
        return mid;
    }

    public void setMid(List<Integer> mid) {
        this.mid = mid;
    }

    public List<String> getCredit() {
        return credit;
    }

    public void setCredit(List<String> credit) {
        this.credit = credit;
    }

    public List<String> getCoins() {
        return coins;
    }

    public void setCoins(List<String> coins) {
        this.coins = coins;
    }

    public List<String> getTypename() {
        return typename;
    }

    public void setTypename(List<String> typename) {
        this.typename = typename;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(List<String> subtitle) {
        this.subtitle = subtitle;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public List<String> getCreate() {
        return create;
    }

    public void setCreate(List<String> create) {
        this.create = create;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public List<String> getDuration() {
        return duration;
    }

    public void setDuration(List<String> duration) {
        this.duration = duration;
    }

    public List<String> getDanmaku() {
        return danmaku;
    }

    public void setDanmaku(List<String> danmaku) {
        this.danmaku = danmaku;
    }

    public List<String> getAva() {
        return ava;
    }

    public void setAva(List<String> ava) {
        this.ava = ava;
    }

    public List<String> getFans() {
        return fans;
    }

    public void setFans(List<String> fans) {
        this.fans = fans;
    }

    public List<Integer> getCid() {
        return cid;
    }

    public void setCid(List<Integer> cid) {
        this.cid = cid;
    }
}
//返回字段 "list" 子项
//        返回值字段	字段类型	字段说明
//        aid	int	视频编号
//        typeid	int	视频分类ID
//        typename	string	视频分类名称
//        title	string	视频标题
//        subtitle	string	视频副标题
//        play	int	播放次数
//        review	int	评论数
//        video_review	int	弹幕数
//        favorites	int	收藏数
//        author	string	视频作者
//        mid	int	视频作者ID
//        create	string	视频创建日期
//        description	string	视频简介
//        pic	string	封面图片地址
//        credit	int	评分数量
//        coins	int	推荐数量
//        duration	string	视频时长