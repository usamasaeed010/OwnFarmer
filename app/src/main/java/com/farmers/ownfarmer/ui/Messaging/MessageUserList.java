package com.farmers.ownfarmer.ui.Messaging;

public class MessageUserList {
    private String senderName;
    private String senderImage;
    private String receiverName;
    private String recevierImage;
     public boolean isSeen;
    private String productid;
    private String messageFrom;
    private String messageId;
    private String mediaUrl;
    private String mediaName;
    private String mediaThumbnail;
    private String mediaThumbnailName;
    private String messageText;
    private long messageTime;
    private String messageTo;
    private String messageType;
    private String chatStatus;

    public MessageUserList() {
    }

    public MessageUserList(String senderName, String senderImage, String receiverName, String recevierImage, boolean isSeen, String productid, String messageFrom, String messageId, String mediaUrl, String mediaName, String mediaThumbnail, String mediaThumbnailName, String messageText, long messageTime, String messageTo, String messageType, String chatStatus) {
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.receiverName = receiverName;
        this.recevierImage = recevierImage;
        this.isSeen = isSeen;
        this.productid = productid;
        this.messageFrom = messageFrom;
        this.messageId = messageId;
        this.mediaUrl = mediaUrl;
        this.mediaName = mediaName;
        this.mediaThumbnail = mediaThumbnail;
        this.mediaThumbnailName = mediaThumbnailName;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageTo = messageTo;
        this.messageType = messageType;
        this.chatStatus = chatStatus;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getRecevierImage() {
        return recevierImage;
    }

    public void setRecevierImage(String recevierImage) {
        this.recevierImage = recevierImage;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaThumbnail() {
        return mediaThumbnail;
    }

    public void setMediaThumbnail(String mediaThumbnail) {
        this.mediaThumbnail = mediaThumbnail;
    }

    public String getMediaThumbnailName() {
        return mediaThumbnailName;
    }

    public void setMediaThumbnailName(String mediaThumbnailName) {
        this.mediaThumbnailName = mediaThumbnailName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTo() {
        return messageTo;
    }

    public void setMessageTo(String messageTo) {
        this.messageTo = messageTo;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }
}
