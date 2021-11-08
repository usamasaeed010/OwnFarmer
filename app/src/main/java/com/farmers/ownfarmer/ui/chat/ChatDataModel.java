package com.farmers.ownfarmer.ui.chat;

public class ChatDataModel {
    private Boolean isSeen;
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

    public ChatDataModel() {
    }

    public ChatDataModel(Boolean isSeen, String messageFrom, String messageId, String mediaUrl, String mediaName, String mediaThumbnail, String mediaThumbnailName, String messageText, long messageTime, String messageTo, String messageType) {
        this.isSeen = isSeen;
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
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
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
}
