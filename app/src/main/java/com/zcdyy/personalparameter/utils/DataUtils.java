package com.zcdyy.personalparameter.utils;

import com.zcdyy.personalparameter.bean.Comment;
import com.zcdyy.personalparameter.bean.CommentInfo;
import com.zcdyy.personalparameter.bean.HealthCircle;
import com.zcdyy.personalparameter.bean.PraiseInfo;
import com.zcdyy.personalparameter.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchuan on 2017/5/11.
 */

public class DataUtils {
    public   List<HealthCircle> healthCircleList = new ArrayList<>();
    public  List<PraiseInfo> praiseInfoList = new ArrayList<>();
    public  List<CommentInfo> commentInfoList = new ArrayList<>();
    public  List<UserInfo> articleUserList = new ArrayList<>();

    public List<UserInfo> praiseUserInfo = new ArrayList<>();
    public List<UserInfo> commentUserInfo = new ArrayList<>();
    public List<UserInfo> replyUserInfo = new ArrayList<>();
    /**
     * 设置文章的数据
     * @param userId
     * @return
     */
//    public List<Article> setArticleData(String userId){
//        List<Article> list = new ArrayList<>();
//        for (int i = 0;i< healthCircleList.size();i++){
//            Article article = new Article();
//            article.setHead( articleUserList.get(i).getHead());
//            article.setId( healthCircleList.get(i).getObjectId());
//            article.setCommentCount( healthCircleList.get(i).getCommentCount());
//            article.setContent( healthCircleList.get(i).getContent());
//            article.setName( articleUserList.get(i).getName());
//            article.setPic( healthCircleList.get(i).isPic());
//            if (article.isPic()){
//                article.setImg( healthCircleList.get(i).getImg());
//            }
//            article.setPraiseCount( healthCircleList.get(i).getPraiseCount());
//            article.setPraise(false);
////            for (PraiseInfo p: praiseInfoList){
////                if (p.getUser_id().equals(userId)&&
////                        p.getNews_id().equals( healthCircleList.get(i).getObjectId())){
////                    article.setPraise(true);
////                    break;
////                }
////            }
//            list.add(article);
//        }
//        return list;
//    }
//
//    public Article getArticle(){
//        Article article = new Article();
//        article.setHead( articleUserList.get(0).getHead());
//        article.setId( healthCircleList.get(0).getObjectId());
//        article.setCommentCount( healthCircleList.get(0).getCommentCount());
//        article.setContent( healthCircleList.get(0).getContent());
//        article.setImg( healthCircleList.get(0).getImg());
//        article.setName( articleUserList.get(0).getName());
//        article.setPic( healthCircleList.get(0).isPic());
//        article.setPraiseCount( healthCircleList.get(0).getPraiseCount());
//        article.setPraise(BmobUtils.hasPraise);
//        return article;
//    }
//
//    public List<Praise> getPraiseList(){
//        List<Praise> list = new ArrayList<>();
//        for (int i = 0;i<praiseInfoList.size();i++){
//            Praise praise = new Praise();
////            for (UserInfo userInfo:praiseUserInfo){
////                if (userInfo.getId().equals(praiseInfoList.get(i).getUser_id())){
////                    praise.setHead(userInfo.getHead());
////                    praise.setName(userInfo.getName());
////                    break;
////                }
////            }
//            praise.setTimeStr(praiseInfoList.get(i).getCreatedAt());
//            list.add(praise);
//        }
//        return list;
//    }
//
//    public List<Comment> getCommetList(){
//        List<Comment> list = new ArrayList<>();
//        for (int i = 0;i<commentInfoList.size();i++){
//            Comment comment = new Comment();
////            for (UserInfo userInfo:commentUserInfo){
////                if (userInfo.getId().equals(commentInfoList.get(i).getUser_id())){
////                    comment.setName(userInfo.getName());
////                    comment.setHead(userInfo.getHead());
////                    break;
////                }
////            }
////            comment.setContent(commentInfoList.get(i).getContent());
////            comment.setReply(commentInfoList.get(i).is_reply());
////            if (comment.isReply()){
////                comment.setReplyId(commentInfoList.get(i).getReply_id());
////                for (UserInfo userInfo:replyUserInfo){
////                    if (userInfo.getId().equals(commentInfoList.get(i).getReply_id())){
////                        comment.setReplyName(userInfo.getName());
////                        break;
////                    }
////                }
////            }
////            comment.setUserId(commentInfoList.get(i).getUser_id());
////            comment.setTimeStr(commentInfoList.get(i).getCreatedAt());
////            list.add(comment);
//        }
//        return list;
//    }
}
